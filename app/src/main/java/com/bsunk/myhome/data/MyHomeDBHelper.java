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

        final String SQL_CREATE_MYHOME_TABLE = "CREATE TABLE " + MyHomeContract.MyHome.TABLE_NAME + " (" +
                MyHomeContract.MyHome.COLUMN_ID + " INTEGER PRIMARY KEY," +
                MyHomeContract.MyHome.COLUMN_ENTITY_ID + " TEXT NOT NULL," +
                MyHomeContract.MyHome.COLUMN_NAME + " TEXT," +
                MyHomeContract.MyHome.COLUMN_STATE + " TEXT," +
                MyHomeContract.MyHome.COLUMN_LAST_CHANGED + " TEXT," +
                MyHomeContract.MyHome.COLUMN_BRIGHTNESS + " TEXT," +
                MyHomeContract.MyHome.COLUMN_COLOR_TEMP + " TEXT," +
                MyHomeContract.MyHome.COLUMN_ICON + " TEXT," +
                MyHomeContract.MyHome.COLUMN_UNITS + " TEXT," +
                MyHomeContract.MyHome.COLUMN_TYPE + " TEXT," +
                MyHomeContract.MyHome.COLUMN_RGB + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MYHOME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyHomeContract.MyHome.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
