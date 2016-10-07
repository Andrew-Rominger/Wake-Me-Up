package arominger.com.wakemeup.Recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import arominger.com.wakemeup.Services.bootUpService;

/**
 * Created by Andrew on 10/6/2016.
 */

public class bootUpReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Intent serviceIntent = new Intent(context, bootUpService.class);
            context.startService(serviceIntent);
        }
    }
}
