package com.example.dobs.Tasks;

import android.app.ProgressDialog;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.BehaviorRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExportExcelTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "ExportExcelTask";
    private AppCompatActivity context;

    List<BehaviorRecord> behaviors;
    private Calendar startDate;
    private Calendar endDate;

    public ExportExcelTask(AppCompatActivity context, Calendar startDate, Calendar endDate) {
        this.context = context;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        if (!isCancelled()) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        behaviors = MainActivity.db.getBehaviorRecords(startDate, endDate);
        String excelFilePath = "Records.xls";
        try {
            writeExcel(behaviors, excelFilePath);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
        Toast.makeText(context, "File saved", Toast.LENGTH_LONG).show();
    }

    public void writeExcel(List<BehaviorRecord> behaviors, String excelFileName) throws IOException {
        Workbook workbook = getWorkbook(excelFileName);
        Sheet sheet = workbook.createSheet();
        createHeaderRow(sheet);

        writeToFile(workbook, excelFileName);
    }

    private void createHeaderRow(Sheet sheet) {

        sheet.setDefaultColumnWidth(20);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Row row = sheet.createRow(0);
        long days = countDays();

        logDate(startDate);
        logDate(endDate);
        Log.i(TAG, "days between: " + String.valueOf(days));

        Calendar date = Calendar.getInstance();
        date.setTime(startDate.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        for (int i = 1; i <= days; i++) {
            Cell cellTitle = row.createCell(i);
            cellTitle.setCellStyle(cellStyle);
            cellTitle.setCellValue(sdf.format(date.getTime()));
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private long countDays() {
        long milis1 = startDate.getTimeInMillis();
        long milis2 = endDate.getTimeInMillis();
        long diff = Math.abs(milis2 - milis1);
        return (TimeUnit.MILLISECONDS.toDays(diff) + 1);
    }

    private void writeToFile(Workbook workbook, String excelFileName) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(this.getClass().getSimpleName(), "Storage not available or read only");
        } else {
            File dobsDir = new File(Environment.getExternalStorageDirectory(), "Dobs");
            if (!dobsDir.exists() && !dobsDir.mkdirs()) {
                Log.e(this.getClass().getSimpleName(), "Creating directory DObs failed");
            }
            File file = new File(dobsDir, excelFileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                workbook.write(outputStream);
                outputStream.close();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
            // To solve the problem: "Folder created on Android external storage isn't visible via MTP"
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            // Inspired by http://stackoverflow.com/questions/13737261/nexus-4-not-showing-files-via-mtp
        }
    }

    private Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            //workbook = new XSSFWorkbook();
            Log.e(this.getClass().getSimpleName(), "Cannot write xlsx file for now");
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(extStorageState));
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState));
    }

    private void logDate(Calendar date) {
        String myFormat = "yyyy-MM-dd HH:mm:ss.SSSZ";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
        Log.i(TAG, sdf.format(date.getTime()));
    }
}
