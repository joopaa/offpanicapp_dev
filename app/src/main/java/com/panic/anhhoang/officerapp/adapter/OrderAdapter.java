package com.panic.anhhoang.officerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.panic.anhhoang.officerapp.AppController;
import com.panic.anhhoang.officerapp.DryRunActiveActivity;
import com.panic.anhhoang.officerapp.R;
import com.panic.anhhoang.officerapp.config.AppConfig;
import com.panic.anhhoang.officerapp.helper.MessageHandle;
import com.panic.anhhoang.officerapp.helper.SQLiteHandler;
import com.panic.anhhoang.officerapp.helper.SessionManager;
import com.panic.anhhoang.officerapp.model.OfficerModel;
import com.panic.anhhoang.officerapp.model.OrderModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnhHoang on 12/10/2015.
 */
public class OrderAdapter extends CursorAdapter {
    private static String LOG_TAG = OrderAdapter.class.getSimpleName();
    private final Context mContext;
    private final SessionManager mSession;
    private SQLiteHandler db;

    public OrderAdapter(Context context, Cursor c, int flags, SessionManager session) {
        super(context, c, flags);
        this.mContext = context;
        this.mSession = session;
        this.db = new SQLiteHandler(mContext);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(final View view, Context context, Cursor cursor) {
        final OrderModel orderModel = new OrderModel(cursor);
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.setData(orderModel);
        holder.iconView.setImageResource(R.mipmap.ic_launcher);
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder(view, orderModel);
            }
        });

        holder.viewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name", orderModel.getClientName());
                editor.putLong("latitude", Double.doubleToRawLongBits(orderModel.getClientLatitude()));
                editor.putLong("longitude", Double.doubleToRawLongBits(orderModel.getClientLongitude()));
                editor.putString("address", orderModel.getAddress());
                editor.commit();
                Intent i = new Intent(mContext, DryRunActiveActivity.class);
                mContext.startActivity(i);
                //openPreferredLocationInMap(orderModel.getClientLatitude(), orderModel.getClientLongitude());
            }
        });
    }

    private void acceptOrder(final View currentView, final OrderModel orderModel) {
        final OfficerModel officerModel = this.db.getUserById(mSession.getUserId());
        final String orderId = String.valueOf(orderModel.getId());
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                AppConfig.URL_SERVER_RESPONSE_HELP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    orderModel.setStatus("Accepted");
                    orderModel.setOfficerLatitude(officerModel.getLat());
                    orderModel.setOfficerLongitude(officerModel.getLon());
                    mContext.getContentResolver().update(SQLiteHandler.OrderEntry.CONTENT_URI,
                            orderModel.createDBValues(), SQLiteHandler.OrderEntry._ID + " = ? ",
                            new String[]{orderId});
                    ViewHolder holder = (ViewHolder) currentView.getTag();
                    holder.viewButton.setVisibility(View.VISIBLE);
                    holder.acceptButton.setVisibility(View.GONE);
                    Toast.makeText(mContext, jObj.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, MessageHandle.getVolleyError(error.networkResponse),
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", orderId);
                params.put("officer_id", String.valueOf(officerModel.getId()));
                params.put("lat", String.valueOf(officerModel.getLat()));
                params.put("lon", String.valueOf(officerModel.getLon()));
                return params;
            }
        };

        String tag_string_req = "str_accept_order";
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*
    private void openPreferredLocationInMap(final double latitude, final double longitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        this.mContext.startActivity(mapIntent);
    } */

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView clientNameView;
        public final TextView addressView;
        public final TextView statusView;
        public final Button acceptButton;
        public final Button viewButton;
        public final Button abandonButton;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            iconView.setImageResource(R.mipmap.ic_launcher);
            clientNameView = (TextView) view.findViewById(R.id.list_item_clientname_textview);
            addressView = (TextView) view.findViewById(R.id.list_item_address_textview);
            statusView = (TextView) view.findViewById(R.id.list_item_status_textview);
            acceptButton = (Button) view.findViewById(R.id.list_item_accept_button);
            viewButton = (Button) view.findViewById(R.id.list_item_view_button);
            abandonButton = (Button) view.findViewById(R.id.list_item_abandon_button);
        }

        public void setData(OrderModel model) {
            clientNameView.setText(model.getClientName());
            addressView.setText(model.getAddress());
            statusView.setText(model.getStatus());
            if (model.getStatus().toLowerCase().contains("awaiting")) {
                acceptButton.setText(model.getStatus());
                acceptButton.setVisibility(View.VISIBLE);
                viewButton.setVisibility(View.GONE);
            } else {
                acceptButton.setVisibility(View.GONE);
                viewButton.setText("View");
                viewButton.setVisibility(View.VISIBLE);
            }
            abandonButton.setText("Abandon");
        }
    }
}
