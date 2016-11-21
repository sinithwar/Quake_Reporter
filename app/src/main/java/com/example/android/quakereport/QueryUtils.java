package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static List<Quake> fetchEarthquakeData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = httpRequestMaker(url);
        } catch (IOException e) {
            Log.e("QueryUtils51", "Error closing input stream", e);
        }
        // Creates a List of Quake objects
        List<Quake> returnJson = extractEarthquakes(jsonResponse);
        // Return the {@link Event}
        return returnJson;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils70", "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Return a list of {@link Quake} objects that has been built up from
     * parsing a JSON response.
     */
    private static String httpRequestMaker (URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnect = null;
        InputStream inputStream = null;
        try {
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setReadTimeout(10000);
            urlConnect.setConnectTimeout(15000);
            urlConnect.setRequestMethod("GET");
            urlConnect.connect();

            if (urlConnect.getResponseCode() == 200){
                inputStream = urlConnect.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e("QueryUtils98", "Code:" + urlConnect.getResponseCode());
            }
        }catch (IOException e){
            Log.e("QueryUtils101", "Code:", e);
        }finally {
            if(urlConnect != null){
                urlConnect.disconnect();
            }
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (IOException e){
                    Log.e("QueryUtils110", "Code:", e);
                }
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Quake> extractEarthquakes(String JSON_RESPONSE) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Quake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            // Takes the String and provides a JSON Object to work with
            JSONObject jsonObject = new JSONObject(JSON_RESPONSE);

            //Traverses the JSON tree to get to the features section
            JSONArray features = jsonObject.getJSONArray("features");


            // Iterates over features array by getting the JSONobjects in that array
            for(int i = 0; i < features.length(); i++){

                JSONObject featuresJSONObject = features.getJSONObject(i);

                // Pulls each properties JSON Object to allow access to variables of Mag, Place, and Time
                JSONObject properties = featuresJSONObject.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                Long time = properties.getLong("time");


                // Extract the time variable as a new Date object
                Date getProperTimeFormatForDisplay = new Date(time);
                // Setup a simple formatter to format the time correctly
                SimpleDateFormat formatTimeForDisplay = new SimpleDateFormat("MM, dd - yyyy");
                // Changes the time to the correct format and returns it as a String
                String displayTime = formatTimeForDisplay.format(getProperTimeFormatForDisplay).toString();


                // Applies a Standard Format to the Magnitude so that solid integers are displayed as doubles
                DecimalFormat formatter = new DecimalFormat("0.0");
                String magOutput = formatter.format(mag);


                // Grab the url from the JSON response
                String url = properties.getString("url");
                // Adds each iteration to the earthquakes Array for iteration over the List View Earthquake Activity
                earthquakes.add(new Quake(magOutput, place, displayTime, url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}