package com.example.android.quakereport;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Tommy on 11/18/2016.
 */
public class QuakeLoader extends AsyncTaskLoader<List<Quake>> {

    private static final String LOG_TAG = QuakeLoader.class.getSimpleName();

    private String jsonUrl;

    public QuakeLoader(Context context, String url){
        super(context);
        this.jsonUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Quake> loadInBackground() {
        if (jsonUrl == null){
            return null;
        }
        List<Quake> earthquake = QueryUtils.fetchEarthquakeData(jsonUrl);
        return  earthquake;
    }
}
