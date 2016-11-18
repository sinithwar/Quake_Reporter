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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URI;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

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
        EarthQuakeAsync task = new EarthQuakeAsync();
        task.execute(JSON_RESPONSE);
    }
    private class EarthQuakeAsync extends AsyncTask<String, Void, ArrayList> {
        // Pass in an url to the AnsycTask so that it can return a string, which is then returned in the even parameter of
        // the Async Task and used by the Post Execute
        @Override
        protected ArrayList doInBackground(String... url) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (url.length < 1 || url[0] == null) {
                return null;
            }
            ArrayList earthquakes = QueryUtils.extractEarthquakes(url[0]);
            return earthquakes;
        }

        @Override
        protected void onPostExecute(ArrayList data) {
            qAdapter.clear();

            if (data != null && !data.isEmpty()){
                qAdapter.addAll(data);
            }
        }
    }
}
