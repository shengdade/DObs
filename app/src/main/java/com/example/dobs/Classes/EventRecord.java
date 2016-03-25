package com.example.dobs.Classes;

import java.io.Serializable;
import java.util.Calendar;

public class EventRecord implements Serializable {
    private Calendar time;
    private boolean hasFall;
    private boolean hasPRN;
    private boolean isAggressive;

    public EventRecord() {
    }

    public EventRecord(Calendar time, boolean hasFall, boolean hasPRN, boolean isAggressive) {
        this.time = time;
        this.hasFall = hasFall;
        this.hasPRN = hasPRN;
        this.isAggressive = isAggressive;
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
