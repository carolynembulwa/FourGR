package equity.com.fourgr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Submission;
import equity.com.fourgr.model.SubmissionPhoto;
import equity.com.fourgr.util.FileUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SubmissionPhotosActivity extends AppCompatActivity implements View.OnClickListener {
    private int HIDE_MENU = 0;
    int submission_id;


    DatabaseHandler db;
    Submission submission;
    GridView gridView;
    CoordinatorLayout coordinatorLayout;
    GridViewAdapter adapter;
    ArrayList<SubmissionPhoto> submissionPhotos;
    Context context = this;
    String mCurrentPhotoPath;
    FloatingActionButton fab;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
    private int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_VIDEO_CAPTURE = 2;
    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView empty_text = (TextView) findViewById(R.id.empty_text);
        mVideoView = (VideoView) findViewById(R.id.video_view);

        gridView = (GridView) findViewById(R.id.photo_grid);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        submission_id = getIntent().getIntExtra("submission_id", 0);
        db = new DatabaseHandler(this);
        submission = db.draftSubmission(submission_id);

        submission_id = getIntent().getIntExtra("submission_id", 0);
        db = new DatabaseHandler(this);
        submission = db.draftSubmission(submission_id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String title = "Monitoring Photos";
        getSupportActionBar().setTitle(title);

        submissionPhotos = db.getSubmissionPhotos(submission_id);

        adapter = new GridViewAdapter(this);
        gridView.setAdapter(adapter);

        updateUI();

        fab.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.monitoring_photos_menu, menu);
        if (HIDE_MENU == 0){
            menu.findItem(R.id.action_add_photo).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(SubmissionPhotosActivity.this, DataSubmission.class);
                intent.putExtra("submission_id", submission_id);

                startActivity(intent);
                break;
            case R.id.action_add_photo:
                if (mayRequestWriteFilePermissions()) {
                    final View view = gridView;
                    gridView.post(new Runnable() {
                        @Override
                        public void run() {
                                dispatchTakePictureIntent();
                        }
                    });

                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Intent intent = new Intent(SubmissionPhotosActivity.this, SubmissionReviewActivity.class);
                intent.putExtra("submission_id", submission_id);
                startActivity(intent);
                break;
        }
    }


    public class GridViewAdapter extends BaseAdapter {
        Activity activity;
        public GridViewAdapter(Activity a){
            this.activity = a;
        }

        @Override
        public int getCount() {
            return submissionPhotos.size();
        }

        @Override
        public Object getItem(int i) {
            return submissionPhotos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            View row = view;
            ViewHolder holder = null;

            if (row == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                row = inflater.inflate(R.layout.upload_photo_item, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) row.findViewById(R.id.upload_image);
                holder.txtRemove = (TextView) row.findViewById(R.id.remove_image);
                holder.txtPosition = (TextView) row.findViewById(R.id.position);

                holder.txtPosition.setText(String.valueOf(position));
                final ViewHolder finalHolder = holder;
                row.setTag(holder);


            }else{
                holder = (ViewHolder) row.getTag();
            }

            SubmissionPhoto photo = submissionPhotos.get(position);
            File imgFile = new File(photo.getThumb());
            if (imgFile.exists()){
                BitmapFactory.Options mOptions = new BitmapFactory.Options();
                mOptions.inSampleSize = 4;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), mOptions);
                holder.image.setImageBitmap(myBitmap);
                holder.txtRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                    }
                });
            }
            return row;
        }

        public void removeItem(int position){
            DatabaseHandler db = new DatabaseHandler(context);
            SubmissionPhoto upload = submissionPhotos.get(position);
            if(upload.getId() != 0) {
                File file = new File(upload.getPath());
                if (file.exists()){
                    try {
                        File thumbFile = new File(upload.getThumb());
                        thumbFile.delete();
                        file.delete();
                        Toast.makeText(activity, "File deleted", Toast.LENGTH_SHORT).show();
                    }catch(Exception ex){
                        Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                boolean deleted = db.deletePhotoByID(upload.getId());
                if (deleted == false) {
                    Toast.makeText(activity, "Could not delete", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            submissionPhotos.remove(upload);
            adapter.notifyDataSetChanged();
            updateUI();
        }
    }

    public boolean mayRequestWriteFilePermissions(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)){
            Snackbar.make(coordinatorLayout, "Storage Permissions are needed to take the photos", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        }
                    }).show();
        }else{
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(coordinatorLayout, "Storage Permissions are needed to take the photos", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                                }
                            }
                        }).show();
            }else{
                dispatchTakePictureIntent();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Uri photoURI = null;
            if (photoFile != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                photoURI = FileProvider.getUriForFile(this,"equity.com.fourgr.provider", photoFile);
            }else if(photoFile != null && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                photoURI = FileUtils.getUri(photoFile);
            }

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void updateUI(){
        LinearLayout no_photos = (LinearLayout) findViewById(R.id.no_photos);
        int imagecount = 2;
        if (submissionPhotos.size() < imagecount) {
            fab.setVisibility(View.GONE);
            this.HIDE_MENU = 1;
        } if (submissionPhotos.size() == imagecount) {
            fab.setVisibility(View.VISIBLE);
            this.HIDE_MENU = 0;
        }

        if (submissionPhotos.size() == 0) {
            no_photos.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            this.HIDE_MENU = 1;
            invalidateOptionsMenu();
            return;
        }
        no_photos.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
        return;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            SubmissionPhoto photo = new SubmissionPhoto();
            Bitmap thumbnailPhoto = decodeFile(mCurrentPhotoPath);
            Uri tempUri = getImageUri(this, thumbnailPhoto);
            File finalThumbnailFile = new File(getRealPathFromURI(tempUri));

            photo.setSubmission_id(submission_id);
            photo.setThumb(finalThumbnailFile.getAbsolutePath());
            photo.setPath(mCurrentPhotoPath);
            photo.setId(db.addSubmissionPhoto(photo));

            submissionPhotos.add(photo);
//            submissionPhotos = db.getSubmissionPhotos(submission_id);
            adapter.notifyDataSetChanged();
            updateUI();
        }else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(videoUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            SubmissionPhoto photo = new SubmissionPhoto();

            photo.setSubmission_id(submission_id);
            photo.setThumb(picturePath);
            photo.setPath(getRealPathFromURI(videoUri));
            photo.setId(db.addSubmissionPhoto(photo));
            submissionPhotos.add(photo);
            mVideoView.setVideoURI(videoUri);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 300;

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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static class ViewHolder {
        ImageView image;
        TextView txtRemove, txtPosition;
    }
}
