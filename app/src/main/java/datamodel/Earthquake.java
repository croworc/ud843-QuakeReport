package datamodel;

/**
 * This class models a single earthquake event, as reported by
 *     http://earthquake.usgs.gov/
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

    /**
     * Creates a new Earthquake object with time of event, place description and the magnitude.
     *
     * @param time      Time of earthquake event in UTC
     * @param place     Textual description of location of event, for closest location w/ more than
     *                  1000 inhabitants, if available
     * @param mag       The magnitude, like 4.5
     */
    public Earthquake(long time, String place, double mag) {
        mTimeInMilliseconds = time;
        mPlace = place;
        mMag = mag;
    }

    long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    String getPlace() {
        return mPlace;
    }

    double getMag() { return mMag; }

} // close class Earthquake
