package mc.com.geopplaces.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import mc.com.geopplaces.R;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isTablet(Context context){
        return context.getResources().getBoolean(R.bool.isTablet);
    }

}
