package com.outdoorclassroom;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Walk {
    private LatLng start;
    private LatLng end;
    private ArrayList<LatLng> wpts;

    public Walk () {
        start = new LatLng(-33.852,151.211);
        end = new LatLng(-33.852,151.211);
        wpts = new ArrayList();
    }

    public Walk (LatLng st, LatLng en) {
        start = st;
        end = en;
        wpts = new ArrayList();
    }

    public Walk (LatLng st, LatLng en, ArrayList wp) {
        start = st;
        end = en;
        wpts = new ArrayList(wp);
    }

    public void addWpt (LatLng latLng) {
        wpts.add(latLng);
    }

    public LatLng getStart () {
        return start;
    }

    public LatLng getEnd() {
        return end;
    }

    public LatLng getWpt (int i) {
        return wpts.get(i);
    }

    public void setStart (LatLng start) {
        this.start = start;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public void removeLastWpt () {
        wpts.remove(wpts.size()-1);
    }

    public boolean wptsIsEmpty() {
        return wpts.size() <= 0;
    }

    public int wptsSize() {
        return wpts.size();
    }
}
