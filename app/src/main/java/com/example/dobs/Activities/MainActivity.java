package com.example.dobs.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dobs.Classes.DatabaseHelper;
import com.example.dobs.Classes.Patient;
import com.example.dobs.Fragments.LoadScreen;
import com.example.dobs.R;
import com.example.dobs.Receivers.AlarmReceiver;

public class MainActivity extends AppCompatActivity {
    public static Patient patient;
    public static String patientFilename = "patient.dat";
    public static DatabaseHelper db = null;
    public static String datePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.fragMain, new LoadScreen()).commit();
        }
    }
}
