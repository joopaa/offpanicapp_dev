package com.panic.anhhoang.officerapp.custombasket;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.panic.anhhoang.officerapp.R;

public class CustomBasketFragment extends Fragment {

    //list apps
    private Button appList;
    private Button panicapp;
    //panic app
    //custom apps
    public static ImageButton appButton1;
    public static ImageButton appButton2;
    public static ImageButton appButton3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_basket, container, false);


        appList = (Button) v.findViewById(R.id.apps_button);
        appList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), AppsListActivity.class);
                startActivity(i);
            }
        });
        panicapp = (Button) v.findViewById(R.id.panic_button);
        panicapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.panic.quandh.panicapp");
                startActivity(i);
            }
        });
        appButton1 = (ImageButton) v.findViewById(R.id.imageButton);
        appButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String data = prefs.getString("App1",
                        "default_value_here_if_string_is_missing");
                Intent i;
                i = getActivity().getPackageManager().getLaunchIntentForPackage(data);
                startActivity(i);
            }
        });
        appButton2 = (ImageButton) v.findViewById(R.id.imageButton2);
        appButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String data = prefs.getString("App2",
                        "default_value_here_if_string_is_missing");
                Intent i;
                i = getActivity().getPackageManager().getLaunchIntentForPackage(data);
                startActivity(i);
            }
        });
        appButton3 = (ImageButton) v.findViewById(R.id.imageButton3);
        appButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String data = prefs.getString("App3",
                        "default_value_here_if_string_is_missing");
                Intent i;
                i = getActivity().getPackageManager().getLaunchIntentForPackage(data);
                startActivity(i);
            }
        });
        return v;
    }

}
