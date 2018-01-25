package equity.com.fourgr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.MapItem;
import equity.com.fourgr.model.Submission;

public class ViewInMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    private ClusterManager<MapItem> mClusterManager;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_in_map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("DATA ON MAP");

        db = new DatabaseHandler(this);

        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpCluster();
    }

    private void setUpCluster(){
        mClusterManager = new ClusterManager<MapItem>(this, mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        List<Submission> submissions = new ArrayList<Submission>();



    }
}
