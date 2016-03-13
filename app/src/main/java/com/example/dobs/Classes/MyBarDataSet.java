package com.example.dobs.Classes;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Created by Dade on 03/03/2016.
 */
public class MyBarDataSet extends BarDataSet {

    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if (getEntryForXIndex(index).getVal() < 1) // less than 1 asleep
            return mColors.get(0);
        else if (getEntryForXIndex(index).getVal() == 1) // equal to 1 restless
            return mColors.get(1);
        else // greater than 1 awake
            return mColors.get(2);
    }
}
