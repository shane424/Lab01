package com.mapbox.mapboxsdk.android.testapp;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;

/**
 * Created by Alex on 2/4/2016.
 */
public class CustomMarker extends Marker {

    public CustomMarker(MapView mv, String aTitle, String aDescription, LatLng aLatLng){
        super(mv, aTitle, aDescription, aLatLng);
    }

    @Override
    protected InfoWindow createTooltip(MapView mv) {
        InfoWindow iw = new InfoWindow(R.layout.infowindow_custom, mv);

        return iw;
    }
}