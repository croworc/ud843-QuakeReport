package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date formatting
 *
 * Created by ingo on 11/11/16.
 */

public class DateUtil {
    public static String getSimpleDate(long timeInMillis) {
        // Create a new Date object from the Unix timestamp which will then be formatted.
        Date timestamp = new java.util.Date(timeInMillis);

        // Create a new NumberFormat object with a default format pattern.
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat
                .getDateInstance(DateFormat.MEDIUM);

        // Now we'll change that default pattern according to our desire...
        String myFormatPattern = "MMM dd, yyyy";
        // ...and apply it to the just obtained dateFormat.
        dateFormat.applyLocalizedPattern(myFormatPattern);

        // Finally, let's format our date with that custom pattern and return
        // the formatted date string.
        return  dateFormat.format(timestamp);

    } // close getSimpleDate()

} // close class DateUtil
