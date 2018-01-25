package equity.com.fourgr.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;


public class Manager {
    public static final String SESSION_PREF_NAME = "session";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_FIRSTNAME = "firstname";
    public static final String KEY_USER_LASTNAME = "lastname";

    private static Context context;
    private static Manager mInstance;

    public Manager(Context ctx){
        this.context = ctx;
    }

    public static synchronized Manager getInstance(Context context){
        if (mInstance == null){
            mInstance = new Manager(context);
        }

        return mInstance;
    }


    public void clear(){
        SharedPreferences preferences = this.context.getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
