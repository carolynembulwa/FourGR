package equity.com.fourgr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.squareup.otto.Bus;

import java.util.List;

import equity.com.fourgr.adapter.RecyclerViewAdapter;
import equity.com.fourgr.app.MyApp;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.helper.DataInitializer;
import equity.com.fourgr.model.Menu;
import com.microsoft.appcenter.AppCenter; 
import com.microsoft.appcenter.analytics.Analytics; 
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {
    Bus bus = MyApp.getBus();

    List<Menu> menuList;
    DatabaseHandler db;

    String[] perms = { "android.permission.WRITE_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }


        RecyclerView rv = (RecyclerView)findViewById(R.id.menu_recycler_view);
        GridLayoutManager llm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(llm);

        db = new DatabaseHandler(this);
        menuList = db.Mainmenu();
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(menuList, this, this);
        rv.setAdapter(adapter);
        
        AppCenter.start(getApplication(), "518611cd-2ee9-4ade-a2c4-87c172b78875", Analytics.class, Crashes.class);
    }

    public void initialize(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
//        db.clearDatabase();
        if (db.getmainMenuCount() == 0 ){
            DataInitializer.getInstance(context, false).setupMenuMain();
        }else{
           // startActivity(new Intent(context, MainActivity.class));
            //finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.offline_data:
                startActivity(new Intent(this, OfflineDataActivity.class));
                break;

            case R.id.draft_submission:
                startActivity(new Intent(this, DraftSubmissionsActivity.class));
                break;
        }
        return true;
    }


}
