package com.example.importantdays.utils;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class NetworkUtils {
    private static final String LOG_TAG =
            NetworkUtils.class.getSimpleName();

    private static String BASE_URL = "";

    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";

    private static HashMap<String, String> queryParms = new HashMap<>();

    public NetworkUtils(String baseUrl) {
        this.BASE_URL = baseUrl;
    }

    public void addQueryParm(String key, String value) {
        queryParms.put(key, value);
    }

    public String getInfo() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;
        try {
            Uri.Builder uriBuilder = Uri.parse(BASE_URL).buildUpon();
            for(String key : queryParms.keySet()) {
                uriBuilder = uriBuilder.appendQueryParameter(key, queryParms.get(key));
            }
            Uri builtURI = uriBuilder.build();
//            Uri builtURI = Uri.parse(BASE_URL).buildUpon()
//                    .appendQueryParameter(QUERY_PARAM, queryString)
//                    .appendQueryParameter(MAX_RESULTS, "10")
//                    .appendQueryParameter(PRINT_TYPE, "books")
//                    .build();
            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if(builder.length() == 0) {
                return null;
            }
            bookJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        Log.d(LOG_TAG, bookJSONString);

        return bookJSONString;
    }

}

