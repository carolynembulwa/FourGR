package equity.com.fourgr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import equity.com.fourgr.adapter.SubmissionAdapter;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Submission;
import equity.com.fourgr.util.ClickListener;
import equity.com.fourgr.util.DividerItemDecoration;
import equity.com.fourgr.util.RecyclerTouchListener;

public class ViewSubmissionsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    final Context context = this;


    FrameLayout loadingLayout, emptyLayout;
    SwipeRefreshLayout dataLayout;
    private RecyclerView recyclerView;

    private SubmissionAdapter adapter;
    List<Submission> submissionList = new ArrayList<Submission>();
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submissions);

        db = new DatabaseHandler(this);

        loadingLayout = (FrameLayout) findViewById(R.id.loading_layout);
        emptyLayout = (FrameLayout) findViewById(R.id.no_submissions);
        dataLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.submission_recycler_view);


        if(submissionList.size() > 0){
            dataLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }else{
            dataLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        adapter = new SubmissionAdapter(this, submissionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Submission submission = submissionList.get(position);
                Intent intent = new Intent(ViewSubmissionsActivity.this, SubmissionActivity.class);
                intent.putExtra("submission_id", String.valueOf(submission.get_id()));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        dataLayout.setOnRefreshListener(this);
        dataLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryLight);

        getSupportActionBar().setTitle("Submissions");
        getSupportActionBar().setSubtitle(submissionList.size() + " submissions");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ViewSubmissionsActivity.this, MainActivity.class));
                finish();
                break;

        }
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onRefresh() {

    }
}
