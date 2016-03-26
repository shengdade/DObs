package com.example.dobs.Activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.example.dobs.Adapters.RecordView;
import com.example.dobs.Classes.BehaviorRecord;
import com.example.dobs.Classes.Record;
import com.example.dobs.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ViewBehaviors extends ListActivity {
    private static final String TAG = "ViewBehaviors";
    List<Record> records;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int[] pickedDate = getIntent().getIntArrayExtra("DatePicked");
        GregorianCalendar date = new GregorianCalendar(pickedDate[0], pickedDate[1], pickedDate[2]);
        new GetRecordsTask(date).execute();//Go fetch records
    }

    private class GetRecordsTask extends AsyncTask<Void, Void, Void> {
        private GregorianCalendar date;

        public GetRecordsTask(GregorianCalendar date) {
            this.date = date;
        }

        protected Void doInBackground(Void... arguments) {
            GregorianCalendar dateEnd = new GregorianCalendar();
            dateEnd.setTime(date.getTime());
            dateEnd.add(Calendar.DAY_OF_MONTH, 1);
            List<BehaviorRecord> behaviors = MainActivity.db.getBehaviorRecords(date, dateEnd);
            records = new ArrayList<Record>();
            records.addAll(behaviors);
            return null;
        }

        protected void onPostExecute(Void result) {
            if (records.isEmpty()) {
                Toast toast = Toast.makeText(context, "No record on this day.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
                finish();
            } else {
                RecordView adp = new RecordView(context, R.layout.view_behavior_row, records);
                setListAdapter(adp);
            }
        }
    }
}
