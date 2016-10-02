package arominger.com.wakemeup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Andrew on 10/1/2016.
 */
public class AlarmPlayingService extends Service
{
    private static final String TAG = AlarmPlayingService.class.getSimpleName();

    MediaPlayer mp;
    NotificationManager nf;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        nf = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.v(TAG, "In service");
        mp = MediaPlayer.create(this,R.raw.gunshot);
        mp.setLooping(true);
        mp.start();
        Intent dismissIntent = new Intent(this, pingService.class);
        dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, pingService.class);
        dismissIntent.setAction(CommonConstants.ACTION_snooze);
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Waking you up...")
                .setContentText("Time to get up!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("TEST"))
                .addAction(R.drawable.ic_dismiss, "Dismss", piDismiss)
                .addAction(R.drawable.ic_snooze, "Snooze", piSnooze);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent p = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(p);





        nf.notify(0, builder.build());

        return START_NOT_STICKY;
    }

}
