package com.mapbox.mapboxsdk.android.testapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONException;


import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by smith6s3 on 1/26/2016.
 */

public class NavigationFragment extends Fragment {

    private Button searchButton;
    private EditText inputAddress;
    private String addressUrl, search1, search2, token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        inputAddress = (EditText) view.findViewById(R.id.InputAddress);
        search1 = String.format(getResources().getString(R.string.search_query_1));
        search2 = String.format(getResources().getString(R.string.search_query_2));
        token = String.format(getResources().getString(R.string.testAccessToken));

        searchButton = (Button)view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressUrl = BuildSearchString(inputAddress.getText().toString());
                DownloadGeoJsonFile downloadGeoJsonFile = new DownloadGeoJsonFile();
                downloadGeoJsonFile.execute(addressUrl);
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

    private class DownloadGeoJsonFile extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                // Open a stream from the URL
                InputStream stream = new URL(params[0]).openStream();

                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    // Read and save each line of the stream
                    result.append(line);
                }

                // Close the stream
                reader.close();
                stream.close();

                // Convert result to JSONObject
                return new JSONObject(result.toString());
            } catch (IOException e) {
            } catch (JSONException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
            }
        }


    }


}