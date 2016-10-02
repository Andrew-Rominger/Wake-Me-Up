package arominger.com.wakemeup;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Andrew on 9/30/2016.
 */

public class timePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    int hourpicked;
    int minutepicked;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int currHour = c.get(Calendar.HOUR_OF_DAY);
        int currMinute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),this,currHour,currMinute, DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        this.hourpicked = hourOfDay;
        this.minutepicked = minute;

        pickerListner pickerlistner = (pickerListner) getActivity();
        pickerlistner.setTimes(hourpicked,minutepicked);
        Log.i("Setting time" , "test");
    }




}
