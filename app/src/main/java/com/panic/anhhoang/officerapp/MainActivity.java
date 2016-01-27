package com.panic.anhhoang.officerapp;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.panic.anhhoang.officerapp.custombasket.CustomBasketFragment;
import com.panic.anhhoang.officerapp.helper.SQLiteHandler;
import com.panic.anhhoang.officerapp.helper.SessionManager;
import com.panic.anhhoang.officerapp.model.OfficerModel;
import com.panic.anhhoang.officerapp.navigationFragment.OrderFragment;
import com.panic.anhhoang.officerapp.navigationFragment.UsageFragment;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener{
    protected static final String TAG = MainActivity.class.getSimpleName();
    private SessionManager session;
    private GoogleApiClient mGoogleApiClient;
    private SQLiteHandler db;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);

        //Layouts
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);

        this.db = new SQLiteHandler(this);
        this.session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            session.logoutUser(this);
            finish();
        } else {
            //TODO for test only
            if(db.getUserById(session.getUserId()) == null) {
                session.logoutUser(this);
                finish();
            }
        }

        buildGoogleApiClient();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_navigation_main_drawer, menu);
        return true;
    }
    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            OfficerModel model = db.getUserById(session.getUserId());
            model.setLat(mLastLocation.getLatitude());
            model.setLon(mLastLocation.getLongitude());
            db.updateUser(model.createDBValues(), String.valueOf(model.getId()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);

        FragmentTransaction transaction;

        switch (item.getItemId()) {
            case R.id.nav_home:
                //this.setActionBarTitle(item.getTitle());
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, new OrderFragment());
                transaction.commit();
                return true;
            case R.id.nav_usage:
                //this.setActionBarTitle(item.getTitle());
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, new UsageFragment());
                transaction.commit();
                return true;
            case R.id.nav_view_profile:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, new CustomBasketFragment());
                transaction.commit();
                return true;
            case R.id.action_logout:
                session.logoutUser(this);
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }
}
