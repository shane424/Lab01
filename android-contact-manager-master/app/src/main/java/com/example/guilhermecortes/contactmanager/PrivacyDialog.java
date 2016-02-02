package com.example.guilhermecortes.contactmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Ryan Finley on 1/25/2016.
 */
public class PrivacyDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        // make a layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View privacyLayout =  inflater.inflate(R.layout.privacy_setting_layout, null);

        dialog.setView(privacyLayout);

        // setting the check box state
        CheckBox checkBox3 = (CheckBox) privacyLayout.findViewById(R.id.checkBox3);
        SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        Log.d("INFO", Boolean.toString(sharedPrefs.getBoolean(Integer.toString(R.id.checkBox3), false)));

        checkBox3.setChecked(sharedPrefs.getBoolean(Integer.toString(R.id.checkBox3), false));

        return dialog.create();
    }
}
