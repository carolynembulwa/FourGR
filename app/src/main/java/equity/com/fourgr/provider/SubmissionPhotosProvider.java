package equity.com.fourgr.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

import equity.com.fourgr.db.DatabaseHandler;


public class SubmissionPhotosProvider extends ContentProvider {
    static final String PROVIDER_NAME = "equity.com.fourgr.provider.SubmissionPhotosProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/" + DatabaseHandler.TABLE_SUBMISSION_PHOTOS;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int URI_CODE = 1;

    private static HashMap<String, String> values;

    static final UriMatcher uriMatcher;
    DatabaseHandler databaseHandler;
    SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, DatabaseHandler.TABLE_SUBMISSION_PHOTOS, URI_CODE);
    }

    @Override
    public boolean onCreate() {
        databaseHandler = new DatabaseHandler(getContext());
        db = databaseHandler.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(databaseHandler.TABLE_SUBMISSION_PHOTOS);

        switch (uriMatcher.match(uri)){
            case URI_CODE:
                builder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cur = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cur.setNotificationUri(getContext().getContentResolver(), uri);
        return cur;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case URI_CODE:
                return "vnd.android.cursor.dir/" + DatabaseHandler.TABLE_SUBMISSION_PHOTOS;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = db.insert(databaseHandler.TABLE_SUBMISSION_PHOTOS, null, contentValues);

        if (rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);

            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }
        Log.e("SubmissionError", "Could not insert photo");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted = 0;
        switch (uriMatcher.match(uri)) {
            case URI_CODE:
                rowsDeleted = db.delete(databaseHandler.TABLE_SUBMISSION_PHOTOS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)) {
            case URI_CODE:
                rowsUpdated = db.update(databaseHandler.TABLE_SUBMISSION_PHOTOS, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
