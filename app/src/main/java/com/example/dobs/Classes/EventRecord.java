package com.example.dobs.Classes;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by dade on 16/02/16.
 */
public class EventRecord implements Serializable {
    public Calendar time;
    public String description;
    public boolean hasPRN;
    public boolean isAggressive;
}
