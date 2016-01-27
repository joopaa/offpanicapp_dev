package com.panic.anhhoang.officerapp.navigationFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.panic.anhhoang.officerapp.R;


public class UsageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nav_usage_fragment, container, false);
        ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.expandableListView);
        elv.setAdapter(new SavedTabsListAdapter());
        return v;
    }

    public class SavedTabsListAdapter extends BaseExpandableListAdapter {


        private String[] groups = {"<b>Registration</b>", "<b>Distress Call Log</b>", "<b>Dry-Run Log</b>", "<b>Alerts</b>"};


        private String[][] children = {
                {"The user is required to fill in as many details as possible, " +
                        "When filling address be specific about floor number, " +
                        "apartment name etc. " +
                        "Specific and accurate information is essential"},
                {"This mode represents real-time distress alerts that will draw require " +
                        "your immediate attention within the shortest possible time-frame " +
                        "When received and if you are in a position to respond please " +
                        "confirm immediately and attend without further delay"},
                {"This mode represents dry-runs that will show dry-run mock requests from " +
                        "people (using the client version) who test the functionality. This " +
                        "will give the client real-time confirmation of how far authorities " +
                        "(Police.Ambulance) are from him / her at a given point in time " +
                        "ALL dry-runs made are recorded with the server"},
                {"This mode represents the alerts received to maintain functionality of app like " +
                        "resetting PIN. A similiar functionality ONE-TIME notification that lets " +
                        "display of alert message outside of PIN verification if the forgot PIN " +
                        "sequence is initiated."}
        };

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return children[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return groups[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return children[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(UsageFragment.this.getActivity());
            //Html.fromhtml allows HTML tags in array <b>
            textView.setText(Html.fromHtml(getGroup(i).toString()));
            textView.setMinimumHeight(150);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            Context context = getActivity().getApplicationContext();
            switch (i) {
                case 0:
                    textView.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.usage_registration_title));
                    break;
                case 1:
                    textView.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.usage_active_mode_title));
                    break;
                case 2:
                    textView.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.usage_dry_run_title));
                    break;
                case 3:
                    textView.setBackgroundColor(ContextCompat.getColor(context,
                            R.color.usage_third_party_title));
                    break;
            }
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(UsageFragment.this.getActivity());
            textView.setPadding(10, 10, 10, 10);
            textView.setText(getChild(i, i1).toString());
            textView.setTextSize(16);
            textView.setTextColor(Color.BLACK);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }
}