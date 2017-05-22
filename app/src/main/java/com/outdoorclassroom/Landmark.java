package com.outdoorclassroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Allan on 15-May-17.
 */

public class Landmark implements Parcelable {
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

    //Parcelling implementation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
