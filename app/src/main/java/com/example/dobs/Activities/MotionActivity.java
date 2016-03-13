package com.example.dobs.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dobs.R;
import com.example.dobs.Tasks.FetchMotionTask;
import com.github.mikephil.charting.charts.BarChart;

public class MotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        TextView log = (TextView) findViewById(R.id.log);
        BarChart chart = (BarChart) findViewById(R.id.chart);
        // may raise bugs in case FinalizeOAuthTask is not completed
        new FetchMotionTask(log, chart).execute();
    }
}