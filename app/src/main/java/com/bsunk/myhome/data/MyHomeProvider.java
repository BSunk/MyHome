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

    static final int LIGHTS = 100;
    static final int LIGHTS_WITH_ID = 101;
    static final int SENSORS = 200;
    static final int SENSORS_WITH_ID = 201;
    static final int MEDIA_PLAYERS = 300;
    static final int MEDIA_PLAYER_WITH_ID = 301;

    static final UriMatcher uriMatcher;
    static final String authority = MyHomeContract.CONTENT_AUTHORITY;

    static {
        uriMatcher = new UriMatcher((UriMatcher.NO_MATCH));
        uriMatcher.addURI(authority, MyHomeContract.PATH_LIGHTS, LIGHTS);
        uriMatcher.addURI(authority,MyHomeContract.PATH_LIGHTS + "/#", LIGHTS_WITH_ID);
        uriMatcher.addURI(authority, MyHomeContract.PATH_SENSORS, SENSORS);
        uriMatcher.addURI(authority,MyHomeContract.PATH_SENSORS + "/#", SENSORS_WITH_ID);
        uriMatcher.addURI(authority, MyHomeContract.PATH_MEDIA_PLAYERS, MEDIA_PLAYERS);
        uriMatcher.addURI(authority,MyHomeContract.PATH_MEDIA_PLAYERS + "/#", MEDIA_PLAYER_WITH_ID);
    }

    private static final String sLightsIDSelection =
            MyHomeContract.Lights.TABLE_NAME+
                    "." + MyHomeContract.Lights.COLUMN_ENTITY_ID + " = ? ";
    private static final String sSensorsIDSelection =
            MyHomeContract.Sensors.TABLE_NAME+
                    "." + MyHomeContract.Sensors.COLUMN_ENTITY_ID + " = ? ";
    private static final String sMediaPlayersIDSelection =
            MyHomeContract.MediaPlayers.TABLE_NAME+
                    "." + MyHomeContract.MediaPlayers.COLUMN_ENTITY_ID + " = ? ";

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
            case LIGHTS:
                return  MyHomeContract.Lights.CONTENT_TYPE;
            case LIGHTS_WITH_ID:
                return  MyHomeContract.Lights.CONTENT_TYPE;
            case SENSORS:
                return  MyHomeContract.Sensors.CONTENT_TYPE;
            case SENSORS_WITH_ID:
                return  MyHomeContract.Sensors.CONTENT_TYPE;
            case MEDIA_PLAYERS:
                return  MyHomeContract.MediaPlayers.CONTENT_TYPE;
            case MEDIA_PLAYER_WITH_ID:
                return  MyHomeContract.MediaPlayers.CONTENT_TYPE;
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
            case LIGHTS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.Lights.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LIGHTS_WITH_ID: {
                int entityID = MyHomeContract.getEntityIDFromURI(uri);
                selectionArgs = new String[]{Integer.toString(entityID)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.Lights.TABLE_NAME,
                        projection,
                        sLightsIDSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SENSORS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.Sensors.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SENSORS_WITH_ID: {
                int entityID = MyHomeContract.getEntityIDFromURI(uri);
                selectionArgs = new String[]{Integer.toString(entityID)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.Sensors.TABLE_NAME,
                        projection,
                        sSensorsIDSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MEDIA_PLAYERS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.MediaPlayers.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MEDIA_PLAYER_WITH_ID: {
                int entityID = MyHomeContract.getEntityIDFromURI(uri);
                selectionArgs = new String[]{Integer.toString(entityID)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyHomeContract.MediaPlayers.TABLE_NAME,
                        projection,
                        sMediaPlayersIDSelection,
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
            case LIGHTS: {
                long _id = db.insert(MyHomeContract.Lights.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MyHomeContract.Lights.buildLightsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SENSORS: {
                long _id = db.insert(MyHomeContract.Sensors.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MyHomeContract.Sensors.buildSensorsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MEDIA_PLAYERS: {
                long _id = db.insert(MyHomeContract.MediaPlayers.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MyHomeContract.MediaPlayers.buildMediaPlayersUri(_id);
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
            case LIGHTS:
                rowsDeleted = db.delete(
                        MyHomeContract.Lights.TABLE_NAME, selection, selectionArgs);
                break;
            case SENSORS:
                rowsDeleted = db.delete(
                        MyHomeContract.Sensors.TABLE_NAME, selection, selectionArgs);
                break;
            case MEDIA_PLAYERS:
                rowsDeleted = db.delete(
                        MyHomeContract.MediaPlayers.TABLE_NAME, selection, selectionArgs);
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
            case LIGHTS:
                rowsUpdated = db.update(MyHomeContract.Lights.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SENSORS:
                rowsUpdated = db.update(MyHomeContract.Sensors.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MEDIA_PLAYERS:
                rowsUpdated = db.update(MyHomeContract.MediaPlayers.TABLE_NAME, values, selection,
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
            case LIGHTS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MyHomeContract.Lights.TABLE_NAME, null, value);
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
            case SENSORS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MyHomeContract.Sensors.TABLE_NAME, null, value);
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
            case MEDIA_PLAYERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MyHomeContract.MediaPlayers.TABLE_NAME, null, value);
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
