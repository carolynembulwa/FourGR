package equity.com.fourgr;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import equity.com.fourgr.adapter.OfflineSubmissionsAdapter;
import equity.com.fourgr.config.Variables;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Submission;
import equity.com.fourgr.server.client.SubmissionClient;
import equity.com.fourgr.util.DividerItemDecoration;
import equity.com.fourgr.util.FileUtils;
import equity.com.fourgr.util.Picture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfflineDataActivity extends AppCompatActivity {
    Context context = this;
    RecyclerView offlineRecyclerView;
    List<Submission> submissionList = new ArrayList<Submission>();
    OfflineSubmissionsAdapter offlineSubmissionsAdapter;
    int upload = 0;
    DatabaseHandler db;
    LinearLayout emptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data);

        db = new DatabaseHandler(context);

        offlineRecyclerView = (RecyclerView) findViewById(R.id.all_offline_submissions);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        offlineRecyclerView.setLayoutManager(mLayoutManager);
        offlineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        offlineRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        emptyLayout = (LinearLayout) findViewById(R.id.empty);

        submissionList = db.allOfflineSubmissions();
        updateUI();
        offlineSubmissionsAdapter = new OfflineSubmissionsAdapter(context, submissionList);
        offlineRecyclerView.setAdapter(offlineSubmissionsAdapter);
        getSupportActionBar().setTitle("Offline Submission");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offline_submission_menu, menu);
        return true;
    }

    private void updateUI(){
        if (submissionList.size() > 0){
            offlineRecyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            return;
        }
        offlineRecyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int notification_id = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void submitOfflineData(final Submission s, final int index){
        String url = Variables.BASE_URL + "API/addSubmission";
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("submission_date", createPartFromString(s.get_submission_date()));
        params.put("town", createPartFromString(s.get_town()));
        params.put("other_comments", createPartFromString(s.get_other_comments()));
        //params.put("latitude", createPartFromString(s.get_latitude()));
        //params.put("longitude", createPartFromString(s.get_longitude()));
        params.put("firstname", createPartFromString(s.get_firstname()));
        params.put("lastname", createPartFromString(s.get_lastname()));
        params.put("camp", createPartFromString(s.get_camp()));
        params.put("refugee_id", createPartFromString(s.get_refugee_id()));
        params.put("spinner", createPartFromString(s.get_spinner1()));



        final Bitmap bmp_photo_1 = decodeFile(s.get_photo_1());
        final Bitmap bmp_photo_2 = decodeFile(s.get_photo_2());

        Uri tempPhoto1Uri = Picture.getImageUri(context, bmp_photo_1);
        Uri tempPhoto2Uri = Picture.getImageUri(context, bmp_photo_2);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Variables.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        SubmissionClient client = retrofit.create(SubmissionClient.class);
        Call<equity.com.fourgr.server.model.Response> call = client.createSubmission(
                params,
                prepareFilePart("photo_1", tempPhoto1Uri),
                prepareFilePart("photo_2", tempPhoto2Uri)
        );

        call.enqueue(new Callback<equity.com.fourgr.server.model.Response>() {
            @Override
            public void onResponse(Call<equity.com.fourgr.server.model.Response> call, retrofit2.Response<equity.com.fourgr.server.model.Response> response) {
                if (response.body().isStatus()) {
                    db.deleteOfflineSubmission(s.get_id());
                    submissionList.remove(index);
                    offlineRecyclerView.removeViewAt(index);
                    offlineSubmissionsAdapter.notifyItemRemoved(index);
                    offlineSubmissionsAdapter.notifyItemRangeChanged(index, submissionList.size());
                    offlineSubmissionsAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(OfflineDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<equity.com.fourgr.server.model.Response> call, Throwable t) {
                Toast.makeText(OfflineDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (submissionList.size() > 0 && upload == 1){
            Snackbar.make(findViewById(R.id.offline_coordinator_layout), R.string.cannot_go_back,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }else{
            super.onBackPressed();
        }
    }

    @NonNull
    private RequestBody createPartFromString(String string){
        return RequestBody.create(
                MultipartBody.FORM, string
        );
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        File file = FileUtils.getFile(this, fileUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1000;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }
}
