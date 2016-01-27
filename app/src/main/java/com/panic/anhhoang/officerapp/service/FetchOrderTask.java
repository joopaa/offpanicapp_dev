package com.panic.anhhoang.officerapp.service;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.panic.anhhoang.officerapp.config.AppConfig;
import com.panic.anhhoang.officerapp.helper.SQLiteHandler;
import com.panic.anhhoang.officerapp.model.OrderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by AnhHoang on 12/10/2015.
 */
public class FetchOrderTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchOrderTask.class.getSimpleName();
    private final Context mContext;

    public FetchOrderTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String orderJsonStr = null;

        try {
            Uri builtUri = Uri.parse(AppConfig.URL_SERVER_GET_REQUEST).buildUpon().build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            orderJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            updateOrderTbl(orderJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private void updateOrderTbl(String orderJsonStr)
            throws JSONException {
        JSONObject data = new JSONObject(orderJsonStr);
        JSONArray orderArray = data.getJSONArray("data");
        OrderModel orderObj = null;
        Vector<ContentValues> cVVector = new Vector<ContentValues>(orderArray.length());
        for (int i = 0; i < orderArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject orderJsonObj = orderArray.getJSONObject(i);
            orderObj = OrderModel.getInstance(orderJsonObj);
            cVVector.add(orderObj.createDBValues());
        }

        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            // Student: call bulkInsert to add the weatherEntries to the database here
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(SQLiteHandler.OrderEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "FetchOrderTask Complete. " + inserted + " Inserted");
    }
}
