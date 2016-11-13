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
    /**
     * Returns a formatted date string from the passed-in Unix timestamp (in milliseconds).
     * The formatting applies the localization for the devices current locale.
     * Example: "Jan 01, 1970"
     *
     * @param timeInMillis  Unix timestamp in milliseconds
     * @return  Formatted date string, like "Feb 14, 1965"
     */
    public static String getSimpleDate(long timeInMillis) {
        // Create a new Date object from the Unix timestamp.
        Date timestamp = new java.util.Date(timeInMillis);

        // Create a new NumberFormat object (with a default format pattern).
        SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat
                .getDateInstance(DateFormat.MEDIUM);

        // Now we'll change that default pattern according to our need...
        String myFormatPattern = "MMM dd, yyyy";
        // ...and apply it to the just obtained NumberFormat object.
        dateFormat.applyLocalizedPattern(myFormatPattern);

        // Finally, let's format our date with that custom pattern and
        // return the formatted date string.
        return  dateFormat.format(timestamp);

    } // close method getSimpleDate()

} // close class DateUtil
