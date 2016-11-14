package com.abnd.maso.news;

import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariosoberanis on 11/11/16.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<GuardianArticle> fetchGuardianArticle(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<GuardianArticle> guardianArticles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link GuardianArticle}
        return guardianArticles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            // If the request was succesful (response code 200),
            //then read the infput stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                Log.i(LOG_TAG, "Got good ResponseCode");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
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

    /**
     * Return a list of {@link GuardianArticle} build from parsing JSON
     */

    public static List<GuardianArticle> extractFeatureFromJson(String newJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can add articles to it
        List<GuardianArticle> articleList = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(newJSON);

            JSONObject response = baseJsonObject.getJSONObject("response");

            JSONArray result = response.getJSONArray("results");

            for (int i = 0; i < result.length(); i++) {
                JSONObject newsObject = result.getJSONObject(i);
                String sectionName = newsObject.getString("sectionName");
                String webPublicationDate = newsObject.getString("webPublicationDate");
                String webTitle = newsObject.getString("webTitle");
                String webUrl = newsObject.getString("webUrl");

                GuardianArticle article = new GuardianArticle(sectionName, webPublicationDate, webTitle, webUrl);
                articleList.add(article);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return articleList;
    }

}
