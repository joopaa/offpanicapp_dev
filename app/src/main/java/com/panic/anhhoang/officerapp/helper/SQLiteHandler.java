package com.panic.anhhoang.officerapp.helper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.panic.anhhoang.officerapp.model.OfficerModel;

/**
 * Created by AnhHoang on 12/6/2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "panic_officer";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_LOGIN_TABLE = "CREATE TABLE " + OfficerEntry.TABLE_NAME + "("
                + OfficerEntry.KEY_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + OfficerEntry.KEY_NAME + " TEXT," + OfficerEntry.KEY_ROLE + " TEXT,"
                + OfficerEntry.KEY_PHONE + " TEXT UNIQUE," + OfficerEntry.KEY_TOKEN + " TEXT ,"
                + OfficerEntry.KEY_CREATED_AT + " TEXT," + OfficerEntry.KEY_PIN + " TEXT,"
                + OfficerEntry.KEY_TOKEN_HEADER + " TEXT," + OfficerEntry.KEY_LAT + " REAL,"
                + OfficerEntry.KEY_LON + " REAL)";

        final String CREATE_ORDER_TABLE = "CREATE TABLE " + OrderEntry.TABLE_NAME + "("
                + OrderEntry._ID + " INTEGER PRIMARY KEY NOT NULL,"
                + OrderEntry.KEY_CLIENT_ID + " INTEGER," + OrderEntry.KEY_CLIENT_NAME + " TEXT,"
                + OrderEntry.KEY_OFFICER_ID + " INTEGER," + OrderEntry.KEY_OFFICER_NAME + " TEXT,"
                + OrderEntry.KEY_CLIENT_LATITUDE + " REAL ," + OrderEntry.KEY_CLIENT_LONGITUDE + " REAL,"
                + OrderEntry.KEY_OFFICER_LATITUDE + " REAL ," + OrderEntry.KEY_OFFICER_LONGITUDE + " REAL,"
                + OrderEntry.KEY_ADDRESS + " TEXT ," + OrderEntry.KEY_STATUS + " TEXT)";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OfficerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Inserting Row
        long id = db.insert(OfficerEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void updateUser(ContentValues values, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(OfficerEntry.TABLE_NAME, values, " id = ? ", new String[]{id});
        db.close(); // Closing database connection
        Log.d(TAG, "update sqlite: " + id);
    }

    public OfficerModel getUserById(String id) {
        String selectQuery = "SELECT  * FROM " + OfficerEntry.TABLE_NAME + " WHERE id = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});
        OfficerModel model = this.getUserDetail(cursor);
        db.close();
        return model;
    }

    public OfficerModel getUserByPin(String phoneNo, String pin) {
        String selectQuery = "SELECT  * FROM " + OfficerEntry.TABLE_NAME + " WHERE phone = ? and pin = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{phoneNo, pin});
        OfficerModel model = this.getUserDetail(cursor);
        db.close();
        return model;
    }

    /**
     * Getting user data from database
     */
    private OfficerModel getUserDetail(Cursor cursor) {
        // Move to first row
        cursor.moveToFirst();
        OfficerModel user = null;
        if (cursor.getCount() > 0) user = new OfficerModel(cursor);
        cursor.close();
        return user;
    }

    public static final class OfficerEntry {
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_ROLE = "role";
        public static final String KEY_PHONE = "phone";
        public static final String KEY_TOKEN = "token";
        public static final String KEY_CREATED_AT = "created_at";
        public static final String KEY_PIN = "pin";
        public static final String KEY_TOKEN_HEADER = "token_header";
        public static final String KEY_LAT = "lat";
        public static final String KEY_LON = "lon";
        public static final String TABLE_NAME = "user";
    }

    public static final class OrderEntry implements BaseColumns {
        // The "Content authority" is a name for the entire content provider, similar to the
        // relationship between a domain name and its website.  A convenient string to use for the
        // content authority is the package name for the app, which is guaranteed to be unique on the
        // device.
        public static final String CONTENT_AUTHORITY = "com.panic.anhhoang.officerapp";
        // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
        // the content provider.
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_ORDER = "tbl_order";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ORDER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDER;
        public static final String TABLE_NAME = "tbl_order";
        public static final String KEY_CLIENT_ID = "client_id";
        public static final String KEY_CLIENT_NAME = "client_name";
        public static final String KEY_OFFICER_ID = "officer_id";
        public static final String KEY_OFFICER_NAME = "officer_name";
        public static final String KEY_CLIENT_LATITUDE = "client_lat";
        public static final String KEY_CLIENT_LONGITUDE = "client_long";
        public static final String KEY_OFFICER_LATITUDE = "officer_lat";
        public static final String KEY_OFFICER_LONGITUDE = "officer_long";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_STATUS = "status";

        public static Uri buildOrders() {
            return CONTENT_URI;
        }

        public static Uri buildOrderUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
