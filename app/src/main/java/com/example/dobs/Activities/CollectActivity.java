package com.example.dobs.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.example.dobs.Classes.Patient;
import com.example.dobs.R;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Calendar;

public class CollectActivity extends AppCompatActivity {
    private static final String TAG = "CollectActivity";
    public final static String tagBehavior = "timeBehavior";
    public final static String tagEvent = "timeEvent";
    private TimePicker timeBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        if (MainActivity.patient == null)//In this case, the user has already created a profile
            MainActivity.patient = readPatient();

        timeBehavior = (TimePicker) findViewById(R.id.timeBehavior);
        final TimePicker timeEvent = (TimePicker) findViewById(R.id.timeEvent);
        ImageButton imageBehavior = (ImageButton) findViewById(R.id.imageBehavior);
        ImageButton imageEvent = (ImageButton) findViewById(R.id.imageEvent);

        timeBehavior.setAddStatesFromChildren(true);
        timeEvent.setAddStatesFromChildren(true);
        timeBehavior.setEnabled(false);

        imageBehavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent behaviorRecord = new Intent(v.getContext(), BehaviorActivity.class);
                timeBehavior.clearFocus();
                int iHour = timeBehavior.getCurrentHour();
                int iMinute = timeBehavior.getCurrentMinute();
                behaviorRecord.putExtra(tagBehavior, new int[]{iHour, iMinute});
                startActivity(behaviorRecord);
            }
        });

        imageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventRecord = new Intent(v.getContext(), EventActivity.class);
                timeEvent.clearFocus();
                int iHour = timeEvent.getCurrentHour();
                int iMinute = timeEvent.getCurrentMinute();
                eventRecord.putExtra(tagEvent, new int[]{iHour, iMinute});
                startActivity(eventRecord);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fixInterval();
    }

    private void fixInterval() {
        Calendar c = Calendar.getInstance();
        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        if (MainActivity.patient.trackingInterval == 30) {
            if (nowMinute <= 10) {
                timeBehavior.setCurrentMinute(0);
            } else if (nowMinute <= 40) {
                timeBehavior.setCurrentMinute(30);
            } else {
                timeBehavior.setCurrentMinute(0);
                if (nowHour < 23) {
                    timeBehavior.setCurrentHour(nowHour + 1);
                } else {
                    timeBehavior.setCurrentHour(0);
                }
            }
        } else {
            if (nowMinute <= 5) {
                timeBehavior.setCurrentMinute(0);
            } else if (nowMinute <= 20) {
                timeBehavior.setCurrentMinute(15);
            } else if (nowMinute <= 35) {
                timeBehavior.setCurrentMinute(30);
            } else if (nowMinute <= 50) {
                timeBehavior.setCurrentMinute(45);
            } else {
                timeBehavior.setCurrentMinute(0);
                if (nowHour < 23) {
                    timeBehavior.setCurrentHour(nowHour + 1);
                } else {
                    timeBehavior.setCurrentHour(0);
                }
            }
        }
    }

    private Patient readPatient() {
        Patient patient = null;
        try {
            FileInputStream fis = openFileInput(MainActivity.patientFilename);
            ObjectInputStream is = new ObjectInputStream(fis);
            patient = (Patient) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return patient;
    }
}
