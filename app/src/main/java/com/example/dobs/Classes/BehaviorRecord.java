package com.example.dobs.Classes;

import com.example.dobs.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BehaviorRecord implements Serializable {
    public Calendar time;
    public Behavior behavior;
    public String environment;

    public BehaviorRecord() {
        this.time = GregorianCalendar.getInstance();
        this.behavior = new Behavior("");
        this.environment = "";
    }

    public long getTime() {
        return time.getTimeInMillis();
    }

    public String getBehavior() {
        return behavior.name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setTime(long timeLong) {
        this.time.setTimeInMillis(timeLong);
    }

    public void setBehavior(String name) {
        this.behavior.name = name;
        switch (name) {
            case "Sleeping in chair":
                this.behavior.color = R.color.my_blue;
                break;
            case "Sleeping in bed":
                this.behavior.color = R.color.green;
                break;
            case "Awake & calm":
                this.behavior.color = R.color.yellow;
                break;
            case "Noisy":
                this.behavior.color = R.color.orange;
                break;
            case "Restless, pacing":
                this.behavior.color = R.color.pink;
                break;
            case "Aggressive-verbal":
                this.behavior.color = R.color.silver;
                break;
            case "Aggressive-physical":
                this.behavior.color = R.color.red;
                break;
            default:
                this.behavior.color = R.color.default_behavior;
                break;
        }
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
