package arominger.com.wakemeup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Andrew on 10/1/2016.
 */

public class pingService extends Service {
    private static final String TAG = pingService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {


        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG, intent.getAction());
        return super.onStartCommand(intent, flags, startId);
    }
}
