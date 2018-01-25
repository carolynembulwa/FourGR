package equity.com.fourgr.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetChecker {
    private static Context context;
    private static InternetChecker mInstance;
    public boolean mIsInternetConnected;
    public InternetChecker(Context context){ this.context = context; }

    public InternetChecker(boolean isInternetConnected){
        this.mIsInternetConnected = isInternetConnected;
    }

    public static synchronized InternetChecker getInstance(Context context){
        if (mInstance == null){
            mInstance = new InternetChecker(context);
        }
        return mInstance;
    }

    public boolean isInternetConnected(){
        return this.mIsInternetConnected;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        this.mIsInternetConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return this.mIsInternetConnected;
    }
}
