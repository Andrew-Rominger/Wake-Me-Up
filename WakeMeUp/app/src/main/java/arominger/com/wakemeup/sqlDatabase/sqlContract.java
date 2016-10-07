package arominger.com.wakemeup.sqlDatabase;

import android.provider.BaseColumns;

/**
 * Created by Andrew on 10/4/2016.
 */

public class sqlContract
{
    private sqlContract(){};

    public static class FeedEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "alarms";
        public static final String COLUMN_TIME_MS = "timeInMs";
    }
}
