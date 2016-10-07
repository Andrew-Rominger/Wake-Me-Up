package arominger.com.wakemeup;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import arominger.com.wakemeup.Activities.SettingsActivity;
import arominger.com.wakemeup.Classes.Utilities;
import arominger.com.wakemeup.Classes.alarmInList;
import arominger.com.wakemeup.Pickers.datePicker;
import arominger.com.wakemeup.Pickers.pickerListner;
import arominger.com.wakemeup.Pickers.timePicker;
import arominger.com.wakemeup.adapter.recViewAdapter;
import arominger.com.wakemeup.sqlDatabase.alarmDBHelper;


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
    RecyclerView rv;
    recViewAdapter adapter;
    public ListView alarmList;
    ArrayList<Long> list;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         list = Utilities.getAllAlarms(this);


        helper = new alarmDBHelper(this);
        db = helper.getReadableDatabase();

        //Declare Managers


        //Declare Views
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Set Toolbar
        setSupportActionBar(toolbar);
        setUpRecView();


        Log.v(TAG + " LIST SIZE","" + list.size());


    }

    private void setUpRecView()
    {
        rv = (RecyclerView) findViewById(R.id.alarmList);
        adapter = new recViewAdapter(this, alarmInList.getData(this), this);
        rv.setAdapter(adapter);

        LinearLayoutManager lmV = new LinearLayoutManager(this);
        lmV.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lmV);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(TAG, "RESEUME");
        adapter = new recViewAdapter(this, alarmInList.getData(this),this);
        rv.setAdapter(adapter);
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
        Utilities.createNewAlarm(this,newAlarmMinute,newAlarmHour,dayPicked,monthPicked,yearPicked);
        Calendar c = Utilities.createCalender(newAlarmMinute,newAlarmHour,dayPicked,monthPicked,yearPicked);
        Utilities.addAlarmtoList(adapter, newAlarmMinute,newAlarmHour,dayPicked,monthPicked,yearPicked,this, c.getTimeInMillis());



        //Toast.makeText(this, "New alarm: " + sdf3.format(new Date(c.getTimeInMillis())), Toast.LENGTH_LONG).show();


    }

    public void helper(int position)
    {
        ArrayList<alarmInList> l1 = alarmInList.getData(this);
        Utilities.deleteAlarm(this, l1.get(position).getTimeL());
    }





}

