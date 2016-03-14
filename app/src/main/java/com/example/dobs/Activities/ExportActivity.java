package com.example.dobs.Activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dobs.Classes.Patient;
import com.example.dobs.R;
import com.example.dobs.Tasks.ExportExcelTask;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ExportActivity extends AppCompatActivity {
    private static final String TAG = "ExportActivity";
    private AppCompatActivity context = this;
    EditText editStart;
    EditText editEnd;
    Calendar startDate;
    Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        if (MainActivity.patient == null)//In this case, the user has already created a profile
            MainActivity.patient = readPatient();

        editStart = (EditText) findViewById(R.id.editStart);
        editEnd = (EditText) findViewById(R.id.editEnd);
        Button btnExport = (Button) findViewById(R.id.btnExport);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        final DatePickerDialog.OnDateSetListener startDateListener;
        final DatePickerDialog.OnDateSetListener endDateListener;

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_YEAR, -1);
        startDate.add(Calendar.DAY_OF_MONTH, 1);

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startDate.set(Calendar.YEAR, year);
                startDate.set(Calendar.MONTH, monthOfYear);
                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editStart, startDate);
            }
        };
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, monthOfYear);
                endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editEnd, endDate);
            }
        };

        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar startDateShown = Calendar.getInstance();
                startDateShown.setTime(endDate.getTime());
                startDateShown.add(Calendar.WEEK_OF_YEAR, -1);
                startDateShown.add(Calendar.DAY_OF_MONTH, 1);
                new DatePickerDialog(context, startDateListener, startDateShown.get(Calendar.YEAR), startDateShown.get(Calendar.MONTH), startDateShown.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar endDateShown = Calendar.getInstance();
                endDateShown.setTime(startDate.getTime());
                endDateShown.add(Calendar.WEEK_OF_YEAR, 1);
                endDateShown.add(Calendar.DAY_OF_MONTH, -1);
                new DatePickerDialog(context, endDateListener, endDateShown.get(Calendar.YEAR), endDateShown.get(Calendar.MONTH), endDateShown.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnExport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onExportPressed(v);
                    }
                }
        );
        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCancelPressed(v);
                    }
                }
        );
    }

    private void onExportPressed(View v) {
        if (editStart.getText().toString().equals("") || editEnd.getText().toString().equals("")) {
            Toast.makeText(context, "Please specify a period.", Toast.LENGTH_SHORT).show();
        } else {
//            String myFormat = "yyyy-MM-dd";
//            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
//            Log.i(TAG, sdf.format(startDate.getTime()) + " || " + sdf.format(endDate.getTime()));
            GregorianCalendar start = new GregorianCalendar(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
            GregorianCalendar end = new GregorianCalendar(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
            end.add(Calendar.DAY_OF_MONTH, 1);
            new ExportExcelTask(context, start, end).execute();
        }
    }

    private void onCancelPressed(View v) {
        finish();
    }

    private void updateLabel(EditText editText, Calendar date) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
        editText.setText(sdf.format(date.getTime()));
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
