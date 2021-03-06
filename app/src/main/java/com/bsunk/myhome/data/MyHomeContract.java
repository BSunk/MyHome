package com.bsunk.myhome.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bharat on 9/12/2016.
 */
public class MyHomeContract {

    public static final String CONTENT_AUTHORITY = "com.bsunk.myhome";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MYHOME = "my_home";

    public static final class MyHome implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYHOME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MYHOME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MYHOME;

        public static final String TABLE_NAME = "myhome";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_ENTITY_ID = "entity_id";
        public static final String COLUMN_NAME = "friendly_name";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_LAST_CHANGED = "last_changed";
        public static final String COLUMN_BRIGHTNESS = "brightness";
        public static final String COLUMN_COLOR_TEMP = "color_temp";
        public static final String COLUMN_RGB = "rgb_color";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_UNITS = "units";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static int getEntityIDFromURI(Uri uri) {
        try {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
        catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }
}
