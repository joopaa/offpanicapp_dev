package com.panic.anhhoang.officerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.panic.anhhoang.officerapp.config.AppConfig;
import com.panic.anhhoang.officerapp.helper.MessageHandle;
import com.panic.anhhoang.officerapp.helper.SQLiteHandler;
import com.panic.anhhoang.officerapp.helper.SessionManager;
import com.panic.anhhoang.officerapp.model.OfficerModel;
import com.panic.anhhoang.officerapp.service.QuickstartPreferences;
import com.panic.anhhoang.officerapp.service.RegistrationIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnhHoang on 12/6/2015.
 */
public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputPhoneNo;
    private EditText inputPin;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_login);
        this.db = new SQLiteHandler(getApplicationContext());
        this.session = new SessionManager(getApplicationContext());
        if (this.session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        this.inputPhoneNo = (EditText) findViewById(R.id.phone);
        this.inputPin = (EditText) findViewById(R.id.password);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        this.pDialog = new ProgressDialog(this);
        this.pDialog.setCancelable(false);
        this.btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String phoneNo = inputPhoneNo.getText().toString().trim();
                String pin = inputPin.getText().toString().trim();

                if (!phoneNo.isEmpty() && !pin.isEmpty()) {
                    checkLogin(phoneNo, pin);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
        
        this.btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Intent ac = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(ac);
                    session.setLogin(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Error occured, cannot regist GCM", Toast.LENGTH_LONG);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String phoneNo, final String pin) {
        this.pDialog.setMessage("Logging in ...");
        this.pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SERVER_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                pDialog.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    OfficerModel model = db.getUserByPin(phoneNo, pin);
                    if (model == null) {
                        model = new OfficerModel();
                        model.setId(jObj.getInt("id"));
                        model.setName(jObj.getString("name"));
                        model.setPhone(phoneNo);
                        model.setPin(pin);
                        model.setToken_header(jObj.getString("token"));
                        model.setCreated_at(new Date().toString());
                        model.setRole(jObj.getString("role"));
                        db.addUser(model.createDBValues());
                        session.setUserId(String.valueOf(model.getId()));
                        Intent intent = new Intent(LoginActivity.this, RegistrationIntentService.class);
                        startService(intent);
                    } else {
                        model.setToken_header(jObj.getString("token"));
                        db.updateUser(model.createDBValues(), String.valueOf(model.getId()));
                        session.setLogin(true);
                        session.setUserId(String.valueOf(model.getId()));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), MessageHandle.getVolleyError(error.networkResponse),
                        Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phoneNo);
                params.put("pin", pin);
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Tag used to cancel the request
        String tag_string_req = "req_login";
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
