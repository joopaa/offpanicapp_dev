package com.panic.anhhoang.officerapp.helper;

import com.android.volley.NetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AnhHoang on 12/9/2015.
 */
public class MessageHandle {
    public static String getVolleyError(NetworkResponse networkResponse) {
        String temp = new String(networkResponse.data);
        return trimMessage(temp, "error");
    }

    private static String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }
}
