package com.example.dobs.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DObs.db";
    private static final int SCHEMA = 1;
    static final String TABLE_BEHAVIOR = "behavior";
    static final String TABLE_EVENT = "event";

    //define the columns of TABLE_BEHAVIOR
    static final String TIME = "time";
    static final String BEHAVIOR = "behavior";
    static final String ENVIRONMENT = "environment";
    static final String[] COLUMNS_BEHAVIOR = {TIME, BEHAVIOR, ENVIRONMENT};

    //define the columns of TABLE_EVENT
    static final String FALL = "hasFall";
    static final String PRN = "hasPRN";
    static final String AGGRESSIVE = "isAggressive";
    static final String[] COLUMNS_EVENT = {TIME, FALL, PRN, AGGRESSIVE};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BEHAVIOR + " (time INTEGER, behavior TEXT, environment TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_EVENT + " (time INTEGER, hasFall INTEGER, hasPRN INTEGER, isAggressive INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEHAVIOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

        // create fresh table
        this.onCreate(db);
    }

    public void addBehaviorRecord(BehaviorRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, record.getTime());
        values.put(BEHAVIOR, record.getBehavior());
        values.put(ENVIRONMENT, record.getEnvironment());
        db.insert(TABLE_BEHAVIOR, null, values);
        db.close();
    }

    public void addEventRecord(EventRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, record.getTime());
        values.put(FALL, record.getHasFall());
        values.put(PRN, record.getHasPRN());
        values.put(AGGRESSIVE, record.getIsAggressive());
        db.insert(TABLE_EVENT, null, values);
        db.close();
    }

    public int updateBehaviorRecord(BehaviorRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, record.getTime());
        values.put(BEHAVIOR, record.getBehavior());
        values.put(ENVIRONMENT, record.getEnvironment());

        int i = db.update(TABLE_BEHAVIOR, //table
                values, // column/value
                TIME + " = ?", // selections
                new String[]{String.valueOf(record.getTime())}); //selection args

        db.close();
        return i;
    }

    public int updateEventRecord(EventRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, record.getTime());
        values.put(FALL, record.getHasFall());
        values.put(PRN, record.getHasPRN());
        values.put(AGGRESSIVE, record.getIsAggressive());

        int i = db.update(TABLE_EVENT, //table
                values, // column/value
                TIME + " = ?", // selections
                new String[]{String.valueOf(record.getTime())}); //selection args

        db.close();
        return i;
    }

    public BehaviorRecord getBehaviorRecord(Calendar time) {
        BehaviorRecord record = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_BEHAVIOR, // a. table
                        COLUMNS_BEHAVIOR, // b. column names
                        " time = ?", // c. selections
                        new String[]{String.valueOf(time.getTimeInMillis())}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor.moveToFirst()) {
            record = new BehaviorRecord();
            record.setTime(cursor.getLong(0));
            record.setBehavior(cursor.getString(1));
            record.setEnvironment(cursor.getString(2));
        }

        cursor.close();
        db.close();
        return record;
    }

    public EventRecord getEventRecord(Calendar time) {
        EventRecord record = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_EVENT, // a. table
                        COLUMNS_EVENT, // b. column names
                        " time = ?", // c. selections
                        new String[]{String.valueOf(time.getTimeInMillis())}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor.moveToFirst()) {
            record = new EventRecord();
            record.setTime(cursor.getLong(0));
            record.setHasFall(cursor.getInt(1));
            record.setHasPRN(cursor.getInt(2));
            record.setIsAggressive(cursor.getInt(3));
        }

        cursor.close();
        db.close();
        return record;
    }

    public List<BehaviorRecord> getAllBehaviorRecords() {
        List<BehaviorRecord> records = new LinkedList<BehaviorRecord>();
        String query = "SELECT  * FROM " + TABLE_BEHAVIOR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        BehaviorRecord record = null;
        if (cursor.moveToFirst()) {
            do {
                record = new BehaviorRecord();
                record.setTime(cursor.getLong(0));
                record.setBehavior(cursor.getString(1));
                record.setEnvironment(cursor.getString(2));
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

    public List<BehaviorRecord> getBehaviorRecords(Calendar startTime, Calendar endTime) {
        List<BehaviorRecord> records = new LinkedList<BehaviorRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_BEHAVIOR, // a. table
                        COLUMNS_BEHAVIOR, // b. column names
                        " time >= ? AND time < ? ", // c. selections
                        new String[]{String.valueOf(startTime.getTimeInMillis()), String.valueOf(endTime.getTimeInMillis())}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        BehaviorRecord record = null;
        if (cursor.moveToFirst()) {
            do {
                record = new BehaviorRecord();
                record.setTime(cursor.getLong(0));
                record.setBehavior(cursor.getString(1));
                record.setEnvironment(cursor.getString(2));
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

    public List<EventRecord> getEventRecords(Calendar startTime, Calendar endTime) {
        List<EventRecord> records = new LinkedList<EventRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_EVENT, // a. table
                        COLUMNS_EVENT, // b. column names
                        " time >= ? AND time < ? ", // c. selections
                        new String[]{String.valueOf(startTime.getTimeInMillis()), String.valueOf(endTime.getTimeInMillis())}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        EventRecord record = null;
        if (cursor.moveToFirst()) {
            do {
                record = new EventRecord();
                record.setTime(cursor.getLong(0));
                record.setHasFall(cursor.getInt(1));
                record.setHasPRN(cursor.getInt(2));
                record.setIsAggressive(cursor.getInt(3));
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }

    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_BEHAVIOR);
        db.execSQL("delete from " + TABLE_EVENT);
        db.close();
    }
}