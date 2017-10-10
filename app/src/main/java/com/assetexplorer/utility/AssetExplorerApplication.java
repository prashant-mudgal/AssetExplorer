package com.assetexplorer.utility;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Prashant Mudgal on 05/18/2017.
 */


public class AssetExplorerApplication extends Application {
    public static final String TAG = AssetExplorerApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private Gson mGson;
    private static GoogleAnalytics analytics;
    private static AssetExplorerApplication mInstance;
    private static Tracker tracker;


    public static GoogleAnalytics analytics() {
        return analytics;
    }

    public static Tracker tracker() {
        return tracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(Constants.ANALYTICS_TRACKER_ID);

        mInstance = this;
    }

    public static synchronized AssetExplorerApplication getInstance() {
        return mInstance;
    }

    /**
     * @return returns Gson singleton object with LOWER_CASE_WITH_UNDERSCORES jsonKeys
     */
    public Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
        }
        return mGson;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelAllApi() {
        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
