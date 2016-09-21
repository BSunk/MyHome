package com.bsunk.myhome.service;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.bsunk.myhome.data.MyHomeContract;
import com.tylerjroach.eventsource.EventSourceHandler;
import com.tylerjroach.eventsource.MessageEvent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bharat on 9/18/2016.
 */

public class EventSourceConnection implements EventSourceHandler {
    Context mContext;
    public static String LOG_TAG = EventSourceConnection.class.getName();

    public EventSourceConnection(Context context) {
        mContext = context;
    }

    @Override
    public void onConnect() {
        Log.v("SSE Connected", "True");
    }

    @Override
    public void onMessage(String event, MessageEvent message) {
        Log.v("SSE Message", message.data);

        try {
            JSONObject jsonObject = new JSONObject(message.data);
            if (jsonObject.getString("event_type").equals("state_changed")) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject new_state = data.getJSONObject("new_state");
                if(new_state.getString("entity_id").contains("light.")) {
                    getLightsUpdate(new_state);
                }
                else if(new_state.getString("entity_id").contains("sensor.")) {
                    getSensorsUpdate(new_state);
                }
            }
        }
        catch (JSONException e) {

        }
    }

    public void getSensorsUpdate(JSONObject data) {
        try {
            ContentValues values = new ContentValues();
            String entity_id = data.getString("entity_id");
            values.put(MyHomeContract.MyHome.COLUMN_LAST_CHANGED, data.getString("last_changed"));
            values.put(MyHomeContract.MyHome.COLUMN_STATE, data.getString("state"));
            mContext.getContentResolver().update(MyHomeContract.MyHome.CONTENT_URI, values, MyHomeContract.MyHome.COLUMN_ENTITY_ID + "="+ "'" + entity_id + "'", null);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to parse new sensor state data");
        }
    }

    public void getLightsUpdate(JSONObject data) {
        try {
            ContentValues values = new ContentValues();
            String entity_id = data.getString("entity_id");
            values.put(MyHomeContract.MyHome.COLUMN_LAST_CHANGED, data.getString("last_changed"));
            values.put(MyHomeContract.MyHome.COLUMN_STATE, data.getString("state"));
            JSONObject attributes = data.getJSONObject("attributes");

            if (attributes.has("brightness")) {
                values.put(MyHomeContract.MyHome.COLUMN_BRIGHTNESS, attributes.getString("brightness"));
            }
            if (attributes.has("color_temp")) {
                values.put(MyHomeContract.MyHome.COLUMN_COLOR_TEMP, attributes.getString("color_temp"));
            }
            if (attributes.has("rgb_color")) {
                String rgb = attributes.getString("rgb_color");
                values.put(MyHomeContract.MyHome.COLUMN_RGB, rgb);
            }
            mContext.getContentResolver().update(MyHomeContract.MyHome.CONTENT_URI, values, MyHomeContract.MyHome.COLUMN_ENTITY_ID + "="+ "'" + entity_id + "'", null);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Failed to parse new light state data");
        }
    }

    @Override
    public void onComment(String comment) {
        //comments only received if exposeComments turned on
        Log.v("SSE Comment", comment);
    }

    @Override
    public void onError(Throwable t) {
        //ignore ssl NPE on eventSource.close()
    }

    @Override
    public void onClosed(boolean willReconnect) {
        Log.v("SSE Closed", "reconnect? " + willReconnect);
    }
}

