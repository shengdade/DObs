package com.example.dobs.Tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dobs.Classes.MyBarDataSet;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class DrawSleepTask extends AsyncTask<Void, Void, Void> {
    private BarChart chart;
    private ArrayList<String> labels;
    private ArrayList<BarEntry> entries;

    public DrawSleepTask(BarChart chart, ArrayList<String> labels, ArrayList<BarEntry> entries) {
        this.chart = chart;
        this.labels = labels;
        this.entries = entries;
    }

    protected void onPreExecute() {
        try {
            MyBarDataSet dataSet = new MyBarDataSet(entries, "restless");
            dataSet.setColors(new int[]{Color.DKGRAY, Color.YELLOW, Color.RED});
            dataSet.setDrawValues(false);
            BarData data = new BarData(labels, dataSet);
            chart.getAxisLeft().setAxisMaxValue(1.5f);
            chart.getAxisLeft().setAxisMinValue(0);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisRight().setDrawGridLines(false);
            chart.getXAxis().setDrawAxisLine(false);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getAxisRight().setTextColor(Color.WHITE);
            chart.getAxisLeft().setDrawLabels(false);
            chart.getAxisRight().setDrawLabels(false);
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.setDescription("");
            chart.getLegend().setEnabled(false);
            chart.setData(data);
            chart.animateY(2000);
            chart.invalidate();
            Log.e(this.getClass().toString(), "Draw Sleep Success!");
        } catch (Exception e) {
            // if an exception occurred, show an error message
            Log.e(this.getClass().toString(), e.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }
}
