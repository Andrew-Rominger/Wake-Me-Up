package arominger.com.wakemeup.Recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import arominger.com.wakemeup.Activities.alarmRinging;

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
