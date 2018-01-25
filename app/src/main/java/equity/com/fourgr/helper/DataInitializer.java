package equity.com.fourgr.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import equity.com.fourgr.R;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Menu;


public class DataInitializer {
    private static Context context;
    private static Activity activity;
    private static DataInitializer mInstance;
    DatabaseHandler db;
    boolean SHOW_PROGRESS_FLAG = false;
    ProgressDialog progressDialog;

    public DataInitializer(Context context, Boolean showProgress){
        this.context = context;
        this.db = new DatabaseHandler(context);
        this.SHOW_PROGRESS_FLAG = showProgress;
        if (SHOW_PROGRESS_FLAG == true) {
            progressDialog = new ProgressDialog(context);
        }
    }

    public static synchronized DataInitializer getInstance(Context context, boolean showProgress){
        if (mInstance == null){
            mInstance = new DataInitializer(context, showProgress);
        }

        return mInstance;
    }

    public void initialize(){
        db.clearDatabase();
            // Add Menu Items
            setupMenuMain();

    }


    public void setupMenuMain(){
        Menu menu = new Menu();

        menu = new Menu();
        menu.set_menu_title("View Offline submissions ");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("offline_data_action");
        menu.set_menu_users("Admin");
        db.addMainMenu(menu);

        menu = new Menu();
        menu.set_menu_title("Add new data");
        menu.set_menu_icon(R.mipmap.ic_view);
        menu.set_menu_slug("enter_data_action");
        menu.set_menu_users("Admin");
        db.addMainMenu(menu);


        menu = new Menu();
        menu.set_menu_title("View Draft Submissions");
        menu.set_menu_icon(R.mipmap.ic_view);
        menu.set_menu_slug("draft_data_action");
        menu.set_menu_users("Admin");
        db.addMainMenu(menu);

    }




}
