package com.outdoorclassroom;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //2-dimensional array, containing array of walks
    ArrayList markerPoints = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        parseCoord();

        // function for onMapPoints
    }

    private void parseCoord () {
        LatLng eEsplanade = new LatLng(-33.802222, 151.286979);
        LatLng sSteyne = new LatLng(-33.797286, 151.288127);
        onMapPoints(eEsplanade, sSteyne);
    }

    public void onMapPoints (LatLng start, LatLng end) {

        //get initial size of markerPoints array
        int pointArrSize = markerPoints.size();

        //limiter to how many walks are on the map
        if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }

        //create array for walk (start, end) and add them
        ArrayList <LatLng> walk = new ArrayList<LatLng>();
        walk.add(start);
        walk.add(end);

        //add walk to markerPoints array
        markerPoints.add(walk);

        //create MarkerOptions for start and end
        MarkerOptions startOpt = new MarkerOptions();
        MarkerOptions endOpt = new MarkerOptions();

        startOpt.position(start);
        endOpt.position(end);

        startOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        endOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        mMap.addMarker(startOpt);
        mMap.addMarker(endOpt);

        // check that new points were captured
        /**
         * if (markerPoints.size() >= pointArrSize+2) { }
         */
    }

}
