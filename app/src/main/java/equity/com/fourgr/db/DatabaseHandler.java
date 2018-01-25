package equity.com.fourgr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import equity.com.fourgr.model.Menu;
import equity.com.fourgr.model.Submission;
import equity.com.fourgr.model.SubmissionPhoto;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "fourgr";

    // Menu table
    public static final String TABLE_MAINMENU = "mainmenu";

    public static final String KEY_ID = "id"; // Common column
    public static final String KEY_MENU_TITLE = "menu_title";
    public static final String KEY_MENU_ICON = "menu_icon";
    public static final String KEY_MENU_SLUG = "menu_slug";
    public static final String KEY_MENU_USERS = "menu_users";

    public static final String TABLE_OFFLINE = "offline";


    public static final String KEY_STATUS = "status";

    // Submission Table
    public static final String TABLE_SUBMISSION = "submission";

    public static final String KEY_SUBMISSION_DATE = "submission_date";
    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_LAST_NAME = "lastname";
    public static final String KEY_TOWN = "town";
    public static final String KEY_CAMP = "camp";
    public static final String KEY_REFUGEE_ID = "refugee_id";
    public static final String KEY_SPINNER1 = "spinner1";
    public static final String KEY_OTHER_COMMENTS = "other_comments";
    public static final String KEY_PHOTO_1 = "photo_1";
    public static final String KEY_PHOTO_2 = "photo_2";

    public static final String KEY_CREATED_AT = "created_at";

    public static final String TABLE_SUBMISSION_PHOTOS = "submission_photos";




    public static final String KEY_PHOTO_URL = "photo_path";
    public static final String KEY_SUBMISSION_ID = "submission_id";
    public static final String KEY_THUMB = "photo_thumb";

    public static final String TABLE_PHOTOS = "photos";;
    public static final String KEY_PHOTO_NAME = "photo_name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAINMENU_TABLE = "CREATE TABLE " + TABLE_MAINMENU + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MENU_TITLE + " TEXT,"
                + KEY_MENU_ICON + " TEXT,"
                + KEY_MENU_SLUG + " TEXT,"
                + KEY_MENU_USERS + " TEXT"
                + ");";

        String CREATE_SUBMISSION_TABLE = "CREATE TABLE " + TABLE_SUBMISSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_SUBMISSION_DATE + " TEXT,"
                + KEY_CAMP + " TEXT,"
                + KEY_TOWN + " TEXT,"
                + KEY_REFUGEE_ID + " TEXT,"
                + KEY_SPINNER1 + " TEXT,"
                + KEY_OTHER_COMMENTS + " TEXT,"
                + KEY_PHOTO_1 + " TEXT,"
                + KEY_PHOTO_2 + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ");";

        String CREATE_OFFLINE_TABLE = "CREATE TABLE " + TABLE_OFFLINE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_SUBMISSION_DATE + " TEXT,"
                + KEY_CAMP + " TEXT,"
                + KEY_TOWN + " TEXT,"
                + KEY_REFUGEE_ID + " TEXT,"
                + KEY_SPINNER1 + " TEXT,"
                + KEY_OTHER_COMMENTS + " TEXT,"
                + KEY_PHOTO_1 + " TEXT,"
                + KEY_PHOTO_2 + " TEXT,"
                + KEY_STATUS + " INTEGER"
                + ");";



        String CREATE_SUBMISSION_PHOTOS_TABLE = "CREATE TABLE " + TABLE_SUBMISSION_PHOTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_ID + " INTEGER,"
                + KEY_THUMB + " TEXT,"
                + KEY_PHOTO_URL + " TEXT"
                + ");";


        String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_ID + " INTEGER,"
                + KEY_PHOTO_NAME + " TEXT,"
                + KEY_THUMB + " TEXT,"
                + KEY_PHOTO_URL + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ");";


        db.execSQL(CREATE_MAINMENU_TABLE);
        db.execSQL(CREATE_SUBMISSION_TABLE);
        db.execSQL(CREATE_OFFLINE_TABLE);
        db.execSQL(CREATE_SUBMISSION_PHOTOS_TABLE);
        db.execSQL(CREATE_PHOTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINMENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBMISSION_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MAINMENU);
        db.execSQL("DELETE FROM " + TABLE_SUBMISSION);
        db.execSQL("DELETE FROM " + TABLE_OFFLINE);
        db.execSQL("DELETE FROM " + TABLE_PHOTOS);
        db.execSQL("DELETE FROM " + TABLE_SUBMISSION_PHOTOS);
    }

    public void addMainMenu(Menu menu){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENU_TITLE, menu.get_menu_title());
        values.put(KEY_MENU_ICON, menu.get_menu_icon());
        values.put(KEY_MENU_SLUG, menu.get_menu_slug());
        values.put(KEY_MENU_USERS, menu.get_menu_users());

        db.insert(TABLE_MAINMENU, null, values);
        db.close();
    }




    public List<Menu> Mainmenu(){
        List<Menu> menuList = new ArrayList<Menu>();

        String selectQuery = "SELECT * FROM " + TABLE_MAINMENU;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();

                menu.set_id(Integer.parseInt(cursor.getString(0)));
                menu.set_menu_title(cursor.getString(1));
                menu.set_menu_icon(Integer.parseInt(cursor.getString(2)));
                menu.set_menu_slug(cursor.getString(3));
                menu.set_menu_users(cursor.getString(4));


                    menuList.add(menu);

            }while (cursor.moveToNext());
        }
        return menuList;
    }



    public int getmainMenuCount(){
        String countQuery = "SELECT  * FROM " + TABLE_MAINMENU;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    public boolean deletePhotoByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TABLE_SUBMISSION_PHOTOS, KEY_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }


    public void addSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, submission.get_id());
        values.put(KEY_FIRST_NAME, submission.get_firstname());
        values.put(KEY_LAST_NAME, submission.get_lastname());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_CAMP, submission.get_camp());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_REFUGEE_ID, submission.get_refugee_id());
        values.put(KEY_SPINNER1, submission.get_spinner1());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());

        values.put(KEY_CREATED_AT, submission.get_created_at());

        db.insert(TABLE_SUBMISSION, null, values);
        db.close();
    }

    public void clearSubmissions(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SUBMISSION, null, null);
        db.close();
    }



    public List<Submission> allSubmissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBMISSION + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
                submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
                submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }


    public List<Submission> allSubmissions(int user_id){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBMISSION + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
                submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }


    public Submission getSubmission(int id){
        Submission submission = new Submission();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SUBMISSION, null, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));

            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
            submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
            submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
            submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
            submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
            submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
            submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
            submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
            submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
            submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

        }
        return submission;
    }


    public void addOfflineSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAMP, submission.get_camp());
        values.put(KEY_SPINNER1, submission.get_spinner1());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_REFUGEE_ID, submission.get_refugee_id());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_FIRST_NAME, submission.get_firstname());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());
        values.put(KEY_LAST_NAME, submission.get_lastname());
        values.put(KEY_STATUS, submission.get_status());

        db.insert(TABLE_OFFLINE, null, values);
        db.close();
    }

    public long addDraftSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CAMP, submission.get_camp());
        values.put(KEY_SPINNER1, submission.get_spinner1());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_REFUGEE_ID, submission.get_refugee_id());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_FIRST_NAME, submission.get_firstname());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());
        values.put(KEY_LAST_NAME, submission.get_lastname());
        values.put(KEY_STATUS, -1);

        long id = db.insert(TABLE_OFFLINE, null, values);
        db.close();

        return id;
    }

    public void updatedDraftSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CAMP, submission.get_camp());
        values.put(KEY_SPINNER1, submission.get_spinner1());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_REFUGEE_ID, submission.get_refugee_id());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_FIRST_NAME, submission.get_firstname());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());
        values.put(KEY_LAST_NAME, submission.get_lastname());
        values.put(KEY_STATUS, submission.get_status());

        int updated = db.update(TABLE_OFFLINE, values, KEY_ID + "=?", new String[]{String.valueOf(submission.get_id())});
    }

    public List<Submission> allOfflineSubmissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
                submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public List<Submission> allDraftSubmissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_STATUS + " = -1" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

               submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
                submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }
        return submissions;
    }

    public Submission draftSubmission(int id){
        Submission submission = new Submission();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_OFFLINE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
//        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
            submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
            submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
            submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
            submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
            submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
            submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
            submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
            submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
            submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
        }
        return submission;
    }

    public List<Submission> Submissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_camp(cursor.getString(cursor.getColumnIndex(KEY_CAMP)));
                submission.set_refugee_id(cursor.getString(cursor.getColumnIndex(KEY_REFUGEE_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_spinner1(cursor.getString(cursor.getColumnIndex(KEY_SPINNER1)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_firstname(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                submission.set_lastname(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public ArrayList<SubmissionPhoto> getSubmissionPhotos(int submission_id){
        ArrayList<SubmissionPhoto> submissionPhotos = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SUBMISSION_PHOTOS, null, KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(submission_id)}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                SubmissionPhoto submissionPhoto = new SubmissionPhoto();

                submissionPhoto.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                submissionPhoto.setSubmission_id(cursor.getInt(cursor.getColumnIndex(KEY_SUBMISSION_ID)));
                submissionPhoto.setPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_URL)));
                submissionPhoto.setThumb(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));

                submissionPhotos.add(submissionPhoto);
            }while(cursor.moveToNext());
        }
        return submissionPhotos;
    }

    public boolean deleteOfflineSubmission(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OFFLINE, KEY_ID + "=" + id, null) > 0;
    }


    public int addSubmissionPhoto(SubmissionPhoto photo) {
        ContentValues values = new ContentValues();

        values.put(KEY_SUBMISSION_ID, photo.getSubmission_id());
        values.put(KEY_THUMB, photo.getThumb());
        values.put(KEY_PHOTO_URL, photo.getPath());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(TABLE_SUBMISSION_PHOTOS, null, values);

        return Integer.parseInt(String.valueOf(id));
    }

    public void changeOfflineStatus(int submission_id, int status) {
        ContentValues values = new ContentValues();

        values.put(KEY_STATUS, status);

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_OFFLINE, values, KEY_ID + "=?", new String[]{String.valueOf(submission_id)});
    }

    public void addPhoto(SubmissionPhoto photo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, photo.getId());
        values.put(KEY_SUBMISSION_ID, photo.getSubmission_id());
        values.put(KEY_PHOTO_NAME, photo.getName());
        values.put(KEY_THUMB, photo.getThumb());
        values.put(KEY_PHOTO_URL, photo.getPath());
        values.put(KEY_CREATED_AT, photo.getCreated_at());

        db.insert(TABLE_PHOTOS, null, values);
    }

    public List<SubmissionPhoto> getAllPhotos(){
        List<SubmissionPhoto> photos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PHOTOS, null, null, null, null, null, null);

        photos = this.addPhotosToList(cursor);

        return photos;
    }


    public List<SubmissionPhoto> getPhotoBySubmissionID(int submission_id){
        List<SubmissionPhoto> photos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PHOTOS, null, KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(submission_id)}, null, null, null);

        photos = this.addPhotosToList(cursor);

        return photos;
    }


    private List<SubmissionPhoto> addPhotosToList(Cursor cursor) {
        List<SubmissionPhoto> photos = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                SubmissionPhoto photo = new SubmissionPhoto();

                photo.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                photo.setSubmission_id(cursor.getInt(cursor.getColumnIndex(KEY_SUBMISSION_ID)));
                photo.setName(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_NAME)));
                photo.setThumb(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));
                photo.setPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_URL)));
                photo.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                photos.add(photo);
            }while (cursor.moveToNext());
        }
        return photos;
    }

}
