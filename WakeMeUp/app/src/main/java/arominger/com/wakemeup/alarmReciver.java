package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Andrew on 10/1/2016.
 */

public class alarmReciver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        context.startActivity(new Intent(context, alarmRinging.class));

    }

}
