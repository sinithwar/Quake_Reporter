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

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Quake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    /** Sample JSON response for a USGS query */
    private static final String JSON_RESPONSE = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private QuakeAdapter qAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        qAdapter = new QuakeAdapter(this, new ArrayList<Quake>());

        final ListView earthquakeListView = (ListView) findViewById(R.id.earthquake_view);

        earthquakeListView.setAdapter(qAdapter);

        // Set up ClickListener event to handle the user click
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current earthquake that was clicked on
                Quake currentEarthquake = qAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Instructor failed to make a catch call to check if the string was null, so I added one
                if (earthquakeUri != null){
                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
    }

    @Override
    public Loader<List<Quake>> onCreateLoader(int i, Bundle bundle){
        Log.v("Create Loader", "Successful");
        return new QuakeLoader(this, JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<List<Quake>> loader, List<Quake> data) {
        qAdapter.clear();

        if (data != null && !data.isEmpty()){
            qAdapter.addAll(data);
        }
        Log.v("Load Finished", "Successful");
    }

    @Override
    public void onLoaderReset(Loader<List<Quake>> loader){
        qAdapter.clear();
    }
}
