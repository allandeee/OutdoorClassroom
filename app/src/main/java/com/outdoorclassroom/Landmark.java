package com.outdoorclassroom;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Allan on 15-May-17.
 */

public class Landmark {
    private String name;
    private String summary;
    private LatLng latLng;

    // default landmark for Manly
    public Landmark () {
        name = "Manly";
        summary = "Default landmark";
        latLng = new LatLng(-33.802222, 151.286979);
    }

    // creating landmark
    public Landmark (String n, String s, LatLng l) {
        name = n;
        summary = s;
        latLng = l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
