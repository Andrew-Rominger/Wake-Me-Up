package arominger.com.wakemeup;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

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
        String[] arr = {"10%","20%","30%","40%","50%","60%","70%","80%","90%","100%"};
        CharSequence[] entries = arr;
        CharSequence[] entryValues = arr;
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
        lp.setDefaultValue("100%");

    }
}
