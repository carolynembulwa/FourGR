package equity.com.fourgr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.Calendar;
import java.util.Formatter;

import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Submission;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class DataSubmission extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler db;

    int submission_id;

    EditText txtSubmissionDate, etxFirstname, etxLastname, etxComment, etxRefugeId,etxCamp,etxTown;

    Button btnContinue;


    Spinner media_size_spinner;
    int day, month, year;


    Formatter f = new Formatter();
    Context context = this;

    Submission submission;
    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_data_submission);

        mAwesomeValidation = new AwesomeValidation(BASIC);
        addListenerOnSpinnerItemSelection();

        db = new DatabaseHandler(this);

        submission_id = getIntent().getIntExtra("submission_id", 0);

        if (submission_id == 0) {
        }else{
            submission = db.draftSubmission(submission_id);
        }



        getSupportActionBar().setTitle("Monitoring Refugee Data");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        txtSubmissionDate = (EditText) findViewById(R.id.input_date);
        etxComment = (EditText) findViewById(R.id.input_other_comments);
        etxTown = (EditText) findViewById(R.id.input_town);
        etxCamp = (EditText) findViewById(R.id.input_camp);
        etxFirstname = (EditText) findViewById(R.id.firstname);
        etxLastname = (EditText) findViewById(R.id.lastname);
        etxRefugeId = (EditText) findViewById(R.id.refugeeId);




        btnContinue = (Button) findViewById(R.id.continue_btn);

//valiidation
        mAwesomeValidation.addValidation(this, R.id.input_town, RegexTemplate.NOT_EMPTY, R.string.err_town);
        mAwesomeValidation.addValidation(this, R.id.input_camp, RegexTemplate.NOT_EMPTY, R.string.err_camp);
        mAwesomeValidation.addValidation(this, R.id.firstname, RegexTemplate.NOT_EMPTY, R.string.err_firstname);
        mAwesomeValidation.addValidation(this, R.id.lastname, RegexTemplate.NOT_EMPTY, R.string.err_lastname);
        btnContinue.setOnClickListener(this);

        txtSubmissionDate.setEnabled(false);
        txtSubmissionDate.setFocusable(false);

        if (submission_id != 0){
            txtSubmissionDate.setText(submission.get_submission_date());
            etxCamp.setText(submission.get_camp());
            etxComment.setText(submission.get_other_comments());
            etxRefugeId.setText(submission.get_refugee_id());
            etxTown.setText(submission.get_town());
            etxLastname.setText(submission.get_lastname());
            etxFirstname.setText(submission.get_lastname());
            //media_size_spinner.setAdapter(submission.get_spinner1());


        }

        callCalendar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.continue_btn:
                if(mAwesomeValidation.validate()) {
                      if (submission_id != 0) {
                        updateDraft();
                        Intent intent = new Intent(this, SubmissionPhotosActivity.class);
                        intent.putExtra("submission_id", submission_id);
                        startActivity(intent);

                    } else {

                        Intent intent = new Intent(this, SubmissionPhotosActivity.class);
                        saveDraft();
                        intent.putExtra("submission_id", submission_id);
                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(context, "You have some errors that you need to attend to", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Submission getInput(){

        Submission inputSubmission = new Submission();

        inputSubmission.set_id(submission_id);
        inputSubmission.set_submission_date(txtSubmissionDate.getText().toString());
        inputSubmission.set_firstname(etxFirstname.getText().toString());
        inputSubmission.set_lastname(etxLastname.getText().toString());
        inputSubmission.set_town(etxTown.getText().toString());;
        inputSubmission.set_other_comments(etxComment.getText().toString());
        inputSubmission.set_camp(etxCamp.getText().toString());
        inputSubmission.set_refugee_id(etxRefugeId.getText().toString());
        inputSubmission.set_spinner1(media_size_spinner.getSelectedItem().toString());
        inputSubmission.set_status(-1);

        return inputSubmission;
    }
    private void updateDraft(){
        db.updatedDraftSubmission(getInput());

        Toast.makeText(context, "Updated draft", Toast.LENGTH_SHORT).show();
    }

    private void saveDraft() {
        ContentValues values = new ContentValues();
        long id = db.addDraftSubmission(getInput());
        submission_id = Integer.valueOf(String.valueOf(id));

        Toast.makeText(context, "Saved to draft", Toast.LENGTH_SHORT).show();
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

            String date_string = getTodaysDate(day, month, year);
            txtSubmissionDate.setText(date_string);
        }
    };

    public void callCalendar() {
        Calendar c = Calendar.getInstance();


        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
//
//        String monthName = f.format("%tB",c).toString();

        String date_string = getTodaysDate(day, month, year);
        txtSubmissionDate.setText(date_string);
    }

    public String getTodaysDate(int day, int month, int year){
        String formatted_date = "";

        formatted_date = day + "/" + (month + 1) + "/" + year;
        return formatted_date;
    }

    public void onComplete(String town) {
        etxTown.setText(town);
//        etxTown.setEnabled(false);
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
