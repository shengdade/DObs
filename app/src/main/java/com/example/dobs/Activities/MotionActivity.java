package com.example.dobs.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dobs.R;
import com.example.dobs.Tasks.FetchMotionTask;
import com.github.mikephil.charting.charts.BarChart;

public class MotionActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        this.context = this;
        TextView log = (TextView) findViewById(R.id.log);
        final BarChart chart = (BarChart) findViewById(R.id.chart);
        chart.setOnLongClickListener(new View.OnLongClickListener() {
                                         @Override
                                         public boolean onLongClick(View v) {
                                             AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                                             dlgAlert.setMessage("Save current chart to gallery?");
                                             dlgAlert.setNegativeButton("No", null);
                                             dlgAlert.setPositiveButton("Yes",
                                                     new DialogInterface.OnClickListener() {
                                                         public void onClick(DialogInterface dialog, int which) {
                                                             saveImage(chart);
                                                         }
                                                     });
                                             dlgAlert.setCancelable(true);
                                             dlgAlert.create().show();
                                             return true;
                                         }
                                     }
        );
        // may raise bugs in case FinalizeOAuthTask is not completed
        new FetchMotionTask(log, chart).execute();
    }

    private void saveImage(BarChart chart) {
        String filename = MainActivity.datePicked + "<motion>";
        String subFolderPath = "DObs";
        String fileDescription = "motion data on " + MainActivity.datePicked;
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        int quality = 100;
        if (chart.saveToGallery(filename, subFolderPath, fileDescription, format, quality)) {
            Toast.makeText(this, "chart saved as " + filename, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "chart saving failed", Toast.LENGTH_SHORT).show();
        }
    }
}