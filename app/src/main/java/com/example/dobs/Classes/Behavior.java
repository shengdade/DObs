package com.example.dobs.Classes;

import com.example.dobs.R;

import java.io.Serializable;

public class Behavior implements Serializable {

    public String name;
    public int color;

    public Behavior(String name) { //Without specific color, it is set to be default
        this.name = name;
        this.color = R.color.default_behavior;
    }

    public Behavior(String name, int color) {
        this.name = name;
        this.color = color;
    }
}
