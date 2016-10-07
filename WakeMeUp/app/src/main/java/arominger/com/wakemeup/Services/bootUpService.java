package arominger.com.wakemeup.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

import arominger.com.wakemeup.Classes.Utilities;
import arominger.com.wakemeup.Recivers.alarmReciver;

/**
 * Created by Andrew on 10/6/2016.
 */

public class bootUpService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        ArrayList<Long> list = Utilities.getAllAlarms(this);
        for(Long l : list)
        {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(l);
            PendingIntent pi =  PendingIntent.getBroadcast(this, 2, new Intent(this, alarmReciver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP, l,pi);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
