package com.panic.anhhoang.officerapp.navigationFragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.panic.anhhoang.officerapp.R;
import com.panic.anhhoang.officerapp.adapter.OrderAdapter;
import com.panic.anhhoang.officerapp.helper.SQLiteHandler;
import com.panic.anhhoang.officerapp.helper.SessionManager;
import com.panic.anhhoang.officerapp.service.FetchOrderTask;

public class OrderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ORDER_LOADER_ID = 0;
    private static final String[] ORDER_COLUMNS = {
            SQLiteHandler.OrderEntry._ID,
            SQLiteHandler.OrderEntry.KEY_CLIENT_ID,
            SQLiteHandler.OrderEntry.KEY_CLIENT_NAME,
            SQLiteHandler.OrderEntry.KEY_OFFICER_ID,
            SQLiteHandler.OrderEntry.KEY_OFFICER_NAME,
            SQLiteHandler.OrderEntry.KEY_CLIENT_LATITUDE,
            SQLiteHandler.OrderEntry.KEY_CLIENT_LONGITUDE,
            SQLiteHandler.OrderEntry.KEY_OFFICER_LATITUDE,
            SQLiteHandler.OrderEntry.KEY_OFFICER_LONGITUDE,
            SQLiteHandler.OrderEntry.KEY_ADDRESS,
            SQLiteHandler.OrderEntry.KEY_STATUS
    };
    private final String LOG_TAG = OrderFragment.class.getSimpleName();
    private OrderAdapter orderAdapter;
    private SessionManager session;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.session = new SessionManager(getContext().getApplicationContext());
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.orderAdapter = new OrderAdapter(getActivity(), null, 0, session);
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_order);
        listView.setAdapter(this.orderAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ORDER_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getOrders();
    }

    @Override
    public void onResume() {
        super.onStart();
        getOrders();
    }


    private void getOrders() {
        FetchOrderTask ot = new FetchOrderTask(getActivity());
        ot.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = SQLiteHandler.OrderEntry.KEY_CLIENT_NAME + " ASC";
        Uri uri = SQLiteHandler.OrderEntry.buildOrders();
        return new CursorLoader(getActivity(), uri, ORDER_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.orderAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.orderAdapter.swapCursor(null);
    }
}
