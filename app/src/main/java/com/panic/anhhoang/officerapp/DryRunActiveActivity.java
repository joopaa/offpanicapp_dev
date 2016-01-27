package com.panic.anhhoang.officerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.strongloop.android.loopback.RestAdapter;


/**
 * Created by AnhHT on 1/18/2016.
 */
public class DryRunActiveActivity extends Activity {

    protected static final String TAG = DryRunActiveActivity.class.getSimpleName();
    private RestAdapter restAdapter;
    private GoogleMap map;

    TextView userNameView;
    TextView userLocation;
    Button btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_dry_run_active);

        //Maps
        userNameView = (TextView) findViewById(R.id.NameText);
        userLocation = (TextView) this.findViewById(R.id.LocationText);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String data_name = prefs.getString("name",
                "default_value_here_if_string_is_missing");
        final Long data_longitude= prefs.getLong("longitude",
                0);
        final Long data_latitude= prefs.getLong("latitude",
                0);
        String data_address= prefs.getString("address",
                "default_value_here_if_string_is_missing");

        LatLng LOCATION = new LatLng(Double.longBitsToDouble(data_latitude), Double.longBitsToDouble(data_longitude));
        userNameView.setText("Full Name: " + data_name);
        userLocation.setText("User location is at lat: " + Double.longBitsToDouble(data_latitude) + " long: " + Double.longBitsToDouble(data_longitude));
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.frag_map)).getMap();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, 14));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(Double.longBitsToDouble(data_latitude), Double.longBitsToDouble(data_longitude)))
                .title(data_address));
        btnGoogle = (Button) findViewById(R.id.btn_googlemaps);
        btnGoogle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.longBitsToDouble(data_latitude) + "," + Double.longBitsToDouble(data_longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
            });
    }


}
