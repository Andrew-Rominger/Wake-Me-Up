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

public class changedReciver extends BroadcastReceiver
{
    PendingIntent volumePendingIntent;
    public changedReciver()
    {}
    @Override
    public void onReceive(Context context, Intent intent)
    {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);



        if(mgr.getNextAlarmClock() != null)
        {
            if(volumePendingIntent != null)
            {
                volumePendingIntent.cancel();
            }
            Long l = mgr.getNextAlarmClock().getTriggerTime();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context,alarmReciverVolume.class),PendingIntent.FLAG_UPDATE_CURRENT);
            volumePendingIntent = pendingIntent;
            mgr.setExact(AlarmManager.RTC, l+1, pendingIntent);
            Log.d("next alarm changed to ", l.toString());
        }
        else
        {
            Log.i("Was Null", "Error");
            if(volumePendingIntent != null)
            {
                volumePendingIntent.cancel();
                Log.i("Cancled", "no more alarm");
            }
        }




    }
}
