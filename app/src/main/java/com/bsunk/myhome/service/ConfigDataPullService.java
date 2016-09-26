package com.bsunk.myhome.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bsunk.myhome.R;
import com.bsunk.myhome.Utility;
import com.bsunk.myhome.data.MyHomeContract;
import com.bsunk.myhome.helper.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
/**
 * Created by Bharat on 9/11/2016.
 */
//IntentService class that retrieves all the data from the server.

public class ConfigDataPullService extends IntentService {

    RequestQueue mRequestQueue;
    String LOG_TAG = ConfigDataPullService.class.getName();
    Handler mHandler;

    public static final String[] TYPE = {"Sensors", "Lights", "Media Players"};

    public ConfigDataPullService() {
        super("ConfigDataPullService");
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        mRequestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        getData();
    }

    public void getData() {
        String url;
        String baseURL = Utility.buildConnectionBaseURL(getApplicationContext());
        final String pw = Utility.getPW(getApplicationContext());

        if (baseURL!=null) {
            url = baseURL + "/api/bootstrap";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            eraseDBs();
                            try {
                                JSONArray states = response.getJSONArray("states");
                                for (int i = 0; i < states.length(); i++) {
                                    JSONObject entity = states.getJSONObject(i);
                                    if (entity.getString("entity_id").contains("sensor.")) {
                                        getSensorData(entity);
                                    } else if (entity.getString("entity_id").contains("media_player.")) {
                                        getMediaPlayerData(entity);
                                    } else if (entity.getString("entity_id").contains("light.")) {
                                        getLightsData(entity);
                                    } else if (entity.getString("entity_id").contains("group.")) {

                                    }
                                }
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                            mHandler.post(new DisplayToast(getApplicationContext(), getString(R.string.connection_failed_dialog)));
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-ha-access", pw);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            mRequestQueue.add(jsObjRequest);
        }
        else {
            mHandler.post(new DisplayToast(this, getString(R.string.connection_failed_dialog)));
        }
    }

    private void getLightsData(JSONObject entity) {
        ContentValues lightsValues = new ContentValues();

        try {
            lightsValues.put(MyHomeContract.MyHome.COLUMN_ENTITY_ID, entity.getString("entity_id"));
            lightsValues.put(MyHomeContract.MyHome.COLUMN_STATE, entity.getString("state"));
            lightsValues.put(MyHomeContract.MyHome.COLUMN_LAST_CHANGED, entity.getString("last_updated"));
            lightsValues.put(MyHomeContract.MyHome.COLUMN_TYPE, TYPE[1]);
            JSONObject attributes = entity.getJSONObject("attributes");
            if (attributes.has("brightness")) {
                lightsValues.put(MyHomeContract.MyHome.COLUMN_BRIGHTNESS, attributes.getString("brightness"));
            }
            if (attributes.has("color_temp")) {
                lightsValues.put(MyHomeContract.MyHome.COLUMN_COLOR_TEMP, attributes.getString("color_temp"));
            }
            if (attributes.has("friendly_name")) {
                lightsValues.put(MyHomeContract.MyHome.COLUMN_NAME, attributes.getString("friendly_name"));
            }
            if (entity.has("rgb_color")) {
                JSONArray rgb = entity.getJSONArray("rgb_color");
                String rgbString = rgb.getString(1) + " " + rgb.getString(2) + " " + rgb.getString(3);
                lightsValues.put(MyHomeContract.MyHome.COLUMN_RGB, rgbString);
            }
            getContentResolver().insert(MyHomeContract.MyHome.CONTENT_URI, lightsValues);
        }
        catch(JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void getSensorData(JSONObject entity) {
        ContentValues sensorValues = new ContentValues();

        try {
            sensorValues.put(MyHomeContract.MyHome.COLUMN_ENTITY_ID, entity.getString("entity_id"));
            sensorValues.put(MyHomeContract.MyHome.COLUMN_STATE, entity.getString("state"));
            sensorValues.put(MyHomeContract.MyHome.COLUMN_LAST_CHANGED, entity.getString("last_updated"));
            sensorValues.put(MyHomeContract.MyHome.COLUMN_TYPE, TYPE[0]);
            JSONObject attributes = entity.getJSONObject("attributes");
            if (attributes.has("icon")) {
                sensorValues.put(MyHomeContract.MyHome.COLUMN_ICON, attributes.getString("icon"));
            }
            if (attributes.has("friendly_name")) {
                sensorValues.put(MyHomeContract.MyHome.COLUMN_NAME, attributes.getString("friendly_name"));
            }
            if (attributes.has("unit_of_measurement")) {
                sensorValues.put(MyHomeContract.MyHome.COLUMN_UNITS, attributes.getString("unit_of_measurement"));
            }
            getContentResolver().insert(MyHomeContract.MyHome.CONTENT_URI, sensorValues);
        }
        catch(JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void getMediaPlayerData(JSONObject entity) {
        ContentValues mediaPlayerValues = new ContentValues();

        try {
            mediaPlayerValues.put(MyHomeContract.MyHome.COLUMN_ENTITY_ID, entity.getString("entity_id"));
            mediaPlayerValues.put(MyHomeContract.MyHome.COLUMN_STATE, entity.getString("state"));
            mediaPlayerValues.put(MyHomeContract.MyHome.COLUMN_LAST_CHANGED, entity.getString("last_updated"));
            mediaPlayerValues.put(MyHomeContract.MyHome.COLUMN_TYPE, TYPE[2]);
            JSONObject attributes = entity.getJSONObject("attributes");
            if (attributes.has("friendly_name")) {
                mediaPlayerValues.put(MyHomeContract.MyHome.COLUMN_NAME, attributes.getString("friendly_name"));
            }
            getContentResolver().insert(MyHomeContract.MyHome.CONTENT_URI, mediaPlayerValues);
        }
        catch(JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    private void eraseDBs() {
        getApplicationContext().getContentResolver().delete(MyHomeContract.MyHome.CONTENT_URI, null, null);
    }

    public class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;
        public DisplayToast(Context mContext, String text){
            this.mContext = mContext;
            mText = text;
        }
        public void run(){
            Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
        }
    }
}
