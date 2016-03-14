package com.example.dobs.Classes;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MotionLog {
    private ArrayList<BarEntry> entries;
    private ArrayList<String> labels;

    public MotionLog(String response) {
        entries = new ArrayList<>();
        labels = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);
            JSONObject calories = reader.optJSONObject("activities-calories-intraday");
            JSONArray dataset = calories.optJSONArray("dataset");
            for (int i = 0; i < dataset.length(); i++) {
                JSONObject activity = dataset.getJSONObject(i);
                entries.add(new BarEntry((float) activity.optDouble("value"), i));
                labels.add(activity.optString("time").substring(0, 5));
            }
        } catch (Exception e) {
            {
                Log.i(this.getClass().toString(), e.getMessage());
            }
        }
    }

    public ArrayList<BarEntry> getEntries() {
        return (this.entries);
    }

    public ArrayList<String> getLabels() {
        return (this.labels);
    }
}
