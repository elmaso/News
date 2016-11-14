package com.abnd.maso.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by mariosoberanis on 11/11/16. This is the Loader Class to get tha data asyncronoslly
 */

public class NewsLoader extends AsyncTaskLoader<List<GuardianArticle>> {
    private String mUrl;


    /**
     * Construct a new {@link NewsLoader}
     *
     * @param context
     * @param url     we will be feching for the news
     */
    protected NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<GuardianArticle> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a articles from the Guardian.
        List<GuardianArticle> books = QueryUtils.fetchGuardianArticle(mUrl);
        return books;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
