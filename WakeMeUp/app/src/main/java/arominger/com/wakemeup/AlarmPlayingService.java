package arominger.com.wakemeup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

/**
 * Created by Andrew on 10/1/2016.
 */
public class AlarmPlayingService extends Service {
    private static final String TAG = AlarmPlayingService.class.getSimpleName();

    MediaPlayer mp;
    NotificationManager nf;
    Random r;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        r = new Random();
        int sound = r.nextInt(5);

        switch (sound)
        {
            case 0:
                mp = MediaPlayer.create(this, R.raw.gunshot);
                break;
            case 1:
                mp = MediaPlayer.create(this, R.raw.dogbark);
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.glass1);
                break;
            case 3:
                mp = MediaPlayer.create(this, R.raw.glass2);
                break;
            case 4:
                mp = MediaPlayer.create(this, R.raw.knocking);
                break;
            default:
                mp = MediaPlayer.create(this, R.raw.gunshot);
                break;
        }

        Log.v(TAG, "In service");

        mp.setLooping(true);
        mp.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {   mp.stop();
        Log.v(TAG, "DESTROYINGH");
        mp.setLooping(false);
        super.onDestroy();
    }
}


