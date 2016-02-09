package com.mapbox.mapboxsdk.android.testapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class RouteSelectionDialogFragment extends DialogFragment {

    List<Route> routes;
    View.OnClickListener onClickListener;

    // Used to create the pop-up dialogue which allows the user to select a choice
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        routes = getArguments().getParcelableArrayList("routes");

        // Get the layout inflater and create view object
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View routeView = inflater.inflate(R.layout.fragment_routelistview, null);

        AlertDialog routeSelectionDialog = new AlertDialog.Builder(getActivity()).setView(routeView).create();

        ListView routeListView = (ListView) routeView.findViewById(R.id.routeList);

        ArrayAdapter<Route> adapter = new RouteListAdapter();
        routeListView.setAdapter(adapter);

        return routeSelectionDialog;
    }

    private class RouteListAdapter extends ArrayAdapter<Route> {
        public RouteListAdapter(){
            super(getActivity().getApplicationContext(), R.layout.fragment_routelist, routes);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.fragment_routelist, parent, false);
            }

            Route route = routes.get(position);

            ( (TextView) view.findViewById(R.id.routeNumber) ).setText("Route " + route.getRouteNumber());
            ( (TextView) view.findViewById(R.id.routeMiles) ).setText(route.getDistance());

            Button routeButton = (Button) view.findViewById(R.id.routeButton);
            routeButton.setTag(position);
            routeButton.setOnClickListener(onClickListener);

            return view;
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}