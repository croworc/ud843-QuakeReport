package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date formatting
 *
 * Created by ingo on 11/11/16.
 */

public final class DateUtil {

    /**
     * Private constructor, so that class can't be instantiated. It just exposes static
     * methods.
     */
    private DateUtil() {
    }

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

    /**
     * Returns a formatted time string from the passed-in Unix timestamp (in milliseconds).
     * The formatting applies the localization for the device's current locale.
     * Example: "23:11" or "11:11 PM"
     *
     * @param timeInMillis  Unix timestamp in milliseconds
     * @return  Formatted time string, like "11:11 PM"
     */
    public static String getSimpleTime(long timeInMillis) {
        // Create a new Date object from the Unix timestamp.
        Date timestamp = new java.util.Date(timeInMillis);

        // Create a new NumberFormat object (with a default format pattern).
        SimpleDateFormat timeFormat = (SimpleDateFormat) DateFormat
                .getTimeInstance();

        // Now we'll change that default pattern according to our need...
        String myFormatPattern = "h:mm a";
        // ...and apply it to the just obtained NumberFormat object.
        timeFormat.applyLocalizedPattern(myFormatPattern);

        // Finally, let's format our Date object with that custom pattern and
        // return the formatted time string.
        return  timeFormat.format(timestamp);

    } // close method getSimpleTime()

} // close class DateUtil
