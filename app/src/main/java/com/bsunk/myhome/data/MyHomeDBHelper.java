package com.bsunk.myhome.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bharat on 9/12/2016.
 */
public class MyHomeDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "myhome.db";
    private static MyHomeDBHelper mInstance = null;

    public static MyHomeDBHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new MyHomeDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public MyHomeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_LIGHTS_TABLE = "CREATE TABLE" + MyHomeContract.Lights.TABLE_NAME + " (" +
                MyHomeContract.Lights.COLUMN_ID + " INTEGER PRIMARY KEY," +
                MyHomeContract.Lights.COLUMN_ENTITY_ID + " TEXT NOT NULL," +
                MyHomeContract.Lights.COLUMN_NAME + " TEXT," +
                MyHomeContract.Lights.COLUMN_STATE + " TEXT," +
                MyHomeContract.Lights.COLUMN_LAST_CHANGED + " TEXT," +
                MyHomeContract.Lights.COLUMN_BRIGHTNESS + " INTEGER," +
                MyHomeContract.Lights.COLUMN_COLOR_TEMP + " TEXT," +
                MyHomeContract.Lights.COLUMN_RGB + " TEXT," +
                " );";

        final String SQL_CREATE_SENSORS_TABLE = "CREATE TABLE" + MyHomeContract.Sensors.TABLE_NAME + " (" +
                MyHomeContract.Sensors.COLUMN_ID + " INTEGER PRIMARY KEY," +
                MyHomeContract.Sensors.COLUMN_ENTITY_ID + " TEXT NOT NULL," +
                MyHomeContract.Sensors.COLUMN_NAME + " TEXT," +
                MyHomeContract.Sensors.COLUMN_ICON + " TEXT," +
                MyHomeContract.Sensors.COLUMN_UNITS + " TEXT," +
                MyHomeContract.Sensors.COLUMN_STATE + " TEXT," +
                MyHomeContract.Sensors.COLUMN_LAST_CHANGED + " TEXT," +
                " );";

        final String SQL_CREATE_MEDIA_PLAYERS_TABLE = "CREATE TABLE" + MyHomeContract.MediaPlayers.TABLE_NAME + " (" +
                MyHomeContract.MediaPlayers.COLUMN_ID + " INTEGER PRIMARY KEY," +
                MyHomeContract.MediaPlayers.COLUMN_ENTITY_ID + " TEXT NOT NULL," +
                MyHomeContract.MediaPlayers.COLUMN_NAME + " TEXT," +
                MyHomeContract.MediaPlayers.COLUMN_STATE + " TEXT," +
                MyHomeContract.MediaPlayers.COLUMN_LAST_CHANGED + " TEXT," +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_LIGHTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SENSORS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MEDIA_PLAYERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyHomeContract.Lights.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyHomeContract.Sensors.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyHomeContract.MediaPlayers.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
