package com.mapbox.mapboxsdk.android.testapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;

import com.cocoahero.android.geojson.GeoJSONObject;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Text;


import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by smith6s3 on 1/26/2016.
 */

@TargetApi(20)
public class NavigationFragment extends Fragment {

    private Button searchButton;
    private EditText inputAddress;
    private String addressUrl, search1, search2, token;
    private TextView test;

    private onSearchListener mCallBack;

    public interface onSearchListener {
        public void onLocationFound (String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (onSearchListener) activity;
        } catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        inputAddress = (EditText) view.findViewById(R.id.InputAddress);
        search1 = getResources().getString(R.string.search_query_1);
        search2 = getResources().getString(R.string.search_query_2);
        token = getResources().getString(R.string.AccessToken);

        searchButton = (Button)view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressUrl = BuildSearchString(inputAddress.getText().toString());
                DownloadGeoJsonFile downloadGeoJsonFile = new DownloadGeoJsonFile();
                downloadGeoJsonFile.execute(addressUrl);
            }
        });

        test = (TextView) view.findViewById(R.id.textView);

        return view;
    }

    private String formatAddress(String a) {
        return a.replace(' ', '+');
    }

    private String BuildSearchString(String a) {
        a = formatAddress(a);
        return search1.concat(a.concat(search2.concat(token)));
    }


    //AlertDialog alertDialog = new AlertDialog.Builder(this).create();

    private class DownloadGeoJsonFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                // Open a stream from the URL
                InputStream stream = new URL(params[0]).openStream();

                String line;
                StringBuffer result = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                stream.close();

                return result.toString();
            } catch (IOException e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            test.setText(s);
            mCallBack.onLocationFound(s);
        }


    }

}