package datamodel;

/**
 * This class models a single earthquake event, as reported by
 *     http://earthquake.usgs.gov/
 *
 * Created by ingo on 11/10/16.
 */

public class Earthquake {

    /** Unix mTime stamp of event */
    private long mTime;

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
        mTime = time;
        mPlace = place;
        mMag = mag;
    }

    long getTime() {
        return mTime;
    }

    String getPlace() {
        return mPlace;
    }

    double getMag() { return mMag; }

} // close class Earthquake
