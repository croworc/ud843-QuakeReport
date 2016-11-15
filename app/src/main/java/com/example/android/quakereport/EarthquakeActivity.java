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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import datamodel.Earthquake;
import datamodel.EarthquakeAdapter;
import util.QueryUtil;

public class EarthquakeActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a static list of earthquake locations.
        ArrayList<Earthquake> earthquakes = QueryUtil.extractEarthquakes();

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        // Attach the OnItemClickListener
        earthquakeListView.setOnItemClickListener(this);

    } // close method onCreate()

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Send an intent for opening a browser app to go the the web page the
        // current earthquake links to.

        // Get the current earthquake object from the adapter.
        Earthquake event = (Earthquake) adapterView.getItemAtPosition(i);

        // Fetch the url (may also be the empty string, because no url was provided)
        String url = event.getUrl();

        // If a URL was provided, build an intent to open a browser app and load that URL.
        if (url.length() != 0) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else { // we don't have a URL, so just inform the user.
            Toast.makeText(this, "no URL provided", Toast.LENGTH_SHORT).show();
        }

    } // close method onItemClick()

} // close class EarthquakeActivity
