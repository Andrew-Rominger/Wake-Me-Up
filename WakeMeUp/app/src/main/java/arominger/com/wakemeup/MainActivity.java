package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements pickerListner
{

    alarmDBHelper helper;
    SQLiteDatabase db;

    private static final String TAG = MainActivity.class.getSimpleName();
    private int newAlarmHour;
    private int newAlarmMinute;
    private int dayPicked;
    private int monthPicked;
    private int yearPicked;
    public ListView alarmList;
    ArrayList<Long> list;

    alarmListAdaptor adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         list = Utilities.getAllAlarms(this);
        adapter = new alarmListAdaptor();

        helper = new alarmDBHelper(this);
        db = helper.getReadableDatabase();

        //Declare Managers


        //Declare Views
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        alarmList = (ListView) findViewById(R.id.alarmList);

        //Set Toolbar
        setSupportActionBar(toolbar);


        Log.v(TAG + " LIST SIZE","" + list.size());
        populateListView();

    }

    @Override
    protected void onResume()
    {
        this.updateList();
        super.onResume();
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
        Utilities.createNewAlarm(this,newAlarmMinute,newAlarmHour,dayPicked,monthPicked,yearPicked,TAG);
        updateList();
    }

    public void populateListView()
    {
            alarmList.setAdapter(adapter);
    }
    private void updateList()
    {
        list = Utilities.getAllAlarms(this);
        adapter = new alarmListAdaptor();
        alarmList.setAdapter(adapter);
    }

    public class alarmListAdaptor extends ArrayAdapter<Long>
    {

        alarmListAdaptor()
        {
            super(MainActivity.this, R.layout.alarminlist, list);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
           View itemView = convertView;
            if (itemView ==null)
            {
                itemView = getLayoutInflater().inflate(R.layout.alarminlist, parent, false);
            }
            final long alarmLong = list.get(position);
            TextView timeInList = (TextView) itemView.findViewById(R.id.timeList);
            TextView dateInList = (TextView) itemView.findViewById(R.id.dateList);
            ImageView deleteAlarm = (ImageView) itemView.findViewById(R.id.deleteAlarm);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(alarmLong);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            timeInList.setText(sdf.format(c.getTime()));
            dateInList.setText(sdf2.format(c.getTime()));

            deleteAlarm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Utilities.deleteAlarm(MainActivity.this, alarmLong);
                    MainActivity.this.updateList();

                }
            });

            return itemView;

        }
    }


}

