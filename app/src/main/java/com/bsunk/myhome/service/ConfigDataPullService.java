package com.bsunk.myhome.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bsunk.myhome.helper.MySingleton;

import org.json.JSONObject;

/**
 * Created by Bharat on 9/11/2016.
 */
public class ConfigDataPullService extends IntentService {

    RequestQueue mRequestQueue;
    String LOG_TAG = ConfigDataPullService.class.getName();

    public ConfigDataPullService() {
        super("ConfigDataPullService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        mRequestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //String dataString = workIntent.getDataString();
        getData();

    }

    public void getData() {
        String url = "http://192.168.10.101:8123/api/bootstrap";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(LOG_TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG, error.getMessage());

                    }
                });

        mRequestQueue.add(jsObjRequest);
    }


}
