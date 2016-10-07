package arominger.com.wakemeup.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import arominger.com.wakemeup.R;
import arominger.com.wakemeup.Fragments.preferenceFragment;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.settingsFrame, new preferenceFragment()).commit();
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            finish();
            return super.onOptionsItemSelected(item);
        }

}

