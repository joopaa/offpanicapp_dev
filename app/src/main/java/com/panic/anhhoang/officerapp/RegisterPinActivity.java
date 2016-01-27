package com.panic.anhhoang.officerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.panic.anhhoang.officerapp.config.AppConfig;
import com.panic.anhhoang.officerapp.helper.MessageHandle;
import com.panic.anhhoang.officerapp.helper.SQLiteHandler;
import com.panic.anhhoang.officerapp.helper.SessionManager;
import com.panic.anhhoang.officerapp.model.OfficerModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnhHoang on 12/7/2015.
 */
public class RegisterPinActivity extends Activity {
    private static final String TAG = RegisterPinActivity.class.getSimpleName();
    private Button btnRegister;
    private EditText inputPin;
    private EditText inputPinConfirm;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officer_register_pin);
        this.session = new SessionManager(getApplicationContext());
        this.db = new SQLiteHandler(getApplicationContext());

        if (this.session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterPinActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        this.inputPin = (EditText) findViewById(R.id.pin);
        this.inputPinConfirm = (EditText) findViewById(R.id.pinConfirm);
        this.btnRegister = (Button) findViewById(R.id.btnRegisterPin);
        this.pDialog = new ProgressDialog(this);
        this.pDialog.setCancelable(false);

        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String pin = inputPin.getText().toString().trim();
                String confirmPin = inputPinConfirm.getText().toString().trim();

                if (!pin.isEmpty() && !confirmPin.isEmpty() && pin.equals(confirmPin)) {
                    // Tag used to cancel the request
                    String tag_string_req = "req_register_pin";
                    pDialog.setMessage("Registering ...");
                    pDialog.show();

                    final OfficerModel model = db.getUserById(session.getUserId());
                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            AppConfig.URL_SERVER_REGISTER_PIN, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Register Response: " + response.toString());
                            pDialog.dismiss();
                            model.setPin(pin);
                            db.updateUser(model.createDBValues(), String.valueOf(model.getId()));
                            Intent intent = new Intent(RegisterPinActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
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
                            // Posting params to register url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_id", String.valueOf(model.getId()));
                            params.put("pin", pin);
                            return params;
                        }
                    };

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Pin does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
