package com.example.dobs.Classes;

import java.io.Serializable;
import java.util.Calendar;

public class Record implements Serializable {
    public Calendar time;

    public Record() {
        this.time = Calendar.getInstance();
    }

    public Record(Calendar time) {
        this.time = time;
    }
}
