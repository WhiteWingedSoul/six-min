package com.sphoton.hoangviet.sixmin.managers;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by hoangviet on 10/11/16.
 */
public class APIManager {

    public static final String SERVER_NAME = "http://sixmin.sphoton.com";
    public static final String API_BASE = "/api/v1/";
    public static final String API_GETTOPIC = SERVER_NAME+API_BASE+"topics";
    public static final String API_GETPOST = "/posts";

    public static void GETAllTopics(final Callback callback){

        new AsyncTask<Void, Void, Void>() {
            OkHttpClient client = new OkHttpClient();
            Request request;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                request = new Request.Builder()
                        .url(API_GETTOPIC)
                        .build();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                client.newCall(request).enqueue(callback);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public static void GETPost(final String topicName, final Callback callback){
        new AsyncTask<Void, Void, Void>() {
            OkHttpClient client = new OkHttpClient();
            Request request;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                request = new Request.Builder()
                        .url(SERVER_NAME+API_BASE+topicName+API_GETPOST)
                        .build();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                client.newCall(request).enqueue(callback);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}
