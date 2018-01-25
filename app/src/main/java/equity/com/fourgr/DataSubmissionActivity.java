package equity.com.fourgr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import equity.com.fourgr.config.Variables;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.helper.InternetChecker;
import equity.com.fourgr.model.Submission;
import equity.com.fourgr.server.client.SubmissionClient;
import equity.com.fourgr.server.model.Response;
import equity.com.fourgr.service.GPSTracker;
import equity.com.fourgr.util.FileUtils;
import equity.com.fourgr.util.Picture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DataSubmissionActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;


    Spinner media_size_spinner;

    Button btnUploadPhoto1, btnUploadPhoto2, btnSubmit;
    ImageButton btnChooseDate;
    ImageView imgPhoto1, imgPhoto2;

    EditText txtSubmissionDate, etxFirstname, etxLastname, etxComment, etxRefugeId,etxCamp,etxTown;

    TextView txtPhoto1Path, txtPhoto2Path;


    static final int REQUEST_PHOTO_1_CAPTURE = 100;
    static final int REQUEST_PHOTO_2_CAPTURE = 101;
    String mCurrentPhotoPath;

    GPSTracker gps;

    int year, month, day;


    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_submission);
        etxTown = (EditText) findViewById(R.id.input_town);
        db = new DatabaseHandler(this);

        addListenerOnSpinnerItemSelection();

        btnUploadPhoto1 = (Button) findViewById(R.id.btn_upload_photo_1);
        btnUploadPhoto2 = (Button) findViewById(R.id.btn_upload_photo_2);
        btnChooseDate = (ImageButton) findViewById(R.id.pick_date);
        btnSubmit = (Button) findViewById(R.id.submit_button);

        imgPhoto1 = (ImageView) findViewById(R.id.img_photo_1);
        imgPhoto2 = (ImageView) findViewById(R.id.img_photo_2);

        txtSubmissionDate = (EditText) findViewById(R.id.input_date);
        etxComment = (EditText) findViewById(R.id.input_other_comments);
        etxTown = (EditText) findViewById(R.id.input_town);
        etxCamp = (EditText) findViewById(R.id.input_camp);
        etxFirstname = (EditText) findViewById(R.id.firstname);
        etxLastname = (EditText) findViewById(R.id.lastname);
        etxRefugeId = (EditText) findViewById(R.id.refugeeId);




        txtPhoto1Path = (TextView) findViewById(R.id.photo1path);
        txtPhoto2Path = (TextView) findViewById(R.id.photo2path);


        txtSubmissionDate.setEnabled(false);
        txtSubmissionDate.setFocusable(false);

        getSupportActionBar().setTitle("Refugee Data");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnUploadPhoto1.setOnClickListener(this);
        btnUploadPhoto2.setOnClickListener(this);
        btnChooseDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


        callCalendar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload_photo_1:
                dispatchTakePictureIntent(REQUEST_PHOTO_1_CAPTURE);
                break;
            case R.id.btn_upload_photo_2:
                dispatchTakePictureIntent(REQUEST_PHOTO_2_CAPTURE);
                break;
            case R.id.pick_date:
                showDialog(999);
                break;
            case R.id.submit_button:

                if (InternetChecker.getInstance(context).isNetworkAvailable() == false){

                    saveoffline();
                    Toast.makeText(context, "Submission saved on device. It will be submitted as soon as you go online", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, OfflineDataActivity.class));
                    finish();
                }else{
                    final ProgressDialog pDialog = new ProgressDialog(this);
                    pDialog.setMessage("Uploading Data");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    btnSubmit.setEnabled(false);
                    btnSubmit.setText("Please wait...");
                    submit_data(pDialog);
                }
                break;
        }
    }

    public class MyViewListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            // Code to undo the user's last action
        }
    }

    private void saveoffline(){
        final String photo1_path = txtPhoto1Path.getText().toString();
        final String photo2_path = txtPhoto2Path.getText().toString();

        final Bitmap bmp_photo_1 = decodeFile(photo1_path);
        final Bitmap bmp_photo_2 = decodeFile(photo2_path);

        Uri tempPhoto1Uri = Picture.getImageUri(context, bmp_photo_1);
        Uri tempPhoto2Uri = Picture.getImageUri(context, bmp_photo_2);

        final String real_photo_1_path = getRealPathFromURI(tempPhoto1Uri);
        final String real_photo_2_path = getRealPathFromURI(tempPhoto2Uri);


        Submission submission = new Submission();

        submission.set_submission_date(txtSubmissionDate.getText().toString());
        submission.set_firstname(etxFirstname.getText().toString());
        submission.set_lastname(etxLastname.getText().toString());
        submission.set_town(etxTown.getText().toString());;
        submission.set_other_comments(etxComment.getText().toString());
        submission.set_camp(etxCamp.getText().toString());
        submission.set_refugee_id(etxRefugeId.getText().toString());
        submission.set_spinner1(media_size_spinner.getSelectedItem().toString());
        //submission.set_latitude(String.valueOf(latitude));
        //submission.set_longitude(String.valueOf(longitude));
        submission.set_status(0);

        db.addOfflineSubmission(submission);
    }

    private void submit_data(final ProgressDialog pDialog) {


        final String photo1_path = txtPhoto1Path.getText().toString();
        final String photo2_path = txtPhoto2Path.getText().toString();

        final Bitmap bmp_photo_1 = decodeFile(photo1_path);
        final Bitmap bmp_photo_2 = decodeFile(photo2_path);

        Uri tempPhoto1Uri = Picture.getImageUri(context, bmp_photo_1);
        Uri tempPhoto2Uri = Picture.getImageUri(context, bmp_photo_2);

        final String real_photo_1_path = getRealPathFromURI(tempPhoto1Uri);
        final String real_photo_2_path = getRealPathFromURI(tempPhoto2Uri);



        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("submission_date", createPartFromString(txtSubmissionDate.getText().toString()));
        params.put("firstname", createPartFromString(etxFirstname.getText().toString()));
        params.put("lastname", createPartFromString(etxLastname.getText().toString()));
        params.put("town", createPartFromString(etxTown.getText().toString()));
        params.put("camp", createPartFromString(etxCamp.getText().toString()));
        params.put("refugee_id", createPartFromString(etxRefugeId.getText().toString()));
        params.put("other_comments", createPartFromString(etxComment.getText().toString()));
        params.put("spinner1", createPartFromString(media_size_spinner.getSelectedItem().toString()));
        //params.put("latitude", createPartFromString(String.valueOf(latitude)));
        //params.put("longitude", createPartFromString(String.valueOf(longitude)));


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

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                pDialog.dismiss();
                btnSubmit.setEnabled(true);
                btnSubmit.setText("SUBMIT DATA");
                System.out.println(response.raw());
                if (response.body().isStatus()) {
                    finish();
                    startActivity(getIntent());
//                    System.out.println(response.raw());
                    Toast.makeText(DataSubmissionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    saveoffline();
                    Toast.makeText(DataSubmissionActivity.this, "Could not connect to server. Data stored offline", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(DataSubmissionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                saveoffline();
                Toast.makeText(DataSubmissionActivity.this, "Could not connect to server. Data stored offline", Toast.LENGTH_LONG).show();
            }
        });

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

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void dispatchTakePictureIntent(int code){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Uri photoURI = null;
            if (photoFile != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                photoURI = FileProvider.getUriForFile(this,"equity.com.fourgr.provider", photoFile);
            }else if(photoFile != null && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                photoURI = FileUtils.getUri(photoFile);
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, code);
        }else{
            Toast.makeText(this, "Get Package Manager is Null", Toast.LENGTH_SHORT).show();
        }
    }

    public void callCalendar() {
        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtSubmissionDate.setText(new StringBuilder()
                .append(day).append("/")
                .append(month + 1).append("/")
                .append(year));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 999:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int dayOfMonth) {
            year = selectedYear;
            month = selectedMonth;
            day = dayOfMonth;

            txtSubmissionDate.setText(new StringBuilder()
                    .append(day).append("/")
                    .append(month + 1).append("/")
                    .append(year));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_PHOTO_1_CAPTURE || requestCode == REQUEST_PHOTO_2_CAPTURE){
                if (requestCode == REQUEST_PHOTO_1_CAPTURE){
                    imgPhoto1.setImageBitmap(decodeFile(mCurrentPhotoPath));
                    txtPhoto1Path.setText(mCurrentPhotoPath);
                    if (txtPhoto2Path.getText().toString().equals("")){
                        Toast.makeText(context, "Taking Photo 2", Toast.LENGTH_SHORT).show();
                        btnUploadPhoto2.performClick();
                    }
                }else{
                    imgPhoto2.setImageBitmap(decodeFile(mCurrentPhotoPath));
                    txtPhoto2Path.setText(mCurrentPhotoPath);
                }
            }
        }
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

    public void addListenerOnSpinnerItemSelection() {
        media_size_spinner = (Spinner) findViewById(R.id.spinner1);
        etxComment = (EditText) findViewById(R.id.input_other_comments);
        media_size_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        if(media_size_spinner.getSelectedItem().toString()=="yes" || media_size_spinner.getSelectedItem().toString().equals("yes")){
            etxComment.setVisibility(View.VISIBLE);
        }else{
            etxComment.setVisibility(View.GONE);
        }
    }
}
