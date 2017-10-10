package com.assetexplorer.utility;

import android.util.Log;

/**
 * Created by Ashish Kadam on 2/16/2017.
 */

public class LogHelper {

    public static boolean LOG_ENABLED = true;

    public static void v(String tag, String message) {
        if (LOG_ENABLED)
            Log.v(tag, message);
    }

    public static void e(String tag, String message) {
        if (LOG_ENABLED) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception ex) {
        if (LOG_ENABLED) {
            Log.e(tag, message, ex);
        }
    }

    public static void e(String tag, String message, Error e) {
        if (LOG_ENABLED)
            Log.e(tag, message, e);
    }

    public static void d(String tag, String message) {
        if (LOG_ENABLED)
            Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        if (LOG_ENABLED)
            Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        if (LOG_ENABLED)
            Log.w(tag, message);
    }

    public static void w(String tag, String message, Exception ex) {
        if (LOG_ENABLED) {
            Log.w(tag, message, ex);
        }
    }

}
