package com.example.dobs.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dobs.Classes.Patient;
import com.example.dobs.R;
import com.example.dobs.Receivers.AlarmReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    AlarmReceiver alarm = new AlarmReceiver();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.context = this;
        if (MainActivity.patient == null)//In this case, the user has already created a profile
            MainActivity.patient = readPatient();

        Button btnDeleteProfile = (Button) findViewById(R.id.btnDeleteProfile);
        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                dlgAlert.setMessage("Are you sure to delete the patient profile and all the records?");
                dlgAlert.setNegativeButton("No", null);
                dlgAlert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.db.deleteBehaviorTable();
                                removeFile(MainActivity.patientFilename);
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });

        Button btnClearDatabase = (Button) findViewById(R.id.btnClearDatabase);
        btnClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                dlgAlert.setMessage("Are you sure to delete all the behavior records?");
                dlgAlert.setNegativeButton("No", null);
                dlgAlert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.db.deleteBehaviorTable();
                                Toast.makeText(context, "Database cleared", Toast.LENGTH_SHORT).show();
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });

        Button btnStartAlarm = (Button) findViewById(R.id.btnStartAlarm);
        btnStartAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setAlarm(context);
                Toast.makeText(context, "Alarm enabled", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnClearAlarm = (Button) findViewById(R.id.btnClearAlarm);
        btnClearAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setAlarm(context);
                alarm.cancelAlarm(context);
                Toast.makeText(context, "Alarm disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_okay:
                finish();
                Toast.makeText(this, "Settings stored", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_done, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_add);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    public void removeFile(String filename) {
        try {
            File fileToDelete = new File(this.getFilesDir(), filename);
            boolean deleted = fileToDelete.delete();
            if (deleted) {
                Toast.makeText(this, "Patient profile deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Patient profile already deleted", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
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