package datamodel;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.android.quakereport.R;

/**
 * This class models a single earthquake event, as reported by
 *     http://earthquake.usgs.gov/
 *
 * Provides helper methods to categorize an earthquake's magnitude and for obtaining a
 * color resource ID that pertains to such a given magnitude category;
 *
 * Created by ingo on 11/10/16.
 */

public class Earthquake {

    /** Unix timestamp (in milliseconds) of event */
    private long mTimeInMilliseconds;

    /** Descriptive location of the closest town w/ more than 1000 inhabitants */
    private String mPlace;

    /** The magnitude, like 4.5 */
    private double mMag;

    /** This earthquake's URL at the USGS web page */
    private String mUrl;

    /**
     * Creates a new Earthquake object with time of event, place description and the magnitude.
     *
     * @param time      Time of earthquake event in UTC
     * @param place     Textual description of location of event, for closest location w/ more than
     *                  1000 inhabitants, if available
     * @param mag       The magnitude, like 4.5
     */
    public Earthquake(long time, String place, double mag, String url) {
        mTimeInMilliseconds = time;
        mPlace = place;
        mMag = mag;
        mUrl = url;
    }

    long   getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    String getPlace() {
        return mPlace;
    }

    double getMag() { return mMag; }

    public String getUrl() { return mUrl; }

    /**
     * Returns the color RGB integer value that's associated with a given, passed-in magnitude
     * value.
     *
     * @param context   The EarthquakeAdapter
     * @param mag       The earthquake's magnitude; a double value like 2.9
     * @return          A color RGB value, that's associated with the passed-inmagnitude
     */
    static int getMagnitudeColor(Context context, double mag) {

        // The color resource ID that we're trying to obtain.
        int magnitudeColorResId;

        // Use Math.floor to round the magnitude value to the next double that's less than or equal
        // to the passed-in double. Casting to (int) gives us the magnitude's category value
        // that we'll use within the switch statement to fetch the associated color resource.
        // Example: 2.9 ==> 2
        int magnitudeFloor = (int) Math.floor(mag);

        // Fetch the color resource ID that's associated with a given magnitude category.
        switch (magnitudeFloor) {
            case 0: // magnitude >= 0 and < 2
            case 1:
                magnitudeColorResId = R.color.magnitude0_2;
                break;
            case 2: // magnitude >= 2 and < 3
                magnitudeColorResId =  R.color.magnitude2_3;
                break;
            case 3: // magnitude >= 3 and < 4
                magnitudeColorResId =  R.color.magnitude3_4;
                break;
            case 4: // magnitude >= 4 and < 5
                magnitudeColorResId =  R.color.magnitude4_5;
                break;
            case 5: // magnitude >= 5 and < 6
                magnitudeColorResId =  R.color.magnitude5_6;
                break;
            case 6: // magnitude >= 6 and < 7
                magnitudeColorResId =  R.color.magnitude6_7;
                break;
            case 7: // magnitude >= 7 and < 8
                magnitudeColorResId =  R.color.magnitude7_8;
                break;
            case 8: // magnitude >= 8 and < 9
                magnitudeColorResId =  R.color.magnitude8_9;
                break;
            case 9: // magnitude >= 9 and < 10
                magnitudeColorResId =  R.color.magnitude9_10;
                break;
            case 10: // magnitude >= 10
                magnitudeColorResId =  R.color.magnitude10;
                break;
            default: // magnitude < 0 ??
                magnitudeColorResId =  R.color.magnitude_undefined;
                break;
        } // close switch

        // Return the int color RGB value that pertains to the obtained color resource ID.
        return ContextCompat.getColor(context, magnitudeColorResId);

    } // close getMagnitudeColor()


} // close class Earthquake
