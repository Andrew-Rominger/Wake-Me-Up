package arominger.com.wakemeup.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import arominger.com.wakemeup.Classes.Utilities;
import arominger.com.wakemeup.R;
import arominger.com.wakemeup.Services.AlarmPlayingService;
import arominger.com.wakemeup.sqlDatabase.alarmDBHelper;
import arominger.com.wakemeup.sqlDatabase.sqlContract;

public class alarmRinging extends AppCompatActivity
{
    Intent startAlarm;
    private static final String TAG = alarmRinging.class.getSimpleName();
    TextView date, time;
    Button dismiss, snooze;
    int old;
    Vibrator vb;

    alarmDBHelper helper;
    SQLiteDatabase db;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        startAlarm = new Intent(this, AlarmPlayingService.class);
        super.onCreate(savedInstanceState);
        vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);
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
        if(vb.hasVibrator())
        {
            long[] list = {500,1000};
            vb.vibrate(list,0);
        }

    }

    public void dismissAlarm(View view)
    {
        Log.v(TAG, "Dismissing");
        stopService(new Intent(this, AlarmPlayingService.class));
        String selection =  sqlContract.FeedEntry.COLUMN_TIME_MS + " = ?";
        String[] selectionArgs = {"" + c.getTimeInMillis()};
        db.delete(sqlContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Utilities.setAlarmVolume(old,this);
        vb.cancel();
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
        vb.cancel();

        this.finish();

    }

}
