package arominger.com.wakemeup.Pickers;

/**
 * Created by Andrew on 10/1/2016.
 */

public interface pickerListner
{
    public void setTimes(int hourPicked, int minutePicked);
    public void setDates(int dayPicked, int monthPicked, int yearPicked);
}
