package equity.com.fourgr.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import equity.com.fourgr.app.MyApp;
import equity.com.fourgr.helper.InternetChecker;


public class NetworkReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras()!=null)
        {
            NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED)
            {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                MyApp.getBus().post(new InternetChecker(true) );
                // there is Internet connection
            } else
                MyApp.getBus().post(new InternetChecker(false) );
            if(intent .getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE))
            {
                // no Internet connection, send network state changed
                Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();
                MyApp.getBus().post(new InternetChecker(false) );
                //EventBus.getDefault().post(new NetworkStateChanged(false));
            }
        }
    }

}
