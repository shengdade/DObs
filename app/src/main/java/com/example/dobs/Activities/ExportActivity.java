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

import com.example.dobs.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        editStart = (EditText) findViewById(R.id.editStart);
        editEnd = (EditText) findViewById(R.id.editEnd);
        Button btnExport = (Button) findViewById(R.id.btnExport);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        final DatePickerDialog.OnDateSetListener startDateListener;
        final DatePickerDialog.OnDateSetListener endDateListener;

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_YEAR, -1);

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
                new DatePickerDialog(context, startDateListener, startDateShown.get(Calendar.YEAR), startDateShown.get(Calendar.MONTH), startDateShown.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar endDateShown = Calendar.getInstance();
                endDateShown.setTime(startDate.getTime());
                endDateShown.add(Calendar.WEEK_OF_YEAR, 1);
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
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
            Log.i(TAG, sdf.format(startDate.getTime()) + " || " + sdf.format(endDate.getTime()));
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
}
