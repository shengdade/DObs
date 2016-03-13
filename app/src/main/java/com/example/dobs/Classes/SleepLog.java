package com.example.dobs.Classes;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dade on 02/03/2016.
 */
public class SleepLog {
    private int totalMinutesAsleep;
    private int totalTimeInBed;
    private int mainSleep_awakeCount;
    private int mainSleep_restlessCount;
    private int mainSleep_awakeDuration;
    private int mainSleep_restlessDuration;
    private int mainSleep_minutesToFallAsleep;
    private ArrayList<BarEntry> entries;
    private ArrayList<String> labels;

    public SleepLog(String response) {
        try {
            JSONObject reader = new JSONObject(response);
            JSONObject summary = reader.optJSONObject("summary");
            this.totalMinutesAsleep = summary.optInt("totalMinutesAsleep");
            this.totalTimeInBed = summary.optInt("totalTimeInBed");

            JSONArray sleepArray = reader.optJSONArray("sleep");
            for (int i = 0; i < sleepArray.length(); i++) {
                JSONObject sleep = sleepArray.getJSONObject(i);
                if (sleep.optBoolean("isMainSleep")) {
                    this.mainSleep_awakeCount = sleep.optInt("awakeCount");
                    this.mainSleep_restlessCount = sleep.optInt("restlessCount");
                    this.mainSleep_awakeDuration = sleep.optInt("awakeDuration");
                    this.mainSleep_restlessDuration = sleep.optInt("restlessDuration");
                    this.mainSleep_minutesToFallAsleep = sleep.optInt("minutesToFallAsleep");

                    entries = new ArrayList<>();
                    labels = new ArrayList<>();
                    JSONArray dataArray = sleep.optJSONArray("minuteData");
                    for (int j = 0; j < dataArray.length(); j++) {
                        JSONObject record = dataArray.getJSONObject(j);
                        entries.add(new BarEntry(Integer.parseInt(record.optString("value")) * 0.1f + 0.8f, j));
                        labels.add(record.optString("dateTime").substring(0, 5));
                    }

                    break;
                }
            }

        } catch (Exception e) {
            {
                Log.i(this.getClass().toString(), e.getMessage());
            }
        }
    }

    public String getTimeAsleep() {
        int hour = this.totalMinutesAsleep / 60;
        int minute = this.totalMinutesAsleep % 60;
        if (hour == 0) {
            return (String.valueOf(minute) + " min");
        } else {
            return (String.valueOf(hour) + " hr " + String.valueOf(minute) + " min");
        }

    }

    public String getTimeInBed() {
        int hour = this.totalTimeInBed / 60;
        int minute = this.totalTimeInBed % 60;
        if (hour == 0) {
            return (String.valueOf(minute) + " min");
        } else {
            return (String.valueOf(hour) + " hr " + String.valueOf(minute) + " min");
        }
    }

    public String getAwakeTimes() {
        return String.valueOf(this.mainSleep_awakeCount);
    }

    public String getRestlessTimes() {
        return String.valueOf(this.mainSleep_restlessCount);
    }

    public String getTimeAwakeRestless() {
        return String.valueOf(this.mainSleep_awakeDuration + this.mainSleep_restlessDuration);
    }

    public String geMinutesToFallAsleep() {
        return String.valueOf(this.mainSleep_minutesToFallAsleep);
    }

    public String getSleepSummary() {
        return "Total in bed: \t" + getTimeInBed() +
                "\nTotal asleep: \t" + getTimeAsleep() +
                "\n\n" + getAwakeTimes() + " times awake" +
                "\n" + getRestlessTimes() + " times restless" +
                "\n" + getTimeAwakeRestless() + " min awake / restless";
    }

    public ArrayList<BarEntry> getEntries() {
        return (this.entries);
    }

    public ArrayList<String> getLabels() {
        return (this.labels);
    }
}
