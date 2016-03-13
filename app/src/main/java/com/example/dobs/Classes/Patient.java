package com.example.dobs.Classes;

import com.example.dobs.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dade on 15/02/16.
 */
public class Patient implements Serializable {
    public String ID;
    public int trackingInterval;
    public ArrayList<Behavior> totalBehaviors;
    public ArrayList<Behavior> trackingBehaviors;
    public String accessToken;
    public String refreshToken;

    public Patient() {
        totalBehaviors = new ArrayList<Behavior>();
        trackingBehaviors = new ArrayList<Behavior>();

        totalBehaviors.add(new Behavior("Sleeping in chair", R.color.my_blue));
        totalBehaviors.add(new Behavior("Sleeping in bed", R.color.green));
        totalBehaviors.add(new Behavior("Awake & calm", R.color.yellow));
        totalBehaviors.add(new Behavior("Noisy", R.color.orange));
        totalBehaviors.add(new Behavior("Restless, pacing", R.color.pink));
        totalBehaviors.add(new Behavior("Aggressive-verbal", R.color.white));
        totalBehaviors.add(new Behavior("Aggressive-physical", R.color.red));
    }
}
