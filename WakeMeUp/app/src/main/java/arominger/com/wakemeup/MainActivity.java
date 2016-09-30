package arominger.com.wakemeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity
{
    ImageView arrow;
    Random rand = new Random();
    public PendingIntent pendingIntent;
    boolean IS_ON;
    public AlarmManager mgr;
    MainActivity ma = this;
    changedReciver cr;
    TextView isOn;
    String[] stringarr = {
            "No More Missed Meetings!",
            "Ready to wake up on time?",
            "Dont forget breakfast!",
            "Get some rest!",
            "Dont miss that midterm!",
            "Wake up bright and early!",
            "Where did that sheep come from?",
            "Time for some ZZZ's!",
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        IS_ON = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isOn = (TextView) findViewById(R.id.mainIsOn);
        isOn.setText(R.string.strIsNotOn);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrow = (ImageView) findViewById(R.id.arrow_button);
        final RotateAnimation rotateOnClick = new RotateAnimation(0,180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateOnClick.setDuration(300);
        rotateOnClick.setInterpolator(new LinearInterpolator());
        rotateOnClick.setFillBefore(true);

        final Drawable onToOff = getDrawable(R.drawable.arrow);
        final Drawable offToOn = getDrawable(R.drawable.ic_arrow_on);

        rotateOnClick.setAnimationListener(new Animation.AnimationListener()
        {
            Drawable df;
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(IS_ON)
                {
                    df = onToOff;
                    //Toast.makeText(MainActivity.this,"is on is true", Toast.LENGTH_SHORT).show();
                    IS_ON = false;
                    isOn.setText(R.string.strIsNotOn);
                    arrow.setImageDrawable(df);
                }
                else {
                    df = offToOn;
                    IS_ON = true;
                    //Toast.makeText(MainActivity.this,"is on is false", Toast.LENGTH_SHORT).show();
                    int n = rand.nextInt(stringarr.length-1);
                    String toBed = getString(R.string.strIsRunning) + " " + stringarr[n];
                    isOn.setText(toBed);
                    arrow.setImageDrawable(df);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(IS_ON)
                {
                    Log.i("unregistering", "Was on");
                    ma.unregisterReceiver(ma.cr);
                }
                else
                {
                    Log.i("register", "Was off");
                    ma.cr = new changedReciver(ma);
                    ma.registerReceiver(cr, new IntentFilter(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ma.registerReceiver(cr, new IntentFilter(AlarmClock.ACTION_DISMISS_ALARM));
                    }
                }


                arrow.startAnimation(rotateOnClick);

            }
        });
    }

    public MainActivity() {
        super();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.action_gotosettings:


        }
        return true;
    }



}
