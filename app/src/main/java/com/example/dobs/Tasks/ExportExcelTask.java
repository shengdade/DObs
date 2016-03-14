package com.example.dobs.Tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.BehaviorRecord;

import java.util.Calendar;
import java.util.List;

public class ExportExcelTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "ExportExcelTask";
    private AppCompatActivity context;
    List<BehaviorRecord> behaviors;
    private Calendar startDate;
    private Calendar endDate;

    public ExportExcelTask(AppCompatActivity context, Calendar startDate, Calendar endDate) {
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

    @Override
    protected Void doInBackground(Void... arg0) {
        behaviors = MainActivity.db.getBehaviorRecords(startDate, endDate);
        return null;
    }

    protected void onPostExecute(Void result) {
        dialog.dismiss();
    }
}
