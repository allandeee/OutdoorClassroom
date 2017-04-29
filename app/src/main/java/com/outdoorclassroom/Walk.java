package com.outdoorclassroom;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


class Walk {
    private LatLng start;
    private LatLng end;
    private ArrayList<LatLng> wpts;

    Walk () {
        start = new LatLng(-33.852,151.211);
        end = new LatLng(-33.852,151.211);
        wpts = new ArrayList();
    }

    Walk (LatLng st, LatLng en) {
        start = st;
        end = en;
        wpts = new ArrayList();
    }

    Walk (LatLng st, LatLng en, ArrayList wp) {
        start = st;
        end = en;
        wpts = new ArrayList(wp);
    }

    void addWpt (LatLng latLng) {
        wpts.add(latLng);
    }

    LatLng getStart () {
        return start;
    }

    LatLng getEnd() {
        return end;
    }

    LatLng getWpt (int i) {
        return wpts.get(i);
    }

    void setStart (LatLng start) {
        this.start = start;
    }

    void setEnd(LatLng end) {
        this.end = end;
    }

    void removeLastWpt () {
        wpts.remove(wpts.size()-1);
    }

    boolean wptsIsEmpty() {
        return wpts.size() <= 0;
    }

    int wptsSize() {
        return wpts.size();
    }
}
