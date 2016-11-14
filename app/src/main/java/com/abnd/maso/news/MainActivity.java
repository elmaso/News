package com.abnd.maso.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<GuardianArticle>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    //Connection Variables
    //// TODO: 11/11/16 Add Share preferences to edit sizes, include tags, making for a better UX...
    private static final String REQUEST_URL = "http://content.guardianapis.com/search?q=android&api-key=test&page-size=20";
    private int NEWS_LOADER_ID = 1;
    private ConnectivityManager cm;
    private NetworkInfo networkInfo;
    private LoaderManager lm;

    //UI Variables
    private NewsFeedAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsArticleView = (ListView) findViewById(R.id.news_list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mLoadingIndicator = findViewById(R.id.loading_indicator);

        mAdapter = new NewsFeedAdapter(this, new ArrayList<GuardianArticle>());
        newsArticleView.setAdapter(mAdapter);

        //Set a click listener to each article so we can link to the full story
        newsArticleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                GuardianArticle currArticle = mAdapter.getItem(position);
                Uri webUrl = Uri.parse(currArticle.getWebUrl());

                //we check for null or bad urls
                if (webUrl == null || TextUtils.isEmpty(currArticle.getWebUrl())) {
                    Toast.makeText(MainActivity.this, "Sorry this Article link is missing", Toast.LENGTH_LONG).show();
                } else {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webUrl);
                    startActivity(webIntent);
                }

            }

        });


        // Get a reference to the ConnectivityManager to check state of network connetivity
        cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        networkInfo = cm.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            lm = getLoaderManager();
            lm.initLoader(NEWS_LOADER_ID, null, this);

            //Starting a new search lest update the UI
            mAdapter.clear();
            mEmptyStateTextView.setText(R.string.refreshing_news);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.error_no_internet);
        }


    }

    @Override
    public Loader<List<GuardianArticle>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader");
        return new NewsLoader(this, REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<GuardianArticle>> loader, List<GuardianArticle> data) {
        mLoadingIndicator.setVisibility(View.GONE);
        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);

        } else {
            //Set empty state text to display "No News Found".
            mEmptyStateTextView.setText(R.string.error_no_news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<GuardianArticle>> loader) {
        mAdapter.clear();

    }
}
