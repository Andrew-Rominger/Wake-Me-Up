package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Andrew on 10/1/2016.
 */

public class alarmReciver extends BroadcastReceiver
{
    MediaPlayer mp;
    AudioManager am;
    int max;
    float f;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.d("TESTING", "Alarm sounding");
        //Toast.makeText(context, "BIG TEST", Toast.LENGTH_LONG).show();
        am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String vs = sharedPref.getString("pref_alrm", "100%");
        if(vs.length() == 3)
        {
            vs =  vs.substring(0,2);
            vs = "." + vs;
            f = Float.parseFloat(vs);
            max = (int) (am.getStreamMaxVolume(AudioManager.STREAM_ALARM) * f);

        }
        else
        {
            max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        }

        Intent serviceIntent = new Intent(context, AlarmPlayingService.class);

        context.startService(serviceIntent);


    }

}
