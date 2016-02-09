package com.mapbox.mapboxsdk.android.testapp;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.android.testapp.gson.Route;
import com.mapbox.mapboxsdk.android.testapp.gson.Routes;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.PathOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.ITileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

@TargetApi(20)
public class NavigationMapFragment extends Fragment {
    private JSONObject parentObject, locObject;
    private JSONArray parentArray, coordArray, contextArray;
    private LatLng latLng;
    private MapView mapView;
    private String locName, locDescription, addressNo, street, city, state, zip, latString, lngString;
    private double lat, lng;
    private Marker navMarker;
    private String streetMap = "OpenStreetMap";
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

            lng = coordArray.getDouble(0);
            lat = coordArray.getDouble(1);
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
            //replaceMapView(streetMap);
            navMarker = new Marker(mapView, locName, locDescription, latLng);
            navMarker.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "marker-stroked", "FF0000"));
            navMarker.setSubDescription("<a href=\"\">Navigate Here...</a>");
            replaceMapView(streetMap);
            mapView.addMarker(navMarker, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GetDirections().execute(latLng.getLongitude() + "," + latLng.getLatitude(), "");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }


    protected void replaceMapView(String layer) {
        ITileLayer source;
        BoundingBox box;
        if (layer.equalsIgnoreCase("OpenStreetMap")) {
            source = new WebSourceTileLayer("openstreetmap",
                    "http://tile.openstreetmap.org/{z}/{x}/{y}.png").setName("OpenStreetMap")
                    .setAttribution("© OpenStreetMap Contributors")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else if (layer.equalsIgnoreCase("OpenSeaMap")) {
            source = new WebSourceTileLayer("openstreetmap",
                    "http://tile.openstreetmap.org/seamark/{z}/{x}/{y}.png").setName(
                    "OpenStreetMap")
                    .setAttribution("© OpenStreetMap Contributors")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else if (layer.equalsIgnoreCase("mapquest")) {
            source = new WebSourceTileLayer("mapquest",
                    "http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png").setName(
                    "MapQuest Open Aerial")
                    .setAttribution(
                            "Tiles courtesy of MapQuest and OpenStreetMap contributors.")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else {
            source = new MapboxTileLayer(layer);
        }

        mapView.setTileSource(source);
        box = source.getBoundingBox();
        mapView.setScrollableAreaLimit(box);
        mapView.setMinZoomLevel(mapView.getTileProvider().getMinimumZoomLevel());
        mapView.setMaxZoomLevel(mapView.getTileProvider().getMaximumZoomLevel());
        mapView.setCenter(latLng);
        mapView.setZoom(10);

        Log.d("MainActivity", "zoomToBoundingBox " + box.toString());
    }

    class GetDirections extends AsyncTask<String, Void, Routes> {

        @Override
        protected Routes doInBackground(String... params) {
            String address1 = params[0];
            String address2 = params[1];

            try {
                // Open a stream from the URL
                InputStream stream = new URL("https://api.mapbox.com/v4/directions/mapbox.driving/-81.38,40.72;" + address1 + ".json?alternatives=false&instructions=false&geometry=LineString&steps=false&&access_token=pk.eyJ1IjoiZmlubGV5cmEiLCJhIjoiY2lrZXBhYW56MDA2N3VybTI4ZGhvdzdkbiJ9.Yfum8lcKfweuXBm5m5M3cA").openStream();

                String line;
                StringBuffer result = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                stream.close();
                return new Gson().fromJson(result.toString(), Routes.class);
            } catch (IOException e) {
            }

            return null;
        }


        @Override
        protected void onPostExecute(final Routes routes) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("routes", createRoutes(routes));

            // deciding whether to use the feature 5 dialog or not
            switch (routes.routes.size()) {
                case 1:
                    plotDirectionLine(routes.routes.get(0).geometry.coordinates);
                    break;
                default:
                    final RouteSelectionDialogFragment routeFrag = new RouteSelectionDialogFragment();
                    routeFrag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedRoute(v, routes);
                            routeFrag.dismiss();
                        }
                    });
                    routeFrag.setArguments(bundle);
                    routeFrag.show(getActivity().getFragmentManager(), "routeSelection");
                    break;
            }
        }

        // plotting all the lines by using one point and then connecting it to another point
        public void plotDirectionLine(ArrayList<ArrayList<Float>> coordinates) {
            PathOverlay line = new PathOverlay();
            for (int i = 0; i < coordinates.size() - 1; i++) {
                line.addPoints(new LatLng(coordinates.get(i).get(1), coordinates.get(i).get(0)), new LatLng(coordinates.get(i + 1).get(1), coordinates.get(i + 1).get(0)));
            }
            mapView.getOverlays().add(line);
            LatLng startingPoint = new LatLng(40.72, -81.38);
            Marker start = new Marker("Your Location", "", startingPoint);
            start.setIcon(new Icon(getActivity(), Icon.Size.SMALL, "marker-stroked", "FF0000"));
            mapView.addMarker(start);
            mapView.closeCurrentTooltip();
        }

        // handling the correct selection of the route
        public void selectedRoute(View view, Routes routes) {
            plotDirectionLine(routes.routes.get((Integer) view.getTag()).geometry.coordinates);
        }

        // plotting of lines to make a "Roadmap"
        public ArrayList<com.mapbox.mapboxsdk.android.testapp.Route> createRoutes(Routes routes) {
            ArrayList<com.mapbox.mapboxsdk.android.testapp.Route> routeArrayList = new ArrayList<>();
            int pos = 0;
            for (Route route : routes.routes) {
                pos++;
                routeArrayList.add(new com.mapbox.mapboxsdk.android.testapp.Route(pos, String.valueOf(new Float(route.distance) / new Float(5280)) + " mi"));
            }

            return routeArrayList;
        }
    }
}
