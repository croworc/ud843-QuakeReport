package util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import datamodel.Earthquake;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtil {

    private static final String LOG_TAG = QueryUtil.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtil} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtil (and an object instance of QueryUtil is not needed).
     */
    private QueryUtil() {
    }


    /**
     * Query the USGS dataset and return an ArrayList of {@link Earthquake} objects.
     */
    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {

        // Log.d(LOG_TAG, "fetchEarthquakeData() called... ");

/*        // Simulate bad internet connection
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response for each {@link datamodel.Earthquake}
        // object and put all of them into an ArrayList.
        // Return that {@link ArrayList<datamodel.Earthquake>}
        return extractEarthquakes(jsonResponse);

    } // close method fetchEarthquakeData()


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    } // close method createUrl()


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * This string contains the JSON data with the list of earthquakes.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream(); // get an input stream for the bytes...
                jsonResponse = readFromStream(inputStream);   // ...and pass it on to a helper method
                                                              // for further processing (converting
                                                              // into the JSON string it is).
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            } // close try block

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } // close try/catch/finally

        // We're returning the jsonResponse string no matter whether or not
        // there had been an error. So it may just be the empty string.
        // It will be up to the calling method to deal with that.
        return jsonResponse;

    } // close method makeHttpRequest()

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            // Connect a character converter (InputStreamReader) to the input stream...
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, Charset.forName("UTF-8"));

            //...and wrap it into a buffered reader, so that we can later on read line-by-line.
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Which we do just here, within a loop, until there are no more lines in the buffer.
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        } // done with: if our input stream wasn't null

        return output.toString();

    } // close method readFromStream()


    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<Earthquake> extractEarthquakes(String jsonString) {

        // Create an empty ArrayList that we can start adding earthquakes to.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the JSON string passed-in. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Access the root of the JSON string.
            JSONObject root = new JSONObject(jsonString);
            // Log.d(LOG_TAG, "JSON string: " + root.toString());

            // Get the earthquakes array (node "features").
            JSONArray features = root.getJSONArray("features");

            // Loop through all earthquakes, extract their relevant information (magnitude,
            // location and date/time of event, create a new Earthquake object and add it to
            // the ArrayList.
            for (int i = 0; i < features.length() ; i++) {
                // Get the current earthquake event.
                JSONObject event = features.getJSONObject(i);

                // Get the "properties" JSONObject, describing this earthquake's, well, properties.
                JSONObject properties = event.getJSONObject("properties");

                // Extract magnitude, location, time and url of event.
                double mag   = properties.getDouble("mag");
                String place = properties.getString("place");
                long   time  = properties.getLong("time");
                String url   = properties.optString("url");

                // Create a new Earthquake object and add it to the ArrayList of earthquakes.
                Earthquake earthquake = new Earthquake(time, place, mag, url);
                earthquakes.add(earthquake);

            } // close for loop (iterating over all earthquakes)

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtil", "Problem parsing the earthquake JSON results", e);

        } // close try/catch block

        // Return the list of earthquakes
        return earthquakes;

    } // close method extractEarthquakes()

} // close class QueryUtil