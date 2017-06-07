package com.outdoorclassroom;

/**
 * Ctrl-F following index points to find methods for each implementation
 * I-01: Plot the Walk
 * I-02: Plot the Landmarks
 * I-UP: User Permissions
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.model.MapStyleOptions;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.outdoorclassroom.App.getContext;

public class MapsActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static final String MAP_ID = "map_id";

    private boolean mPermissionDenied = false;

    //array of Walk objects; representing each walk implemented into app
    HashMap<String,Walk> routes = new HashMap<>();
    //hashmap of Landmark objects identified by name; representing each landmark guided by walks
    ArrayList<HashMap<String,Landmark>> walkLandmarks = new ArrayList<>();

    // default Manly LatLng
    LatLng MANLY_CENTRE = new LatLng(-33.802222, 151.286979);
    // Constrain the camera target to Manly bounds
    LatLngBounds MANLY_BOUNDS = new LatLngBounds(
            new LatLng(-33.823213, 151.249588), new LatLng(-33.782981, 151.309864)
    );

    //Colour counter for markers and polylines
    int cCOUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.map_activity));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                startActivity(new Intent(MapsActivity.this, HelpActivity.class));
                return true;
            default:
                Toast.makeText(getBaseContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                return true;
        }
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

        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        //my location settings
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        //zoom controls enabled
        mMap.getUiSettings().setZoomControlsEnabled(true);

        /**
         * Implemented from
         * http://wptrafficanalyzer.in/blog/google-map-android-api-v2-switching-between-normal-view-satellite-view-and-terrain-view/
         */
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);
        rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_norm) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else if (checkedId == R.id.rb_sat) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                else if (checkedId == R.id.rb_terr) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }
        });


        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(20.0f);
        // setting bounds to map
        mMap.setLatLngBoundsForCameraTarget(MANLY_BOUNDS);

        /**
         * This is where the walks plotted in the csv files are implemented into the app
         * All that needs to be done is to pass the csv filename to the readCsvCoord() function
         * to retrieve the resulting Walk object
         * Be sure to have placed the csv file in the assets folder, and that the name is correct
         * Then store the walk in the HashMap
         */
        if (routes.size() <= 5) {
            Walk eHills = readCsvCoord("EHHWv3.csv");
            routes.put("Eastern Hills", eHills);

            Walk corso = readCsvCoord("corso.csv");
            routes.put("Corso", corso);

            Walk pittwater = readCsvCoord("pittwater.csv");
            routes.put("Pittwater Road", pittwater);

            Walk manlyScenic = readCsvCoord("manly_scenic.csv");
            routes.put("Manly Scenic", manlyScenic);

            Walk heart = readCsvCoord("heart.csv");
            routes.put("Heart of Manly", heart);
        }

        /**
         * This is where the landmarks are created and stored
         * Similar to how the walks are processed, all that is needed is to
         * pass the csv filename containing the landmarks to the readCsvLandmarks() function
         * This retrieves a HashMap containing all the landmarks (as per walk)
         * Order here is important
         */
        if (walkLandmarks.size() <= 6) {
            String landmarksTest = "ehLand.csv";
            HashMap<String, Landmark> eHillsLand = readCsvLandmarks(landmarksTest);
            walkLandmarks.add(0,eHillsLand);

            String corsoName = "corsoLand.csv";
            HashMap<String, Landmark> corsoLand = readCsvLandmarks(corsoName);
            walkLandmarks.add(1,corsoLand);

            String pittName = "pittLand.csv";
            HashMap<String, Landmark> pittLand = readCsvLandmarks(pittName);
            walkLandmarks.add(2,pittLand);

            HashMap<String, Landmark> scenic = new HashMap<>(); //empty list
            walkLandmarks.add(3,scenic);

            String heartName = "heartLand.csv";
            HashMap<String, Landmark> heartLand = readCsvLandmarks(heartName);
            walkLandmarks.add(4, heartLand);

            String plaquesName = "plaquesLand.csv";
            HashMap<String, Landmark> plaquesLand = readCsvLandmarks(plaquesName);
            walkLandmarks.add(5, plaquesLand);
        }

        // check to see which walk to display
        Bundle data = getIntent().getExtras();
        // if coming from MainActivity (therefore, id is not established)
        String id = "100";
        if (data != null) {
            id = data.getString(MAP_ID);
        }

        /**
         * This plots the walks and landmarks
         */
        String key;
        switch (id) {
            default: {
                //plot all csv's; make loop to traverse routes
                Iterator it = routes.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry entry = (HashMap.Entry)it.next();
                    parseCoord((Walk) entry.getValue());
                }
                HashMap<String, Landmark> allLandmarks = new HashMap<>();
                for (int i=0;i<walkLandmarks.size();i++) {
                    allLandmarks.putAll(walkLandmarks.get(i));
                }
                parseCoord(allLandmarks);
                // Add a marker in Sydney and move the camera
                LatLng eHillHeritage = new LatLng(-33.802222, 151.286979);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eHillHeritage, 15));
                break;
            }
            case "1": {
                key = "Eastern Hills";
                Walk walk = routes.get(key);
                parseCoord(walk);
                parseCoord(walkLandmarks.get(Integer.parseInt(id) - 1));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(walk.getStart(), 18));
                break;
            }
            case "2": {
                key = "Corso";
                Walk walk = routes.get(key);
                parseCoord(walk);
                parseCoord(walkLandmarks.get(Integer.parseInt(id) - 1));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(walk.getStart(), 18));
                break;
            }
            case "3": {
                key = "Pittwater Road";
                Walk walk = routes.get(key);
                parseCoord(walk);
                parseCoord(walkLandmarks.get(Integer.parseInt(id) - 1));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(walk.getStart(), 18));
                break;
            }
            case "4": {
                key = "Manly Scenic";
                Walk walk = routes.get(key);
                parseCoord(walk);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(walk.getStart(), 18));
            }
            case "5": {
                key = "Heart of Manly";
                Walk walk = routes.get(key);
                parseCoord(walk);
                parseCoord(walkLandmarks.get(Integer.parseInt(id) - 1));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(walk.getStart(), 18));
                break;
            }

        }
        mMap.setOnInfoWindowClickListener(this);
    }


    /*
    I-01: Following is to implement methods to PLOT THE WALK
     */

    public Walk readCsvCoord (String filename) {

        Walk walk = new Walk ();

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(filename);  //error for EHHWv2.csv
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            //String line = "";
            //br.readLine();

            CSVReader reader = new CSVReader(br);
            reader.readNext(); //first line of titles

            String [] tokens;
            tokens = reader.readNext(); //for start
            LatLng start = new LatLng(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
            walk.setStart(start);

            LatLng waypoint = MANLY_CENTRE;   // default value of Manly

            while ( (tokens = reader.readNext()) != null) {
                waypoint = new LatLng(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
                walk.addWpt(waypoint);
            }

            walk.removeLastWpt();
            walk.setEnd(waypoint);

        } catch (Exception e) {
            Log.d("CSV Walk Read", e.toString());
        }
        return walk;
    }

    private void parseCoord (Walk walk) { onMapPoints(walk); }

    private void parseCoord (HashMap landmarks) {
        plotLandmarks(landmarks);
    }

    //plot landmarks on Map
    private void plotLandmarks(HashMap landmarks) {

        for (Object o : landmarks.entrySet()) {
            Map.Entry me = (Map.Entry) o;
            MarkerOptions marker = new MarkerOptions();
            Landmark l = (Landmark) me.getValue();
            marker = markerSetup(l.getName(), l, marker);
            mMap.addMarker(marker);
        }

    }

    private MarkerOptions markerSetup (String n, Landmark l, MarkerOptions m) {
        m.position(l.getLatLng());
        m.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_4));
        m.title(l.getName());
        m.snippet(l.getSummary());
        return m;
    }

    //will implement overloaded onMapPoints function with parameters for waypoints
    public void onMapPoints (Walk walk) {

        /*limiter to how many walks are on the map
        if (routes.size() > 3) {
            routes.clear();
            mMap.clear();
        }
        */

        //use Walk object arg

        //add Walk object to all routes/walks
        //routes.put(name,walk);

        //instantiate Marker options for start and end markers
        MarkerOptions startOpt = new MarkerOptions();
        MarkerOptions endOpt = new MarkerOptions();
        startOpt.position(walk.getStart());
        endOpt.position(walk.getEnd());
        //retrieve marker settings
        startOpt = markerSetup(startOpt);
        endOpt = markerSetup(endOpt);


        mMap.addMarker(startOpt);
        mMap.addMarker(endOpt);

        if (routes.size() >= 1) {
            /**
            LatLng origin = walk.getStart();
            LatLng dest = walk.getEnd();
             */

            PolylineOptions lineOptions = addPoints(walk);

            lineOptions = lineSetup(lineOptions);
            mMap.addPolyline(lineOptions);

        }
        cCOUNT++;
    }

    // to be modified for landmarks (maybe)
    private MarkerOptions markerSetup (MarkerOptions markerOptions) {

        switch (cCOUNT) {
            case 0:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_7));
                break;
            case 1:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_4));
                break;
            case 2:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_6));
                break;
            case 3:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_5));
                break;
            default:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_8));
                break;
        }

        return markerOptions;
    }

    //creating line for Map
    private PolylineOptions addPoints (Walk walk) {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.add(walk.getStart());
        for (int i=0; i<walk.wptsSize(); i++) {
            lineOptions.add(walk.getWpt(i));
        }
        lineOptions.add(walk.getEnd());
        return lineOptions;
    }

    //adjusting line options (colour and width)
    private PolylineOptions lineSetup (PolylineOptions lineOptions) {

        lineOptions.width(12);
        lineOptions.geodesic(true);

        switch (cCOUNT) {
            case 0:
                lineOptions.color(Color.rgb(255,165,0));    //orange
                break;
            case 1:
                lineOptions.color(Color.rgb(66, 134, 244)); //blue
                break;
            case 2:
                lineOptions.color(Color.rgb(65, 244, 190)); //green
                break;
            case 3:
                lineOptions.color(Color.rgb(244, 79, 65));  //red
                break;
            default:
                lineOptions.color(Color.rgb(97, 65, 244));  //purple
                break;
        }

        return lineOptions;
    }

    /*
    I-02: Following methods implement the PLOT THE LANDMARKS requirement
     */

    public HashMap<String, Landmark> readCsvLandmarks (String filename) {
        HashMap<String, Landmark> landmarks = new HashMap<>();

        try {
            AssetManager am = getAssets();
            InputStream is = am.open(filename);  //error for EHHWv2.csv
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            CSVReader reader = new CSVReader(br);
            reader.readNext();  //skip first line containing headers
            String [] tokens;

            while ( (tokens = reader.readNext()) != null) {
                Landmark landmark = new Landmark(
                        tokens[0],
                        tokens[3],
                        tokens[4],
                        tokens[5],
                        new LatLng(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2])));
                landmarks.put(landmark.getName(), landmark);
            }

        } catch (Exception e) {
            Log.d("CSV Landmark Read", e.toString());
        }

        return landmarks;
    }

    /*
    I-UP: Methods to check user permissions
     */

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     * From ApiDemo https://github.com/googlemaps/android-samples
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Finding your location", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int i=0;i<walkLandmarks.size();i++) {
            Landmark landmark = walkLandmarks.get(i).get(marker.getTitle());
            if (landmark != null) {
                Intent intent = new Intent(this, InfoActivity.class);
                intent.putExtra(InfoActivity.LANDMARK, landmark);
                startActivity(intent);
            }
        }


    }

}
