package com.outdoorclassroom;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //2-dimensional array, containing array of walks
    ArrayList walks = new ArrayList();

    //Colour counter for markers and polylines
    int cCOUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public Walk readCsvCoord () {

        Walk walk = new Walk ();

        try {
            AssetManager am = getAssets();
            InputStream is = am.open("EHHWv2.csv");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            String line = "";
            br.readLine();

            //for start and end
            line = br.readLine();
            String[] tokens = line.split(",");
            LatLng start = new LatLng(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
            walk.setStart(start);

            LatLng waypoint = new LatLng(-33.802222, 151.286979);

            while ( (line = br.readLine()) != null) {
                tokens = line.split(",");

                waypoint = new LatLng(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
                walk.addWpt(waypoint);
            }

            walk.removeLastWpt();
            walk.setEnd(waypoint);

        } catch (Exception e) {
            Log.d("CSV Reader Task", e.toString());
        }
        return walk;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng eHillHeritage = new LatLng(-33.802222, 151.286979);
        //mMap.addMarker(new MarkerOptions().position(eHillHeritage).title("E Hill Heritage Walk"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eHillHeritage, 15));
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(20.0f);

        // function to pass coordinates to onMapPoints

        Walk eHills = readCsvCoord();
        parseCoord(eHills);

        // function for onMapPoints
    }

    private void parseCoord (Walk walk) {

        //will implement loop for each walk
        onMapPoints(walk.getStart(), walk.getEnd());
    }

    //will implement overloaded onMapPoints function with parameters for waypoints

    //plots start and end of a walk, sends HTTP request for directions
    public void onMapPoints (LatLng start, LatLng end) {

        //limiter to how many walks are on the map
        if (walks.size() > 1) {
            walks.clear();
            mMap.clear();
        }

        //create array for walk (start, end) and add them
        ArrayList <LatLng> walk = new ArrayList<LatLng>();
        walk.add(start);
        walk.add(end);

        //add walk to markerPoints array
        walks.add(walk);

        //create MarkerOptions for start and end
        MarkerOptions startOpt = new MarkerOptions();
        MarkerOptions endOpt = new MarkerOptions();

        startOpt.position(start);
        endOpt.position(end);

        startOpt = markerSetup(startOpt);
        endOpt = markerSetup(endOpt);

        mMap.addMarker(startOpt);
        mMap.addMarker(endOpt);

        // check that new points were captured

        if (walk.size() >= 2) {
            LatLng origin = walk.get(0);
            LatLng dest = walk.get(1);

            //Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin,dest);

            //Download json data from Google Directions API
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }

        //don't increment cCOUNT, should only be done ONCE at ParserTask when drawing polyline

    }

    private MarkerOptions markerSetup (MarkerOptions markerOptions) {

        switch (cCOUNT) {
            case 0:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                break;
            case 1:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                break;
            case 2:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            default:
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                break;
        }

        return markerOptions;
    }



    private class DownloadTask extends AsyncTask <String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask <String, Integer, List<List<HashMap<String, String>>>> {

        // parsing data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            //MarkerOptions markerOptions = new MarkerOptions();


            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap <String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions = lineSetup(lineOptions);
            }

            mMap.addPolyline(lineOptions);
            cCOUNT++;
        }
    }

    private PolylineOptions lineSetup (PolylineOptions lineOptions) {

        lineOptions.width(12);
        lineOptions.geodesic(true);

        switch (cCOUNT) {
            case 0:
                lineOptions.color(Color.rgb(255,165,0));
                break;
            case 1:
                lineOptions.color(Color.BLUE);
                break;
            case 2:
                lineOptions.color(Color.GREEN);
                break;
            default:
                lineOptions.color(Color.RED);
                break;
        }

        return lineOptions;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    private String getDirectionsUrl (LatLng origin, LatLng dest) {

        //string of origin and destination
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;

        //Sensors and mode
        //String sensor = "sensor=false";
        String mode = "mode=driving";

        //Output format
        String output = "json";

        //Build parameters for http request
        String parameters = strOrigin + "&" + strDest + "&" + mode;

        //building url http request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

}
