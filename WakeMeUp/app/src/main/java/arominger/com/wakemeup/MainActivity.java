package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements pickerListner
{


    private static final String TAG = MainActivity.class.getSimpleName();
    ImageView arrow;
    Random rand = new Random();
    PendingIntent pendingIntent;


    boolean IS_ON;
    public AlarmManager mgr;
    AlarmManager manager;

    MainActivity ma = this;
    changedReciver cr;
    TextView isOn;
    String[] stringarr = {
            "No More Missed Meetings!",
            "Ready to wake up on time?",
            "Dont forget breakfast!",
            "Get some rest!",
            "Dont miss that midterm!",
            "Wake up bright and early!",
            "Where did that sheep come from?",
            "Time for some ZZZ's!",
    };
    private int newAlarmHour;
    private int newAlarmMinute;
    private int dayPicked;
    private int monthPicked;
    private int yearPicked;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AudioManager am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String vs = sharedPref.getString("pref_alrm", "100%");
        Log.i("max alarm:", vs);
        int max;
        if(vs.length() == 3)
        {
            vs =  vs.substring(0,2);
            vs = "." + vs;
            float f = Float.parseFloat(vs);
            max = (int) (am.getStreamMaxVolume(AudioManager.STREAM_ALARM) * f);

        }
        else
        {
            max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        }
        Log.i("Max:", "" + max);
        IS_ON = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare Managers
       mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);;


        //Declare Views
        isOn = (TextView) findViewById(R.id.mainIsOn);
        arrow = (ImageView) findViewById(R.id.arrow_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Drawable arrowW = getDrawable(R.drawable.arrow);



        //Set Toolbar
        setSupportActionBar(toolbar);

        //Animations
        final RotateAnimation rotateToTop = new RotateAnimation(0,180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateToTop.setDuration(300);
            rotateToTop.setInterpolator(new AccelerateInterpolator());
            rotateToTop.setFillAfter(true);
            rotateToTop.setAnimationListener(new Animation.AnimationListener() {
            Drawable df;
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                String text = getResources().getString(R.string.strIsRunning) + " " + stringarr[rand.nextInt(stringarr.length - 1)];
                isOn.setText(text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    arrowW.setTint(getResources().getColor(R.color.colorAccent,  null));
                }
                //arrow.animate().rotationBy(180f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
                arrow.setImageDrawable(arrowW);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        final RotateAnimation rotateToBottom = new RotateAnimation(180,0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateToBottom.setDuration(300);
            rotateToBottom.setInterpolator(new AccelerateInterpolator());
            rotateToBottom.setFillAfter(true);
            rotateToBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isOn.setText(R.string.strIsNotOn);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    arrowW.setTint(getResources().getColor(R.color.colorPrimaryDark,  null));
                }
                arrow.setImageDrawable(arrowW);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Set Values
        isOn.setText(R.string.strIsNotOn);
        assert arrowW != null;
        Drawable mDrawableArrow = arrowW.mutate();
        mDrawableArrow = DrawableCompat.wrap(mDrawableArrow);




        //Arrow color change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            DrawableCompat.setTint(mDrawableArrow, getResources().getColor(R.color.colorPrimaryDark, null));
        }
        arrow.setImageDrawable(arrowW);


        arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(IS_ON)
                {
                    Log.i("unregistering", "Was on");
                    //ma.unregisterReceiver(cr);
                    MainActivity.this.arrow.startAnimation(rotateToBottom);
                    IS_ON = false;
                }
                else
                {

                    if(mgr.getNextAlarmClock() != null)
                    {
                        Log.i(TAG, "Next alarm at " + mgr.getNextAlarmClock().getTriggerTime());
                    }

                    cr = new changedReciver();
                    registerReceiver(cr, new IntentFilter(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        registerReceiver(cr, new IntentFilter(AlarmClock.ACTION_DISMISS_ALARM));
                    }

                    MainActivity.this.arrow.startAnimation(rotateToTop);
                    IS_ON = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()){
            case R.id.action_gotosettings:
                Log.i("settings","Settings");
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            default:

                break;


        }
        return true;
    }


    public void makeNewAlarm(MenuItem item)
    {
        DialogFragment datePicker = new datePicker();
        datePicker.show(getFragmentManager(), "datePicker");
        DialogFragment timePicker = new timePicker();
        timePicker.show(getFragmentManager(), "picker");

    }

    @Override
    public void setTimes(int hourPicked, int minutePicked)
    {
        this.newAlarmHour = hourPicked;
        this.newAlarmMinute = minutePicked;
    }

    @Override
    public void setDates(int dayPicked, int monthPicked, int yearPicked)
    {
        this.dayPicked = dayPicked;
        this.monthPicked = monthPicked;
        this.yearPicked = yearPicked;
        createNewAlarm(newAlarmMinute,newAlarmHour,dayPicked,monthPicked,yearPicked);

    }
    private void createNewAlarm(int minute, int hour, int day, int month, int year)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa", Locale.US);
        Calendar c = Calendar.getInstance();
        PendingIntent pendingIntent;
        Log.i(TAG, "Hour: " + hour);
        Log.i(TAG, "Minute: " + minute);
        Log.i(TAG, "Day: " + day);
        Log.i(TAG, "Month: " + month);
        Log.i(TAG, "Year: " + year);


        c.set(Calendar.YEAR,year);
        c.set(Calendar.DAY_OF_YEAR, day);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.MONTH, month - c.get(Calendar.MONTH));

        Intent i = new Intent(this, alarmReciver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2, i,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "New alarm: " + sdf.format(new Date(c.getTimeInMillis())), Toast.LENGTH_LONG).show();


    }
}

