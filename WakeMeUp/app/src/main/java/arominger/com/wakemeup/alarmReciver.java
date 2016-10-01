package arominger.com.wakemeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Andrew on 10/1/2016.
 */

public class alarmReciver extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String vs = sharedPref.getString("pref_alrm", "100%");
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
        Log.v("Surprise", "Alarm");
        am.setStreamVolume(AudioManager.STREAM_ALARM, max, AudioManager.FLAG_PLAY_SOUND);
        Log.i("Set max volume", "Redy to make loud");
    }
}
