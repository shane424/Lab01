package com.mapbox.mapboxsdk.android.testapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by smith6s3 on 1/26/2016.
 */

public class NavigationFragment extends Fragment {

    private Button searchButton;
    private EditText inputAddress;
    private TextView testText;
    //private JSONObject addressJson;
    private HttpURLConnection httpURLConnection = null;
    private String addressUrl, search1, search2, token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        inputAddress = (EditText) view.findViewById(R.id.InputAddress);
        testText = (TextView) view.findViewById(R.id.testText);
        search1 = String.format(getResources().getString(R.string.search_query_1));
        search2 = String.format(getResources().getString(R.string.search_query_2));
        token = String.format(getResources().getString(R.string.testAccessToken));

        searchButton = (Button)view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL url = null;
                try {
                    url = new URL(BuildSearchString(inputAddress.getText().toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                        InputStream in = address.openStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                    } finally {
                        httpURLConnection.disconnect();
                    }
                }
            }
        });

        return view;
    }

    private String formatAddress(String a) {
        return a.replace(' ', '+');
    }

    private String BuildSearchString(String a) {
        a = formatAddress(a);
        return search1.concat(a.concat(search2.concat(token.concat(";"))));
    }


    //AlertDialog alertDialog = new AlertDialog.Builder(this).create();


}