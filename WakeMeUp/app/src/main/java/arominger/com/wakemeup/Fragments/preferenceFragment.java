package arominger.com.wakemeup.Fragments;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import arominger.com.wakemeup.R;

/**
 * Created by Andrew on 10/1/2016.
 */

public class preferenceFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefrences);

        final ListPreference lp = (ListPreference) findPreference("pref_alrm");
        final EditTextPreference snoozeLength = (EditTextPreference) findPreference("pref_snooze") ;
        String[] arr = {"10%","20%","30%","40%","50%","60%","70%","80%","90%","100%"};
        CharSequence[] entries = arr;
        CharSequence[] entryValues = arr;
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
        lp.setDefaultValue("100%");
        snoozeLength.setDefaultValue("10");

    }
}
