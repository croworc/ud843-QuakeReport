/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import datamodel.Earthquake;
import datamodel.EarthquakeAdapter;
import datamodel.EarthquakeLoader;


public class EarthquakeActivity extends AppCompatActivity
        implements  AdapterView.OnItemClickListener,
                    LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private static final int EARTHQUAKE_LOADER = 0;

    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query";
            // + "format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    ListView          mEarthquakeListView;  // The ListView
    TextView          mEmptyView;           // A TextView to show when there's no data to display
    ProgressBar       mProgressBar;         // Indeterminate spinner for displaying while data fetching
    EarthquakeAdapter mAdapter;             // Custom adapter for handling Earthquake objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d(LOG_TAG, "onCreate() called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // When this app gets started for the very first time:
        // Initialize the preferences with their default values (as defined in preferences.xml)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Find and hold a reference to the {@link ListView} in the layout.
        mEarthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link EarthquakeAdapter} that takes an empty list of earthquakes as input.
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface.
        mEarthquakeListView.setAdapter(mAdapter);

        // Attach the OnItemClickListener to the ListView.
        mEarthquakeListView.setOnItemClickListener(this);

        // Set the empty view on the ListView
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mEarthquakeListView.setEmptyView(mEmptyView);

        // Fetch a reference to the indeterminate spinning progress bar, so that we can
        // disable it when the data loading has finished.
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Check the network connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() ) { // we're connected, so let's go
            // Show the spinning indeterminate progress bar.
            mProgressBar.setVisibility(View.VISIBLE);

            // Initialize an EarthquakeLoader, which will be responsible for fetching the
            // JSON data from the web.
            // Log.d(LOG_TAG, "initLoader() called...");
            getLoaderManager().initLoader(EARTHQUAKE_LOADER, null, this);

        } else { // not currently connected
            // Hide the spinning indeterminate progress bar.
            mProgressBar.setVisibility(View.GONE);

            // Show message in the empty view for checking internet connection
            mEmptyView.setText(R.string.empty_view_text_nointernet);
        }

    } // close method onCreate()


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu main.xml
        getMenuInflater().inflate(R.menu.main, menu);

        // Call through to the superclass
        return super.onCreateOptionsMenu(menu);
    } // close method onCreateOptionsMenu()


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Which menu item has been clicked?
        switch (item.getItemId()) {

            // The "Settings" menu item got clicked...
            case R.id.action_settings:
                // ...so we'll start the settings activity
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_about:
                // TODO: 11/23/16 Implementation required.
                Toast.makeText(this, "Not currently implemented, sorry!", Toast.LENGTH_SHORT)
                        .show();
                return true;

            default:
                return  super.onOptionsItemSelected(item);
        } // close switch

    } // close method onOptionsItemSelected()


    /**
     * When an item (row) in the ListView gets clicked, we'll send an implicit intent for
     * starting a browser app to view this earthquake's detail data on the web.
     *
     * @param adapterView   The ListView
     * @param view          The current row's root view (a LinearLayout)
     * @param i             The current row's ID
     * @param l             The backing ArrayList's current data index
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Send an intent for opening a browser app to go the the web page the
        // current earthquake links to.

        // Get the current earthquake object from the adapter.
        Earthquake event = (Earthquake) adapterView.getItemAtPosition(i);

        // Fetch the url (may also be the empty string, because no url might have been provided)
        String url = event.getUrl();

        // If a URL was provided, build an intent to open a browser app and load that URL.
        if (url.length() > 0) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else { // we don't have a URL, so just inform the user.
            Toast.makeText(this, "No earthquake URL available", Toast.LENGTH_SHORT).show();
        }

    } // close method onItemClick()


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        // Log.d(LOG_TAG, "onCreateLoader() called...");

        // Read the user's latest preferences for the
        //   - "minimum magnitude", the
        //   - "order-by" option (magnitude / time of event) and the
        //   - "number of events to fetch".
        //
        // Construct a proper URI with their preferences and finally
        // create a new Loader for that URI.
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the "Minimum Magnitude" preference.
        String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));

        // Get the "Order-By" preference.
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        // Get the "Number of events to fetch" preference value (its an int)...
        int numberEventsValue = sharedPrefs.getInt(
                getString(R.string.settings_number_events_key),
                Integer.parseInt(getString(R.string.settings_number_events_default)));
        // ...and parse it to a String, to be used in the URL.
        String numberEvents = String.valueOf(numberEventsValue);

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", numberEvents);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        // Create and return a new EarthquakeLoader. Pass in this activity's context and
        // the newly constructed URL.
        return new EarthquakeLoader(this, uriBuilder.toString());
    } // close method onCreateLoader()


    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // Log.d(LOG_TAG, "onLoadFinished() called...");

        // Clear the adapter of previous earthquake data.
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }

        // Hide the spinning progress bar
        mProgressBar.setVisibility(View.GONE);

        // On the empty view: set the text to display when no earthquake data was found.
        mEmptyView.setText(R.string.empty_view_text_nodata);

    } // close method onLoadFinished()


    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Log.d(LOG_TAG, "onLoaderReset() called...");

        // Clear the adapter of previous earthquake data.
        mAdapter.clear();
    }

} // close class EarthquakeActivity
