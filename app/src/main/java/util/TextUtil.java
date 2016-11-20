package util;

import android.content.Context;

import com.example.android.quakereport.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some static helper methods for isolating the location"s offset and the location itself
 * for display in separate TextViews.
 *
 * Created by ingo on 11/13/16.
 */

public final class TextUtil {

    /**
     * This is a string pattern for matching substrings within an earthquake location string that
     * defines an offset from the first populated town w/ more than 1000 inhabitants.
     *
     * That offset substring might look like "58 km SW of" or "112 km WNW of".
     *
     * The complete "place" string, as delivered within the events properties, might look like:
     * "58 km SW of San Francisco, CA" or
     * "112 km WNW of Charagua, Bolivia"
     */
    private static final String OFFSET_PATTERN = "^\\d{1,4}km \\p{javaUpperCase}{1,3} of";

    /** Private constructor, as we won't allow for instantiating objects of this class.
     *  We're just exposing static methods.
     */
    private TextUtil() {}

    /**
     * Returns a String that depicts the offset in km to the {@link datamodel.Earthquake} 's location.
     * This information is contained within the earthquake's location string ("place" attribute).
     * If *no* offset was provided, the place's string will be prefixed with "Near the".
     * Example:
     *      "place" = "58km SW of San Francisco, CA"
     *      returns: "58 km SW of"
     *
     *      "place" = "Pacific Rim"
     *      returns: "Near the Pacific Rim"
     *
     * @param place the earthquake's location string (its "place" attribute)
     * @return  the offset substring, like "58 km SW of", or
     *          "Near the" if no offset was provided
     */
    public static String getLocationOffset(Context context, String place) {

        String strLocationOffset;

        // Create a Matcher object to match our predefined offset pattern against the place string.
        Matcher matcher = Pattern.compile(OFFSET_PATTERN).matcher(place);

        // Does the "place" string contain such an offset pattern, like "58km SW of"?
        if (matcher.find()) {
            // extract that offset descriptor string
            strLocationOffset = matcher.group();

            // For the aesthetics: let's insert a space between the km value and the "km" string.
            //   - find the index of the "km" substring
            int kmIndex = strLocationOffset.indexOf("km");
            //  - and glue together the parts, again, inserting a space in between.
            strLocationOffset = strLocationOffset.substring(0, kmIndex)
                    + " " + strLocationOffset.substring(kmIndex);
        } else {
            // the place string doesn't contain an offset descriptor; set offset to the
            // string resource "Near the"
            strLocationOffset = context.getString(R.string.near_the);
        }

        return strLocationOffset;
    } // close method getLocationOffset()


    /**
     * Returns a string that contains the earthquake's location with the offset stripped off.
     * In cases where no offset was provided, like in "Pacific Rim" that value will be returned
     * as-is.
     *
     * @param place The earthquake's location attribute, as received from the JSON string.
     * @return  The pure location, with the offset (if any) stripped off.
     */
    public static String getLocation(String place) {
        String strLocation;

        // Create a Matcher object to match our predefined offset pattern against the place string.
        Matcher matcher = Pattern.compile(OFFSET_PATTERN).matcher(place);

        // Does the "place" string contain such an offset pattern, like "58km SW of"?
        if (matcher.find()) {
            // This "place" string does indeed contain an offset substring.
            // So we can obtain the isolated location string as a substring of "place",
            // starting at position:  matcher.end() +1
            // matcher.end() returns the next position right *after* the matcher's end (that's
            // a space character) and +1 shifts the position right to the first index of the real
            // location.
            strLocation = place.substring(matcher.end() + 1);

        } else {
            // the place string doesn't contain an offset descriptor;
            // just return the "place" string.
            strLocation = place;
        }

        return strLocation;
    } // close method getLocation()


    public static String formatMagnitude(Double mag) {
        NumberFormat formatter = NumberFormat.getInstance();
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).applyPattern("0.0");
        }
        return formatter.format(mag);
    } // close method formatMagnitude


} // close class TextUtil
