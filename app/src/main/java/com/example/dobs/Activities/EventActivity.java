package com.example.dobs.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.dobs.Classes.EventRecord;
import com.example.dobs.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventActivity extends AppCompatActivity {
    private static final String TAG = " EventActivity";
    private Context context;
    CheckBox checkFall;
    CheckBox checkPRN;
    CheckBox checkAggressive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        this.context = this;
        checkFall = (CheckBox) findViewById(R.id.checkFall);
        checkPRN = (CheckBox) findViewById(R.id.checkPRN);
        checkAggressive = (CheckBox) findViewById(R.id.checkAggressive);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
                EventRecord record = new EventRecord(getTime(), checkFall.isChecked(), checkPRN.isChecked(), checkAggressive.isChecked());
                new AddEventTask().execute(record);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private Calendar getTime() {
        int[] hourMinutes = getIntent().getIntArrayExtra(CollectActivity.tagEvent);
        Calendar time = GregorianCalendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hourMinutes[0]);
        time.set(Calendar.MINUTE, hourMinutes[1]);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        return time;
    }

    private class AddEventTask extends AsyncTask<EventRecord, Void, Void> {

        protected Void doInBackground(EventRecord... records) {
            if (!isCancelled()) {
                MainActivity.db.addEventRecord(records[0]);
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Toast.makeText(context, "Incident stored", Toast.LENGTH_SHORT).show();
        }
    }
}
