package com.example.dobs.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.dobs.Classes.Patient;
import com.example.dobs.R;
import com.example.dobs.Tasks.RefreshTokenTask;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ViewActivity extends AppCompatActivity {
    private static final String TAG = "ViewActivity";
    private AppCompatActivity context;
    private DatePicker dataPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        this.context = this;
        if (MainActivity.patient == null)//In this case, the user has already created a profile
            MainActivity.patient = readPatient();
        new RefreshTokenTask(this).execute();
        dataPicker = (DatePicker) findViewById(R.id.datePicker);

        Button btnBehavior = (Button) findViewById(R.id.btnBehavior);
        btnBehavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewBehaviors.class);
                intent.putExtra("DatePicked", getPickedDate());
                startActivity(intent);
            }
        });

        Button btnMotion = (Button) findViewById(R.id.btnMotion);
        btnMotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.datePicked = getPickedDateString();
                startActivity(new Intent(context, MotionActivity.class));
            }
        });

        Button btnSleep = (Button) findViewById(R.id.btnSleep);
        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.datePicked = getPickedDateString();
                startActivity(new Intent(context, SleepActivity.class));
            }
        });
    }

    private int[] getPickedDate() {
        return (new int[]{dataPicker.getYear(), dataPicker.getMonth(), dataPicker.getDayOfMonth()});
    }

    private String getPickedDateString() {
        return (String.valueOf(dataPicker.getYear()) + "-" + checkDigit(dataPicker.getMonth() + 1) + "-" + checkDigit(dataPicker.getDayOfMonth()));
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private Patient readPatient() {
        Patient patient = null;
        try {
            FileInputStream fis = openFileInput(MainActivity.patientFilename);
            ObjectInputStream is = new ObjectInputStream(fis);
            patient = (Patient) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return patient;
    }
}