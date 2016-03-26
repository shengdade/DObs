package com.example.dobs.Adapters;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dobs.Classes.EventRecord;
import com.example.dobs.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class IncidentView extends ArrayAdapter<EventRecord> {
    private static final String TAG = "IncidentViewAdapter";

    Context context;
    int layoutResourceId;
    List<EventRecord> data = null;

    public IncidentView(Context context, int layoutResourceId, List<EventRecord> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((ListActivity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();
            holder.txtIncident = (TextView) row.findViewById(R.id.labelIncident);
            holder.txtIncidentTime = (TextView) row.findViewById(R.id.labelIncidentTime);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        EventRecord record = data.get(position);
        holder.txtIncident.setText(record.getIncident());
        Calendar time = GregorianCalendar.getInstance();
        time.setTimeInMillis(record.getTime());
        holder.txtIncidentTime.setText(String.format("%1$tb %1$td at %1$tI:%1$tM %1$Tp", time));
        //row.setBackgroundColor(context.getResources().getColor(R.color.black));

        return (row);
    }

    static class Holder {
        TextView txtIncident;
        TextView txtIncidentTime;
    }
}