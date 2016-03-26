package com.example.dobs.Adapters;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dobs.Classes.BehaviorRecord;
import com.example.dobs.Classes.Record;
import com.example.dobs.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RecordView extends ArrayAdapter<Record> {
    private static final String TAG = "BehaviorViewAdapter";

    Context context;
    int layoutResourceId;
    List<Record> data = null;

    public RecordView(Context context, int layoutResourceId, List<Record> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BehaviorHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((ListActivity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new BehaviorHolder();
            holder.txtBehavior = (TextView) row.findViewById(R.id.labelBehavior);
            holder.txtContext = (TextView) row.findViewById(R.id.labelContext);
            holder.txtTime = (TextView) row.findViewById(R.id.labelBehaviorTime);
            row.setTag(holder);
        } else {
            holder = (BehaviorHolder) row.getTag();
        }

        BehaviorRecord recordBehavior = (BehaviorRecord) data.get(position);
        holder.txtBehavior.setText(recordBehavior.getBehavior());
        holder.txtContext.setText(recordBehavior.getEnvironment());

        Calendar time = GregorianCalendar.getInstance();
        time.setTimeInMillis(recordBehavior.getTime());
        holder.txtTime.setText(String.format("%1$tb %1$td at %1$tI:%1$tM %1$Tp", time));

        row.setBackgroundColor(getContext().getResources().getColor(recordBehavior.behavior.color));

        return (row);
    }

    static class BehaviorHolder {
        TextView txtBehavior;
        TextView txtContext;
        TextView txtTime;
    }
}
