package arominger.com.wakemeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by Andrew on 9/28/2016.
 */

public class alarmReciver extends  BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        int max = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        am.setStreamVolume(AudioManager.STREAM_ALARM, max, AudioManager.FLAG_PLAY_SOUND);
        Log.i("Set max volume", "Redy to make loud");

    }
}
