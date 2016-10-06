package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.session.PlaybackState;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andrew on 10/4/2016.
 */

public class Utilities {
    public static void createNewAlarm(Context context, int minute, int hour, int day, int month, int year, String TAG) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa", Locale.US);
        Calendar c = Calendar.getInstance();
        PendingIntent pendingIntent;
        Log.i(TAG, "Hour: " + hour);
        Log.i(TAG, "Minute: " + minute);
        Log.i(TAG, "Day: " + day);
        Log.i(TAG, "Month: " + month);
        Log.i(TAG, "Year: " + year);


        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_YEAR, day);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.MONTH, month - c.get(Calendar.MONTH));

        Intent i = new Intent(context, alarmReciver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        alarmDBHelper helper = new alarmDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String date = String.valueOf(c.getTimeInMillis());
        values.put(sqlContract.FeedEntry.COLUMN_TIME_MS, date);
        long newRowId = db.insert(sqlContract.FeedEntry.TABLE_NAME, null, values);

        Toast.makeText(context, "New alarm: " + sdf.format(new Date(c.getTimeInMillis())), Toast.LENGTH_LONG).show();




    }

    public static int maxAlarmVolume(Context c)
    {
        AudioManager am = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);
        int max;
        float f = 0;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        String vs = sharedPref.getString("pref_alrm", "100%");
        if(vs.length() == 3)
        {
            vs =  vs.substring(0,2);
            vs = "." + vs;
            f = Float.parseFloat(vs);
            max = (int) (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * f);

        }
        else
        {
            max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        Log.v("DEBUG", "MAX " + max);
        Log.v("DEBUG", "f " + f);
        int toReturn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max, AudioManager.FLAG_PLAY_SOUND);
        return toReturn;


    }
    public static void setAlarmVolume(int volume, Context c)
    {
        AudioManager am = (AudioManager)c.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_VIBRATE);
    }

    public static Calendar getEarliestCalender(Context context)
    {
        alarmDBHelper helper = new alarmDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] prjection = {
                sqlContract.FeedEntry._ID,
                sqlContract.FeedEntry.COLUMN_TIME_MS
        };
        String sortOrder = sqlContract.FeedEntry.COLUMN_TIME_MS + " ASC";
        Cursor c = db.query(sqlContract.FeedEntry.TABLE_NAME, prjection, null,null,null,null,sortOrder);
        c.moveToFirst();
        String date = c.getString(c.getColumnIndexOrThrow(sqlContract.FeedEntry.COLUMN_TIME_MS));
        Log.v("DEBUG", date);
        long l = Long.parseLong(date);
        Calendar cal = Calendar.getInstance();
        Log.v("DEBUG", "" + l);
        cal.setTimeInMillis(l);
        return cal;

    }
    public static void createNewAlarm(Context context, Calendar cal)
    {
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_YEAR);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);
        int month = cal.get(Calendar.MONTH);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa", Locale.US);
        Calendar c = Calendar.getInstance();
        PendingIntent pendingIntent;
        Log.v("DEBUG", ""+minute);
        c.set(Calendar.MINUTE, minute);

        Intent i = new Intent(context, alarmReciver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        alarmDBHelper helper = new alarmDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String date = String.valueOf(c.getTimeInMillis());
        values.put(sqlContract.FeedEntry.COLUMN_TIME_MS, date);
        long newRowId = db.insert(sqlContract.FeedEntry.TABLE_NAME, null, values);

        Toast.makeText(context, "New alarm: " + sdf.format(new Date(c.getTimeInMillis())), Toast.LENGTH_LONG).show();

    }
    public static ArrayList<Long> getAllAlarms(Context context)
    {
        alarmDBHelper helper = new alarmDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Long> list = new ArrayList<>();
        String[] prjection = {
                sqlContract.FeedEntry._ID,
                sqlContract.FeedEntry.COLUMN_TIME_MS
        };
        String sortOrder = sqlContract.FeedEntry.COLUMN_TIME_MS + " ASC";
        Cursor c = db.query(sqlContract.FeedEntry.TABLE_NAME, prjection, null,null,null,null,sortOrder);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            list.add(c.getLong(c.getColumnIndexOrThrow(sqlContract.FeedEntry.COLUMN_TIME_MS)));
            c.moveToNext();
        }

        return list;




    }
    public static void deleteAlarm(Context context, long timeInMS)
    {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmDBHelper helper = new alarmDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection =  sqlContract.FeedEntry.COLUMN_TIME_MS + " = ?";
        String[] selectionArgs = {"" + timeInMS};
        Intent i = new Intent(context, alarmReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);

        db.delete(sqlContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }


}
