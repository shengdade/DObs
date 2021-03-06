package com.example.dobs.Classes;

import java.io.Serializable;
import java.util.Calendar;

public class EventRecord extends Record implements Serializable {
    private boolean hasFall;
    private boolean hasPRN;
    private boolean isAggressive;

    public EventRecord() {
        super();
        this.hasFall = false;
        this.hasPRN = false;
        this.isAggressive = false;
    }

    public EventRecord(Calendar time, boolean hasFall, boolean hasPRN, boolean isAggressive) {
        super(time);
        this.hasFall = hasFall;
        this.hasPRN = hasPRN;
        this.isAggressive = isAggressive;
    }

    public String getIncident() {
        String incident = "";
        if (hasFall) incident += "  <Fall>  ";
        if (hasPRN) incident += "  <PRN>  ";
        if (isAggressive) incident += "  <Aggressive>  ";
        return incident;
    }

    public long getTime() {
        return time.getTimeInMillis();
    }

    public void setTime(long timeLong) {
        this.time.setTimeInMillis(timeLong);
    }

    public int getHasFall() {
        return ((hasFall) ? 1 : 0);
    }

    public int getHasPRN() {
        return ((hasPRN) ? 1 : 0);
    }

    public int getIsAggressive() {
        return ((isAggressive) ? 1 : 0);
    }

    public void setHasFall(int hasFall) {
        this.hasFall = (hasFall != 0);
    }

    public void setHasPRN(int hasPRN) {
        this.hasPRN = (hasPRN != 0);
    }

    public void setIsAggressive(int isAggressive) {
        this.isAggressive = (isAggressive != 0);
    }
}