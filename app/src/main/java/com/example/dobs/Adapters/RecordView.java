package com.example.dobs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dobs.Classes.BehaviorRecord;
import com.example.dobs.Classes.EventRecord;
import com.example.dobs.Classes.Record;
import com.example.dobs.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RecordView extends BaseAdapter {
    private static final String TAG = "BehaviorViewAdapter";
    public static final int BEHAVIOR_TYPE = 0;
    public static final int INCIDENT_TYPE = 1;

    Context context;
    int behaviorLayoutResourceId;
    int incidentLayoutResourceId;
    List<Record> data = null;

    public RecordView(Context context, int behaviorLayoutResourceId, int incidentLayoutResourceId, List<Record> data) {
        this.behaviorLayoutResourceId = behaviorLayoutResourceId;
        this.incidentLayoutResourceId = incidentLayoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        Record record = data.get(position);
        if (record instanceof BehaviorRecord) {
            return BEHAVIOR_TYPE;
        } else {
            return INCIDENT_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Record record = data.get(position);
        LayoutInflater inflater = null;
        int type = getItemViewType(position);
        if (row == null) {
            if (type == BEHAVIOR_TYPE) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(behaviorLayoutResourceId, null);
            } else {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(incidentLayoutResourceId, null);
            }
        }
        if (type == BEHAVIOR_TYPE) {
            BehaviorRecord behaviorRecord = (BehaviorRecord) record;
            TextView txtBehavior = (TextView) row.findViewById(R.id.labelBehavior);
            TextView txtContext = (TextView) row.findViewById(R.id.labelContext);
            TextView txtBehaviorTime = (TextView) row.findViewById(R.id.labelBehaviorTime);
            txtBehavior.setText(behaviorRecord.getBehavior());
            txtContext.setText(behaviorRecord.getEnvironment());
            Calendar time = GregorianCalendar.getInstance();
            time.setTimeInMillis(behaviorRecord.getTime());
            txtBehaviorTime.setText(String.format("%1$tb %1$td at %1$tI:%1$tM %1$Tp", time));
            row.setBackgroundColor(context.getResources().getColor(behaviorRecord.behavior.color));
            return (row);
        } else {
            EventRecord incidentRecord = (EventRecord) record;
            TextView txtIncident = (TextView) row.findViewById(R.id.labelIncident);
            TextView txtIncidentTime = (TextView) row.findViewById(R.id.labelIncidentTime);
            txtIncident.setText(incidentRecord.getIncident());
            Calendar time = GregorianCalendar.getInstance();
            time.setTimeInMillis(incidentRecord.getTime());
            txtIncidentTime.setText(String.format("%1$tb %1$td at %1$tI:%1$tM %1$Tp", time));
            //row.setBackgroundColor(context.getResources().getColor(R.color.black));
            return (row);
        }
    }
}