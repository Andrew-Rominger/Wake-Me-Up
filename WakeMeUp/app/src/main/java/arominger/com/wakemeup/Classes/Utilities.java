package arominger.com.wakemeup.Classes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import arominger.com.wakemeup.Recivers.alarmReciver;
import arominger.com.wakemeup.adapter.recViewAdapter;
import arominger.com.wakemeup.sqlDatabase.alarmDBHelper;
import arominger.com.wakemeup.sqlDatabase.sqlContract;

/*
Base class for utilities needed by app
 */
public class Utilities
{

    /**
     * @param context The context to use for making the alarm manager
     * @param minute The minute for the alarm
     * @param hour The hour for the alarm
     * @param day The day for the alarm
     * @param month The month for the alarm
     * @param year The year for the alarm
     */
    public static void createNewAlarm(Context context, int minute, int hour, int day, int month, int year)
    {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa", Locale.US);
        Calendar c = Calendar.getInstance();
        PendingIntent pendingIntent;

        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MONTH, month);

        Intent i = new Intent(context, alarmReciver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 2, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        alarmDBHelper helper = new alarmDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String date = String.valueOf(c.getTimeInMillis());
        values.put(sqlContract.FeedEntry.COLUMN_TIME_MS, date);
        db.insert(sqlContract.FeedEntry.TABLE_NAME, null, values);

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

        //Toast.makeText(context, "New alarm: " + sdf.format(new Date(c.getTimeInMillis())), Toast.LENGTH_LONG).show();

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


    public static int getPosition(Calendar c, Context context)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa", Locale.US);
        Log.v("L: ",sdf.format(new Date(c.getTimeInMillis())));
        alarmDBHelper helper = new alarmDBHelper(context);
        long l = c.getTimeInMillis();
        int position = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Long> list = new ArrayList<>();
        String[] prjection = {
                sqlContract.FeedEntry._ID,
                sqlContract.FeedEntry.COLUMN_TIME_MS
        };
        String sortOrder = sqlContract.FeedEntry.COLUMN_TIME_MS + " ASC";
        Cursor curs = db.query(sqlContract.FeedEntry.TABLE_NAME, prjection, null,null,null,null,sortOrder);
        curs.moveToFirst();
        while (!curs.isAfterLast())
        {
           long l2 = curs.getLong(curs.getColumnIndexOrThrow(sqlContract.FeedEntry.COLUMN_TIME_MS));
            Log.v("L: ", l+"");
            Log.v("L2: ", l2+"");

            if(l > l2)
            {
                position++;
            }
            else
            {
                break;
            }
            curs.moveToNext();
        }
        curs.close();
        return position;

    }

    public static void addAlarmtoList(recViewAdapter ad, int minute, int hour, int day, int month, int year, Context con, Long l)
    {
        alarmInList al = new alarmInList();
        Calendar c = createCalender(minute,hour,day,month,year);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");


        al.setDate(sdf1.format(c.getTime()));
        al.setTime(sdf2.format(c.getTime()));
        al.setTimeL(l);
        ad.addItem(getPosition(createCalender(minute,hour,day,month,year),con),al);


    }

    public static Calendar createCalender(int minute, int hour, int day, int month, int year)
    {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        return c;
    }
}
