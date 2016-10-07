package arominger.com.wakemeup.Classes;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Andrew on 10/6/2016.
 */

public class alarmInList
{
    private long timeL;
    private String time;
    private String date;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeL() {
        return timeL;
    }

    public void setTimeL(long timeL) {
        this.timeL = timeL;
    }

    public static ArrayList<alarmInList> getData(Context context)
    {
        Calendar c;
        ArrayList<Long> list = Utilities.getAllAlarms(context);
        ArrayList<alarmInList> dataList = new ArrayList<>();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
        for(long l: list)
        {
            c=Calendar.getInstance();
            c.setTimeInMillis(l);
            alarmInList alarm = new alarmInList();
            alarm.setTime(sdf2.format(c.getTime()));
            alarm.setDate(sdf1.format(c.getTime()));
            alarm.setTimeL(l);
            dataList.add(alarm);
            c.clear();
        }
        return dataList;
    }
}
