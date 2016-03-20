package com.example.dobs.Tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.BehaviorRecord;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class GenerateBehaviorsTask extends AsyncTask<Void, Void, Void> {
    private AppCompatActivity context;
    private Calendar startDate;
    private Calendar endDate;

    public GenerateBehaviorsTask(AppCompatActivity context, Calendar startDate, Calendar endDate) {
        this.context = context;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        if (!isCancelled()) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    protected Void doInBackground(Void... args) {
        if (!isCancelled()) {
            int trackingInterval = MainActivity.patient.trackingInterval;
            Calendar date = Calendar.getInstance();
            date.setTime(startDate.getTime());
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            int intervals = countIntervals(trackingInterval);
            for (int d = 1; d <= (int) countDays(); d++) {
                for (int i = 1; i <= intervals; i++) {
                    addRecord(date);
                    date.add(Calendar.MINUTE, trackingInterval);
                }
                //date.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return null;
    }

    private void addRecord(Calendar date) {
        BehaviorRecord record = new BehaviorRecord(date);
        if (MainActivity.db.getBehaviorRecord(record.time) == null) {
            MainActivity.db.addBehaviorRecord(record);// if the behavior does not exist, add
        } else {
            MainActivity.db.updateBehaviorRecord(record);// if the behavior does exist, update
        }
    }

    protected void onPostExecute(Void result) {
        dialog.dismiss();
        Toast.makeText(context, "Records generated", Toast.LENGTH_SHORT).show();
    }

    private long countDays() {
        long milis1 = startDate.getTimeInMillis();
        long milis2 = endDate.getTimeInMillis();
        long diff = Math.abs(milis2 - milis1);
        return (TimeUnit.MILLISECONDS.toDays(diff));
    }

    private int countIntervals(int trackingInterval) {
        if (trackingInterval == 30) {
            return 48;
        } else {
            return 96;
        }
    }
}