package equity.com.fourgr.app;

import android.app.*;

import com.squareup.otto.*;

public class MyApp extends Application {
    private static Bus mEventBus;

    public static Bus getBus(){
        return mEventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = new Bus(ThreadEnforcer.ANY);
    }
}
