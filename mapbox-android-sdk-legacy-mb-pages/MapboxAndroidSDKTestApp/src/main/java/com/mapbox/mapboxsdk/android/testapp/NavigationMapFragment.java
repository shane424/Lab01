package com.mapbox.mapboxsdk.android.testapp;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 1/29/2016.
 */

@TargetApi(20)
public class NavigationMapFragment extends Fragment {
    private JSONObject parentObject, locObject;
    private JSONArray parentArray, coordArray, contextArray;
    private LatLng latLng;
    private MapView mapView;
    private String locName, locDescription, addressNo, street, city, state, zip;
    private double lat, lng;
    private Marker navMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigationmap, container, false);
        Bundle b = this.getArguments();

        try {
            parentObject = new JSONObject(b.getString("JSONObject"));
            parentArray = new JSONArray(parentObject.getJSONArray("features"));
            locObject = parentArray.getJSONObject(0);
            coordArray = locObject.getJSONArray("center");

            lng = coordArray.getDouble(0);
            lat = coordArray.getDouble(1);
            latLng = new LatLng(lat, lng);

            contextArray = locObject.getJSONArray("context");

            street = locObject.getString("text");
            addressNo = locObject.getString("address");
            locName = locDescription = city = state = zip = "";

            for (int i = 0; i < contextArray.length(); i++) {
                if (contextArray.getJSONObject(i).getString("id").startsWith("place")) {
                    city = contextArray.getJSONObject(i).getString("text");
                } else if (contextArray.getJSONObject(i).getString("id").startsWith("postcode")) {
                    zip = contextArray.getJSONObject(i).getString("text");
                } else if (contextArray.getJSONObject(i).getString("id").startsWith("region")) {
                    state = contextArray.getJSONObject(i).getString("text");
                }
            }

            locName = addressNo + " " + street;
            locDescription = city + ", " + state + zip;

            mapView = (MapView) view.findViewById(R.id.navMapView);

            navMarker = new Marker(mapView, locName, locDescription, latLng);

            mapView.
                    mapView.setCenter(latLng);
            mapView.setZoom(14);
            mapView.addMarker(navMarker);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
