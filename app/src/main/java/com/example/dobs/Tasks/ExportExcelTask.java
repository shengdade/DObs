package com.example.dobs.Tasks;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.Behavior;
import com.example.dobs.Classes.BehaviorRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
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
    List<BehaviorRecord> behaviors;

    private AppCompatActivity context;
    private Calendar startDate;
    private Calendar endDate;
    private long days;
    private SimpleDateFormat sdfDate;
    private SimpleDateFormat sdfTime;
    private HSSFPalette palette;
    private HashMap<Integer, BehaviorRecord> legendMap;

    public ExportExcelTask(AppCompatActivity context, Calendar startDate, Calendar endDate) {
        this.context = context;
        this.startDate = startDate;
        this.endDate = endDate;
        sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        sdfTime = new SimpleDateFormat("HH:mm", Locale.CANADA);
        legendMap = new HashMap<>();
        days = countDays();
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
            writeExcel(excelFilePath);
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

    public void writeExcel(String excelFileName) throws IOException {
        Workbook workbook = getWorkbook(excelFileName);
        Sheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(18);
        palette = ((HSSFWorkbook) sheet.getWorkbook()).getCustomPalette();
        createHeaderRow(sheet);
        createHeaderColumn(sheet);
        createMainForm(sheet);
        createLegend(sheet);
        writeToFile(workbook, excelFileName);
    }

    private void createLegend(Sheet sheet) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        Font myFont = sheet.getWorkbook().createFont();
        myFont.setItalic(true);
        cellStyle.setFont(myFont);
        Row row = sheet.getRow(1);
        int column = (int) days + 2;
        Cell cellCorner = row.createCell(column);
        cellCorner.setCellStyle(cellStyle);
        cellCorner.setCellValue("legend");

        SortedSet<Integer> keys = new TreeSet<Integer>(legendMap.keySet());
        ArrayList<Integer> keyArray = new ArrayList<>(keys);
        for (int i = 0; i < keyArray.size(); i++) {
            row = sheet.getRow(i + 3);
            Cell legend = row.createCell(column);
            int order = keyArray.get(i);
            BehaviorRecord record = legendMap.get(order);
            legend.setCellStyle(getCellStyle(sheet, record, false));
            legend.setCellValue("  " + order + ". " + record.behavior.name);
        }
    }

    private void createMainForm(Sheet sheet) {
        if (!behaviors.isEmpty()) {
            for (BehaviorRecord record : behaviors) {
                int[] behaviorPosition = getRowColumn(sheet, record);
                if (behaviorPosition[0] != 0 && behaviorPosition[1] != 0) {
                    Row row = sheet.getRow(behaviorPosition[0]);
                    Cell cell = row.createCell(behaviorPosition[1]);
                    cell.setCellStyle(getCellStyle(sheet, record, true));
                    cell.setCellValue(getCellValue(record));
                }
                if (!legendMap.values().contains(record)) {
                    legendMap.put(getCellValue(record), record);
                }
            }
        }
    }

    private int getCellValue(BehaviorRecord record) {
        int position = 0;
        for (Behavior behavior : MainActivity.patient.totalBehaviors) {
            if (record.behavior.name.equals(behavior.name)) {
                return (position + 1);
            }
            position++;
        }
        return position;
    }

    private CellStyle getCellStyle(Sheet sheet, BehaviorRecord record, boolean center) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        if (center) cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        int intColor = context.getResources().getColor(record.behavior.color);
        int red = Color.red(intColor);
        int green = Color.green(intColor);
        int blue = Color.blue(intColor);
        HSSFColor myColor = palette.findSimilarColor(red, green, blue);
        cellStyle.setFillForegroundColor(myColor.getIndex());
        return cellStyle;
    }

    private int[] getRowColumn(Sheet sheet, BehaviorRecord record) {
        Calendar calendar = record.time;
        String date = sdfDate.format(calendar.getTime());
        String time = sdfTime.format(calendar.getTime());
        int datePosition = findHeadRow(sheet, date);
        int timePosition = findHeadColumn(sheet, time);
        return new int[]{timePosition, datePosition};
    }

    private int findHeadRow(Sheet sheet, String cellContent) {
        Row row = sheet.getRow(0);
        for (Cell cell : row) {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return 0;
    }

    private int findHeadColumn(Sheet sheet, String cellContent) {
        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                    return row.getRowNum();
                }
            }
        }
        return 0;
    }

    private void createHeaderRow(Sheet sheet) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // set the corner cell name
        Row row = sheet.createRow(0);
        Cell cellCorner = row.createCell(0);
        cellCorner.setCellStyle(cellStyle);
        cellCorner.setCellValue("TIME");

        logDate(startDate);
        logDate(endDate);
        Log.i(TAG, "days between: " + String.valueOf(days));

        Calendar date = Calendar.getInstance();
        date.setTime(startDate.getTime());
        for (int i = 1; i <= days; i++) {
            Cell cellTitle = row.createCell(i);
            cellTitle.setCellStyle(cellStyle);
            cellTitle.setCellValue(sdfDate.format(date.getTime()));
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void createHeaderColumn(Sheet sheet) {
        int trackingInterval = MainActivity.patient.trackingInterval;
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        int intervals = countIntervals(trackingInterval);
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 7);
        date.set(Calendar.MINUTE, 30);
        for (int i = 1; i <= intervals; i++) {
            Row row = sheet.createRow(i);
            Cell firstCell = row.createCell(0);
            firstCell.setCellStyle(cellStyle);
            firstCell.setCellValue(sdfTime.format(date.getTime()));
            date.add(Calendar.MINUTE, trackingInterval);
        }
    }

    private long countDays() {
        long milis1 = startDate.getTimeInMillis();
        long milis2 = endDate.getTimeInMillis();
        long diff = Math.abs(milis2 - milis1);
        return (TimeUnit.MILLISECONDS.toDays(diff));
    }

    private int countIntervals(int trackingInterval) {
        if (trackingInterval == 30) {
            return 48;
        } else {
            return 96;
        }
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
