package com.example.dobs.Tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.SleepLog;
import com.github.mikephil.charting.charts.BarChart;
import com.temboo.Library.Fitbit.Sleep.GetSleep;
import com.temboo.core.TembooSession;

public class FetchSleepTask extends AsyncTask<Void, Void, String> {

    private TextView textView;
    private BarChart chart;

    public FetchSleepTask(TextView textView, BarChart chart) {
        this.textView = textView;
        this.chart = chart;
    }

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        if (!isCancelled()) {
            dialog = new ProgressDialog(chart.getContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading");
            dialog.setIndeterminate(true);
            //dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(Void... arg0) {

        try {
            // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
            TembooSession session = new TembooSession("shengdade", "myFirstApp", "z8QMpb5tMIsoERAMa7wdldFNVFi4BGKS");
            //-----------------------------------------------------------------------------------------------------------------------
            GetSleep getSleepChoreo = new GetSleep(session);
            Log.i(this.getClass().toString(), "getSleepChoreo created");
            // Get an InputSet object for the choreo
            GetSleep.GetSleepInputSet getSleepInputs = getSleepChoreo.newInputSet();
            Log.i(this.getClass().toString(), "getSleepInputs created");
            // Set inputs
            getSleepInputs.set_AccessToken(MainActivity.patient.accessToken);
            getSleepInputs.set_Date(MainActivity.datePicked);
            Log.i(this.getClass().toString(), "getSleepInputs set ready");
            // Execute Choreo
            GetSleep.GetSleepResultSet getSleepResults = getSleepChoreo.execute(getSleepInputs);
            Log.i(this.getClass().toString(), "getSleepResults created");
            //-----------------------------------------------------------------------------------------------------------------------
            return (getSleepResults.get_Response());
        } catch (Exception e) {
            // if an exception occurred, log it
            Log.e(this.getClass().toString(), e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(String resultResponse) {
        try {
            SleepLog sleepLog = new SleepLog(resultResponse);
            dialog.dismiss();
            new DrawSleepTask(chart, sleepLog.getLabels(), sleepLog.getEntries()).execute();
            textView.setText(sleepLog.getSleepSummary());
            Log.e(this.getClass().toString(), "Fetch Sleep Success!");
        } catch (Exception e) {
            // if an exception occurred, show an error message
            Log.i(this.getClass().toString(), e.getMessage());
        }
    }
}
