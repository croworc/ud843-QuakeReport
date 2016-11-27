package datamodel;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.quakereport.EarthquakeActivity;

import java.util.List;

import util.QueryUtil;

/**
 * Custom loader for fetching earthquake data in the background.
 * Utilizes class QueryUtil.java for doing the hard work...
 *
 * Created by ingo on 11/21/16.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    /**
     * Holds the URL to the USGS website for fetching the JSON data, which
     * has been passed into the constructor.
     */
    private String mUrlString;

    /**
     * Constructs a new EarthquakeLoader with the calling activity as the context and
     * a URL in string representation.
     *
     * @param context   The calling activity
     * @param urlString The URL with the USGS query rerquest, in string representation
     */
    public EarthquakeLoader(Context context, String urlString) {
        super(context);
        mUrlString = urlString;
    } // close constructor


    @Override
    protected void onStartLoading() {
        // Log.d(LOG_TAG, "onStartLoading() called...");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        // Log.d(LOG_TAG, "loadInBackground() called...");

        // If no URL string has been passed into the constructor, or if the URL string
        // is the empty string, there's nothing to do right now. So just log that condition
        // and return early.
        if (mUrlString == null || mUrlString.length() == 0) {
            // Log.d(LOG_TAG, "in loadInBackground(): mUrlString is null or empty");
            return null;
        }
        // Call helper method for fetching the earthquakes as a List<Earthquake> and return
        // them.
        return QueryUtil.fetchEarthquakeData(mUrlString);
    } // close method loadInBackground()


    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
    }

    @Override
    public boolean isStarted() {
        return super.isStarted();
    }

} // close class EarthquakeLoader
