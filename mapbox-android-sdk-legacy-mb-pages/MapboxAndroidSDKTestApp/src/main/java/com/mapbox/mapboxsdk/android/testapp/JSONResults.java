package com.mapbox.mapboxsdk.android.testapp;

/**
 * Created by Alex on 1/31/2016.
 */
public class JSONResults {
    String placeName;
    float lat;
    float lng;

    public JSONResults(String placeName, float lat, float lng) {
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

}
