package com.example.dobs.Classes;

import java.util.Comparator;

public class TimeComparator implements Comparator<Record> {
    @Override
    public int compare(Record record1, Record record2) {
        return record1.time.getTime().compareTo(record2.time.getTime());
    }
}
