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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import datamodel.Earthquake;
import datamodel.EarthquakeAdapter;
import util.QueryUtil;

import static android.R.attr.data;
import static android.R.attr.format;

public class EarthquakeActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener{

    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String QUERY_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?"
            + "format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    ListView          mEarthquakeListView;
    EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find and hold a reference to the {@link ListView} in the layout.
        mEarthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mEarthquakeListView.setAdapter(mAdapter);

        // Attach the OnItemClickListener to the ListView.
        mEarthquakeListView.setOnItemClickListener(this);

        // Fire-up the background task which does the JSON fetching.
        DownloadTask fetchJsonTask = new DownloadTask();
        fetchJsonTask.execute(QUERY_URL);

    } // close method onCreate()


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


    private class DownloadTask extends AsyncTask<String, Void, List<Earthquake>> {
        @Override
        protected List<Earthquake> doInBackground(String... urls) {

            // If no URL has been passed-in, or the URL string is the empty string, return early.
            if (urls.length == 0
                    || urls[0] == null
                    || urls[0].length() == 0) {return null;}

            return QueryUtil.fetchEarthquakeData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {

            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (earthquakes != null && !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
            }
        } // close method onPostExecute()

    } // close inner class DownloadTask

} // close class EarthquakeActivity
