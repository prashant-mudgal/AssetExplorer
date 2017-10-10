package com.assetexplorer.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.assetexplorer.R;

public class AppUtils {
    private AppUtils() {
    }

    public static boolean isInternetConnected(Context context) {
        boolean networkConnected = isNetworkAvailable(context);
        if (!networkConnected) {
            Toast.makeText(context, context.getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
        }
        return networkConnected;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static <T extends Enum<T>> T stringToEnum(Class<T> enumClass,
                                                     String value) {
        if (value == null)
            return null;
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
