package com.example.melbournenews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = NewsActivity.class.getSimpleName();

    private String NEWS_BASE_URL = "http://content.guardianapis.com/search?";

    private static final String GUARDIAN_API_KEY = "test";

    private static final int NEWS_LOADER_ID = 1;

    private LoaderManager loaderManager;
    private TextView emptyTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        ArrayList<News> newsList = new ArrayList<>();

        ListView listView = findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(this, newsList);
        listView.setAdapter(newsAdapter);

        emptyTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyTextView);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        populateNewsListUi();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News clickedItem = (News) parent.getItemAtPosition(position);
                if (clickedItem.getWebURL() != null && Patterns.WEB_URL.matcher(clickedItem.getWebURL()).matches()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedItem.getWebURL())));
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.news_url_not_provided_text), Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (loaderManager == null)
                    populateNewsListUi();
                else
                    updateNewsListUi();
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(NEWS_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "melbourne");
        uriBuilder.appendQueryParameter("show-references", "author");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("api-key", GUARDIAN_API_KEY);
        uriBuilder.appendQueryParameter("order-by", "newest");
        Log.i(LOG_TAG, uriBuilder.toString());

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        swipeRefreshLayout.setRefreshing(false);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        emptyTextView.setText("No news to show.");

        newsAdapter.clear();

        if (newsList != null && !newsList.isEmpty())
            newsAdapter.addAll(newsList);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    private void populateNewsListUi() {
        if (isNetworkAvailable()) {
            loaderManager = getLoaderManager();
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void updateNewsListUi() {
        if (isNetworkAvailable()) {
            loaderManager.restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            emptyTextView.setText(getString(R.string.no_internet_connection));
        }
    }
}
