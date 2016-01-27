package com.panic.anhhoang.officerapp.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.panic.anhhoang.officerapp.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AnhHoang on 12/5/2015.
 */
public class OrderModel {
    static final String O_ID = "id";
    static final String O_CLIENT_ID = "client_id";
    static final String O_CLIENT_NAME = "client_name";
    static final String O_OFFICER_ID = "officer_id";
    static final String O_OFFICER_NAME = "officer_name";
    static final String O_CLIENT_LATITUDE = "client_lat";
    static final String O_CLIENT_LONGITUDE = "client_lon";
    static final String O_OFFICER_LATITUDE = "officer_lat";
    static final String O_OFFICER_LONGITUDE = "officer_lon";
    static final String O_ADDRESS = "client_address";
    static final String O_STATUS = "status";
    private int id;
    private int clientId;
    private int officerId;
    private String clientName;
    private String officerName;
    private String address;
    private double clientLatitude;
    private double clientLongitude;
    private double officerLatitude;
    private double officerLongitude;
    private String status;

    public OrderModel() {
    }

    public OrderModel(Cursor cursor) {
        this.setId(cursor.getInt(0));
        this.setClientId(cursor.getInt(1));
        this.setClientName(cursor.getString(2));
        this.setOfficerId(cursor.getInt(3));
        this.setOfficerName(cursor.getString(4));
        this.setClientLatitude(cursor.getDouble(5));
        this.setClientLongitude(cursor.getDouble(6));
        this.setOfficerLatitude(cursor.getDouble(7));
        this.setOfficerLongitude(cursor.getDouble(8));
        this.setAddress(cursor.getString(9));
        this.setStatus(cursor.getString(10));
    }

    public static OrderModel getInstance(JSONObject jsonObject) throws JSONException {
        OrderModel o = new OrderModel();
        o.setId(jsonObject.getInt(O_ID));
        o.setClientId(jsonObject.getInt(O_CLIENT_ID));
        o.setClientName(jsonObject.getString(O_CLIENT_NAME));
        o.setClientLatitude(jsonObject.getDouble(O_CLIENT_LATITUDE));
        o.setClientLongitude(jsonObject.getDouble(O_CLIENT_LONGITUDE));
        Object temp = jsonObject.get(O_OFFICER_ID);
        if (temp != null && !temp.toString().toLowerCase().equals("null")) {
            o.setOfficerId(jsonObject.getInt(O_OFFICER_ID));
        }

        temp = jsonObject.get(O_OFFICER_NAME);
        if (temp != null && !temp.toString().toLowerCase().equals("null")) {
            o.setOfficerName(jsonObject.getString(O_OFFICER_NAME));
        }

        temp = jsonObject.get(O_OFFICER_LATITUDE);
        if (temp != null && !temp.toString().toLowerCase().equals("null")) {
            o.setOfficerLatitude(jsonObject.getDouble(O_OFFICER_LATITUDE));
        }

        temp = jsonObject.get(O_OFFICER_LONGITUDE);
        if (temp != null && !temp.toString().toLowerCase().equals("null")) {
            o.setOfficerLongitude(jsonObject.getDouble(O_OFFICER_LONGITUDE));
        }

        o.setAddress(jsonObject.getString(O_ADDRESS));
        o.setStatus(jsonObject.getString(O_STATUS));
        return o;
    }

    public ContentValues createDBValues() {
        ContentValues values = new ContentValues();
        values.put(SQLiteHandler.OrderEntry._ID, this.getId());
        values.put(SQLiteHandler.OrderEntry.KEY_CLIENT_ID, this.getClientId());
        values.put(SQLiteHandler.OrderEntry.KEY_CLIENT_NAME, this.getClientName());
        values.put(SQLiteHandler.OrderEntry.KEY_OFFICER_ID, this.getOfficerId());
        values.put(SQLiteHandler.OrderEntry.KEY_OFFICER_NAME, this.getOfficerName());
        values.put(SQLiteHandler.OrderEntry.KEY_CLIENT_LATITUDE, this.getClientLatitude());
        values.put(SQLiteHandler.OrderEntry.KEY_CLIENT_LONGITUDE, this.getClientLongitude());
        values.put(SQLiteHandler.OrderEntry.KEY_OFFICER_LATITUDE, this.getOfficerLatitude());
        values.put(SQLiteHandler.OrderEntry.KEY_OFFICER_LONGITUDE, this.getOfficerLongitude());
        values.put(SQLiteHandler.OrderEntry.KEY_ADDRESS, this.getAddress());
        values.put(SQLiteHandler.OrderEntry.KEY_STATUS, this.getStatus());
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getClientLatitude() {
        return clientLatitude;
    }

    public void setClientLatitude(double clientLatitude) {
        this.clientLatitude = clientLatitude;
    }

    public double getClientLongitude() {
        return clientLongitude;
    }

    public void setClientLongitude(double clientLongitude) {
        this.clientLongitude = clientLongitude;
    }

    public double getOfficerLatitude() {
        return officerLatitude;
    }

    public void setOfficerLatitude(double officerLatitude) {
        this.officerLatitude = officerLatitude;
    }

    public double getOfficerLongitude() {
        return officerLongitude;
    }

    public void setOfficerLongitude(double officerLongitude) {
        this.officerLongitude = officerLongitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
