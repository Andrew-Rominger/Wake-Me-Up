package arominger.com.wakemeup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Andrew on 10/1/2016.
 */

public class datePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    int year;
    int month;
    int day;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int currYear = c.get(Calendar.YEAR);
        int currMonth= c.get(Calendar.MONTH);
        int currDay = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,currYear,currMonth,currDay);

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;

        pickerListner pickerlistner = (pickerListner) getActivity();
        pickerlistner.setDates(day,this.month,this.year);
        Log.i("Setting date" , "");
    }
}
