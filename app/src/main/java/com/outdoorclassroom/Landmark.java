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
    private String info;
    private LatLng latLng;
    private String imgName;

    // default landmark for Manly
    public Landmark () {
        name = "Manly";
        summary = "Default landmark";
        info = "Default information";
        imgName = "";
        latLng = new LatLng(-33.802222, 151.286979);
    }

    // creating landmark
    public Landmark (String n, String s, String i, String im, LatLng l) {
        name = n;
        summary = s;
        info = i;
        imgName = im;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getImgName() {
        return imgName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.summary);
        dest.writeString(this.info);
        dest.writeString(this.imgName);
        dest.writeParcelable(this.latLng, flags);
    }

    protected Landmark(Parcel in) {
        this.name = in.readString();
        this.summary = in.readString();
        this.info = in.readString();
        this.imgName = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Parcelable.Creator<Landmark> CREATOR = new Parcelable.Creator<Landmark>() {
        @Override
        public Landmark createFromParcel(Parcel source) {
            return new Landmark(source);
        }

        @Override
        public Landmark[] newArray(int size) {
            return new Landmark[size];
        }
    };
}
