package com.example.dobs.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.dobs.Activities.MainActivity;
import com.example.dobs.Classes.Notifier;
import com.example.dobs.Classes.Patient;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm received");
        Notifier notifier = new Notifier(context);
        notifier.issueNotification();
    }

    public void setAlarm(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (MainActivity.patient == null)//In this case, the user has already created a profile
            MainActivity.patient = readPatient(context);

        Calendar triggerTime = getLaterTime();
        logDate(triggerTime);
        long triggerInterval = getAlarmInterval();
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), triggerInterval, alarmIntent);

        // Enable BootReceiver to automatically restart the alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable BootReceiver so that it doesn't automatically restart the alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private Calendar getLaterTime() {
        Calendar current = GregorianCalendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        Calendar future = GregorianCalendar.getInstance();
        future.setTimeInMillis(System.currentTimeMillis());
        int nowMinute = current.get(Calendar.MINUTE);
        future.set(Calendar.SECOND, 0);
        future.set(Calendar.MILLISECOND, 0);
        if (MainActivity.patient.trackingInterval == 30) {
            if (nowMinute < 30) {
                future.set(Calendar.MINUTE, 30);
            } else {
                future.set(Calendar.MINUTE, 0);
                future.add(Calendar.HOUR_OF_DAY, 1);
            }
        } else {
            if (nowMinute < 15) {
                future.set(Calendar.MINUTE, 15);
            } else if (nowMinute < 30) {
                future.set(Calendar.MINUTE, 30);
            } else if (nowMinute < 45) {
                future.set(Calendar.MINUTE, 45);
            } else {
                future.set(Calendar.MINUTE, 0);
                future.add(Calendar.HOUR_OF_DAY, 1);
            }
        }
        return future;
    }

//    private Calendar getFormerTime() {
//        Calendar current = GregorianCalendar.getInstance();
//        current.setTimeInMillis(System.currentTimeMillis());
//        Calendar future = GregorianCalendar.getInstance();
//        future.setTimeInMillis(System.currentTimeMillis());
//        int nowMinute = current.get(Calendar.MINUTE);
//        future.set(Calendar.SECOND, 0);
//        future.set(Calendar.MILLISECOND, 0);
//        if (MainActivity.patient.trackingInterval == 30) {
//            if (nowMinute < 30) {
//                future.set(Calendar.MINUTE, 0);
//            } else {
//                future.set(Calendar.MINUTE, 30);
//            }
//        } else {
//            if (nowMinute < 15) {
//                future.set(Calendar.MINUTE, 0);
//            } else if (nowMinute < 30) {
//                future.set(Calendar.MINUTE, 15);
//            } else if (nowMinute < 45) {
//                future.set(Calendar.MINUTE, 30);
//            } else {
//                future.set(Calendar.MINUTE, 45);
//            }
//        }
//        future.add(Calendar.MINUTE, -1);// Ahead 1 minute
//        return future;
//    }

    private long getAlarmInterval() {
        if (MainActivity.patient.trackingInterval == 30) {
            return AlarmManager.INTERVAL_HALF_HOUR;
        } else {
            return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        }
    }

//    private long getAlarmInterval() {
//        return 1000 * 20;
//    }

    private void logDate(Calendar date) {
        String myFormat = "yyyy-MM-dd HH:mm:ss.SSSZ";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
        Log.i(TAG, sdf.format(date.getTime()));
    }

    private Patient readPatient(Context context) {
        Patient patient = null;
        try {
            FileInputStream fis = context.openFileInput(MainActivity.patientFilename);
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