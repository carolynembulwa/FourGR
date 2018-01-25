package equity.com.fourgr;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.fragments.ReviewDataFragment;
import equity.com.fourgr.fragments.ReviewPhotoFragment;
import equity.com.fourgr.model.Submission;
import equity.com.fourgr.provider.SubmissionProvider;

public class SubmissionReviewActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ReviewDataFragment.OnFragmentInteractionListener, ReviewPhotoFragment.OnFragmentInteractionListener {
    int submission_id;


    private TabLayout tabLayout;
    private ViewPager viewPager;;
    DatabaseHandler db;

    Submission submission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new DatabaseHandler(this);

        getSupportActionBar().setTitle("Refugee");
        getSupportActionBar().setSubtitle("Review submission");


        submission_id = getIntent().getIntExtra("submission_id", 0);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        tabLayout.addTab(tabLayout.newTab().setText("Monitoring Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);


        fab.setVisibility(View.VISIBLE);


        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                        ContentValues values = new ContentValues();

                        values.put(DatabaseHandler.KEY_STATUS, 0);

                        getContentResolver().update(SubmissionProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                        Toast.makeText(SubmissionReviewActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(SubmissionReviewActivity.this, MainActivity.class));
                        finish();

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_edit:
                    Intent intent = new Intent(this, DataSubmission.class);
                    intent.putExtra("submission_id", submission_id);
                    startActivity(intent);
                    break;
            }

        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class Pager extends FragmentStatePagerAdapter{
        int tabCount = 1;
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("submission_id", submission_id);

             switch (position) {
                    case 0:
                        ReviewDataFragment reviewDataFragment = new ReviewDataFragment();
                        reviewDataFragment.setArguments(bundle);
                        return reviewDataFragment;
                    case 1:
                        ReviewPhotoFragment reviewPhotoFragment = new ReviewPhotoFragment();
                        reviewPhotoFragment.setArguments(bundle);
                        return reviewPhotoFragment;

                    default:
                        return null;
                }


        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }





}
