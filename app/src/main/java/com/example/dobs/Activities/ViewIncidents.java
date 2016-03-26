package com.example.dobs.Activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.dobs.Adapters.IncidentView;
import com.example.dobs.Classes.EventRecord;
import com.example.dobs.Classes.TimeComparator;
import com.example.dobs.R;

import java.util.Collections;
import java.util.List;

public class ViewIncidents extends ListActivity {
    private static final String TAG = "ViewIncidents";
    List<EventRecord> incidents;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetRecordsTask().execute();//Go fetch records
    }

    private class GetRecordsTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... arguments) {
            incidents = MainActivity.db.getAllEventRecords();
            Collections.sort(incidents, new TimeComparator());// Sort records based on time
            return null;
        }

        protected void onPostExecute(Void result) {
            if (incidents.isEmpty()) {
                Toast toast = Toast.makeText(context, "No incident records stored.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
                finish();
            } else {
                IncidentView adp = new IncidentView(context, R.layout.view_incident_row, incidents);
                setListAdapter(adp);
            }
        }
    }
}