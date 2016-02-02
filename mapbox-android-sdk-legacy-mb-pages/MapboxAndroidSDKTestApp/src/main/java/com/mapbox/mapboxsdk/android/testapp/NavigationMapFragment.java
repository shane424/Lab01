package com.mapbox.mapboxsdk.android.testapp;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
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
    private String locName, locDescription, addressNo, street, city, state, zip, latString, lngString;
    private double lat, lng;
    private Marker navMarker;
   // private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigationmap, container, false);
        Bundle b = this.getArguments();

        //textView = (TextView)view.findViewById(R.id.textView2);
        try {

            //textView.setText("Test 1");
            //textView.setText(b.getString("JSONObject"));
            parentObject = new JSONObject(b.getString("JSONObject"));
            parentArray = parentObject.getJSONArray("features");
            locObject = parentArray.getJSONObject(0);
            coordArray = locObject.getJSONArray("center");

            lat = coordArray.getDouble(0);
            lng = coordArray.getDouble(1);
            latLng = new LatLng(lat, lng);

            //textView.setText("Test 2");
            contextArray = locObject.getJSONArray("context");

            street = locObject.getString("text");
            addressNo = locObject.getString("address");
            locName = locDescription = city = state = zip = "";

            //textView.setText("Test 3");
            for (int i = 0; i < contextArray.length(); i++) {
                if (contextArray.getJSONObject(i).getString("id").startsWith("place")) {
                    //textView.setText("Test 4" + String.valueOf(i));
                    city = contextArray.getJSONObject(i).getString("text");
                } else if (contextArray.getJSONObject(i).getString("id").startsWith("postcode")) {
                    zip = contextArray.getJSONObject(i).getString("text");
                } else if (contextArray.getJSONObject(i).getString("id").startsWith("region")) {
                    state = contextArray.getJSONObject(i).getString("text");
                }
            }

            //textView.setText("Test 5");

            locName = addressNo + " " + street;
            locDescription = city + ", " + state + " " + zip;
            latString = String.valueOf(lat);
            lngString = String.valueOf(lng);

            //textView.setText("Test 6");
            //textView.setText(locName + "\n" + locDescription + "\n" + latString + ", " + lngString);


            mapView = (MapView) view.findViewById(R.id.navMapView);

            navMarker = new Marker(mapView, locName, locDescription, latLng);
            navMarker.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "marker-stroked", "FF0000"));

            mapView.setCenter(latLng);
            mapView.setZoom(14);
            mapView.addMarker(navMarker);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
