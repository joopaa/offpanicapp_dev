package com.panic.anhhoang.officerapp.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.panic.anhhoang.officerapp.helper.SQLiteHandler;

/**
 * Created by AnhHoang on 12/6/2015.
 */
public class OfficerModel {
    private int id;
    private String name;
    private String role;
    private String phone;
    private String token;
    private String created_at;
    private String pin;
    private String token_header;
    private double lat;
    private double lon;

    public OfficerModel() {
    }

    public OfficerModel(Cursor cursor) {
        this.setId(cursor.getInt(0));
        this.setName(cursor.getString(1));
        this.setRole(cursor.getString(2));
        this.setPhone(cursor.getString(3));
        this.setToken(cursor.getString(4));
        this.setCreated_at(cursor.getString(5));
        this.setPin(cursor.getString(6));
        this.setToken_header(cursor.getString(7));
        this.setLat(cursor.getDouble(8));
        this.setLon(cursor.getDouble(9));
    }

    public ContentValues createDBValues() {
        ContentValues values = new ContentValues();
        values.put(SQLiteHandler.OfficerEntry.KEY_ID, this.getId());
        values.put(SQLiteHandler.OfficerEntry.KEY_NAME, this.getName()); // Name
        values.put(SQLiteHandler.OfficerEntry.KEY_ROLE, this.getRole()); // Name
        values.put(SQLiteHandler.OfficerEntry.KEY_PHONE, this.getPhone()); // Phone number
        values.put(SQLiteHandler.OfficerEntry.KEY_TOKEN, this.getToken()); // token
        values.put(SQLiteHandler.OfficerEntry.KEY_CREATED_AT, this.getCreated_at()); // Created At
        values.put(SQLiteHandler.OfficerEntry.KEY_PIN, this.getPin());
        values.put(SQLiteHandler.OfficerEntry.KEY_TOKEN_HEADER, this.getToken_header());
        values.put(SQLiteHandler.OfficerEntry.KEY_LAT, this.getLat());
        values.put(SQLiteHandler.OfficerEntry.KEY_LON, this.getLon());
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getToken_header() {
        return token_header;
    }

    public void setToken_header(String token_header) {
        this.token_header = token_header;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
