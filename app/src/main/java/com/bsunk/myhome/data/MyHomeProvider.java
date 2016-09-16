package com.bsunk.myhome.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Bharat on 9/12/2016.
 */
public class MyHomeProvider extends ContentProvider {

    static final int MYHOME = 100;
    static final int MYHOME_WITH_ID = 101;


    static final UriMatcher uriMatcher;
    static final String authority = MyHomeContract.CONTENT_AUTHORITY;

    static {
        uriMatcher = new UriMatcher((UriMatcher.NO_MATCH));
        uriMatcher.addURI(authority, MyHomeContract.PATH_MYHOME, MYHOME);
        uriMatcher.addURI(authority,MyHomeContract.PATH_MYHOME + "/#", MYHOME_WITH_ID);
    }

    private static final String sMyHomeIDSelection =
            MyHomeContract.MyHome.TABLE_NAME+
                    "." + MyHomeContract.MyHome.COLUMN_ENTITY_ID + " = ? ";

    private MyHomeDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MyHomeDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = uriMatcher.match(uri);

        switch (match) {
            case MYHOME:
                return  MyHomeContract.MyHome.CONTENT_TYPE;
            case MYHOME_WITH_ID:
                return  MyHomeContract.MyHome.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case MYHOME: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.MyHome.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MYHOME_WITH_ID: {
                int entityID = MyHomeContract.getEntityIDFromURI(uri);
                selectionArgs = new String[]{Integer.toString(entityID)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.MyHome.TABLE_NAME,
                        projection,
                        sMyHomeIDSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MYHOME: {
                long _id = db.insert(MyHomeContract.MyHome.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MyHomeContract.MyHome.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MYHOME:
                rowsDeleted = db.delete(
                        MyHomeContract.MyHome.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MYHOME:
                rowsUpdated = db.update(MyHomeContract.MyHome.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case MYHOME:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MyHomeContract.MyHome.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

}
