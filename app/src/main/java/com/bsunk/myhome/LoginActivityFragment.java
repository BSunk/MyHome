package com.bsunk.myhome;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bsunk.myhome.helper.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.server_connect_button) Button buttonConnecct;
    @BindView(R.id.server_ip) EditText editTextIPAddress;
    @BindView(R.id.server_password) EditText editTextPw;
    @BindView(R.id.server_port) EditText editTextPort;
    @BindView(R.id.server_remember_checkbox) CheckBox checkboxRemember;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        buttonConnecct.setOnClickListener(this);

        return rootView;
    }

    public void connectButtonClick() {
        String address = editTextIPAddress.getText().toString();
        String port = editTextPort.getText().toString();
        String pw = editTextPw.getText().toString();
        boolean isRemembered = checkboxRemember.isChecked();

        checkIfValid(address, port, pw);

    }

    public void checkIfValid(String ip, String port, final String pw) {
        String url = "http://"+ ip + ":" + port + "/api/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("API running.")) {
                                System.out.println("success");
                            }
                            else {
                                System.out.println("failed");
                            }
                        }
                        catch(JSONException e) {
                            System.out.println("failed");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handle error
                System.out.println("failed");
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
        MySingleton.getInstance(getContext()).getRequestQueue().add(req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.server_connect_button:
                connectButtonClick();
                break;
        }
    }
}
