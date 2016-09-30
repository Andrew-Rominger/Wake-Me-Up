package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by Andrew on 9/29/2016.
 */

public class changedReciver extends BroadcastReceiver {
    MainActivity ma;
    public changedReciver()
    {}
    public changedReciver(MainActivity ma)
    {
        this.ma = ma;
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(am.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
        {
            am.setStreamVolume(AudioManager.STREAM_ALARM, 0,AudioManager.FLAG_PLAY_SOUND);
            Log.i("Set volume to min", "min");
        }


        if(mgr.getNextAlarmClock() != null)
        {
            if(ma.pendingIntent != null)
            {
                ma.pendingIntent.cancel();
            }
            Long l = mgr.getNextAlarmClock().getTriggerTime();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ma, 0, new Intent(ma,alarmReciver.class),PendingIntent.FLAG_UPDATE_CURRENT);
            ma.pendingIntent = pendingIntent;
            ma.mgr.setExact(AlarmManager.RTC_WAKEUP, l+1,pendingIntent);
            Log.d("next alarm changed to ", l.toString());
        }
        else
        {
            Log.i("Was Null", "Error");
            if(ma.pendingIntent != null)
            {
                ma.pendingIntent.cancel();
                Log.i("Cancled", "no more alarm");
            }
        }




    }
}
