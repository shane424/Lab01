package com.mapbox.mapboxsdk.android.testapp;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(20)
public class SearchFragment extends Fragment{

    double lat, lng;
    String address, url, search1, search2, token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        search1 = String.format(getResources().getString(R.string.search_query_1));
        search2 = String.format(getResources().getString(R.string.search_query_2));
        token = String.format(getResources().getString(R.string.testAccessToken));
        return view;
    }

    public void setAddress(String s) {
        address = s;
    }

    private String formatAddress(String a) {
        return a.replace(' ', '+');
    }

    private String BuildSearchString(String a) {
        a = formatAddress(a);
        return search1.concat(a.concat(search2.concat(token)));
    }
}
