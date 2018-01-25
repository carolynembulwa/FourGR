package equity.com.fourgr;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.fragments.SubmissionDetailsFragment;
import equity.com.fourgr.fragments.SubmissionPhotosFragment;
import equity.com.fourgr.model.Submission;

public class SubmissionActivity extends AppCompatActivity implements SubmissionDetailsFragment.OnFragmentInteractionListener, SubmissionPhotosFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String _id;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Submission submissionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        this._id = this.getIntent().getStringExtra("submission_id");
        DatabaseHandler db = new DatabaseHandler(this);
        submissionData = db.getSubmission(Integer.parseInt(this._id));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Submissions");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextView title = (TextView) findViewById(R.id.app_name);

        //title.setText("Submission");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    bundle.putParcelable("data", submissionData);

                    SubmissionDetailsFragment fragment = new SubmissionDetailsFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                   // bundle.putDouble("latitude", Double.parseDouble(submissionData.get_latitude()));
                   // bundle.putDouble("longitude", Double.parseDouble(submissionData.get_longitude()));

                   // SubmissionLocationFragment submissionLocationFragment = new SubmissionLocationFragment();
                   // submissionLocationFragment.setArguments(bundle);
                   // return submissionLocationFragment;
                case 2:
                    bundle.putString("photo_1", submissionData.get_photo_1());
                    bundle.putString("photo_2", submissionData.get_photo_2());
                    bundle.putInt("submission_id", submissionData.get_id());

                    SubmissionPhotosFragment submissionPhotosFragment = new SubmissionPhotosFragment();
                    submissionPhotosFragment.setArguments(bundle);

                    return submissionPhotosFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    return "MAP";
                case 2:
                    return "PHOTOS";
            }
            return null;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter{
        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new SubmissionDetailsFragment();
               // case 1:
                   // return new SubmissionLocationFragment();
                case 1:
                    return new SubmissionPhotosFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
