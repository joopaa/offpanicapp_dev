package com.panic.anhhoang.officerapp.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.panic.anhhoang.officerapp.LoginActivity;

/**
 * Created by AnhHoang on 12/6/2015.
 */
public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "PanicOfficerLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String USER_ID = "userId";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    public void logoutUser(Context context) {
        this.setLogin(false);
        this.setUserId("");

        // Launching the login activity
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

}
