package com.example.melbournenews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Member variables
     */
    private String url;

    /**
     * Constructor
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    /**
     * Overridden methods
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<News> news = QueryUtils.fetchNews(url);
        return news;
    }
}
