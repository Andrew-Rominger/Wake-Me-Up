package arominger.com.wakemeup;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class alarmRinging extends AppCompatActivity
{
    Intent startAlarm;
    private static final String TAG = alarmRinging.class.getSimpleName();
    TextView date, time;
    Button dismiss, snooze;
    int old;

    alarmDBHelper helper;
    SQLiteDatabase db;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        startAlarm = new Intent(this, AlarmPlayingService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        helper = new alarmDBHelper(this);
        db = helper.getReadableDatabase();

        time = (TextView) findViewById(R.id.alarmTime);
        date = (TextView) findViewById(R.id.alarmDate);
        snooze = (Button) findViewById(R.id.snoozeButton);
        dismiss = (Button) findViewById(R.id.dismissButton);



        c = Utilities.getEarliestCalender(this);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyy");

        String times = sdf.format(c.getTime());
        String dates = sdf2.format(c.getTime());
        time.setText(times);
        date.setText(dates);
        old = Utilities.maxAlarmVolume(this);
        startService(startAlarm);

    }

    public void dismissAlarm(View view)
    {
        Log.v(TAG, "Dismissing");
        stopService(new Intent(this, AlarmPlayingService.class));
        String selection =  sqlContract.FeedEntry.COLUMN_TIME_MS + " = ?";
        String[] selectionArgs = {"" + c.getTimeInMillis()};
        db.delete(sqlContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Utilities.setAlarmVolume(old,this);
        this.finish();

    }

    public void snoozeAlarm(View view)
    {
        Log.v(TAG, "snoozing");
        String selection =  sqlContract.FeedEntry.COLUMN_TIME_MS + " = ?";
        String[] selectionArgs = {"" + c.getTimeInMillis()};
        db.delete(sqlContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Calendar newCal = Calendar.getInstance();
        Random r = new Random();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String vs = sharedPref.getString("pref_snooze", "10");
        int l = r.nextInt(Integer.parseInt(vs));
        Log.v(TAG, "Length: " + l);
        newCal.add(Calendar.MINUTE, l);
        Utilities.createNewAlarm(this, newCal);
        Utilities.setAlarmVolume(old,this);
        stopService(new Intent(this, AlarmPlayingService.class));
        this.finish();

    }

}
