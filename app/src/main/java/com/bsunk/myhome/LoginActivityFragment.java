package com.bsunk.myhome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.server_connect_button) Button buttonConnect;
    @BindView(R.id.server_ip) EditText editTextIPAddress;
    @BindView(R.id.server_password) EditText editTextPw;
    @BindView(R.id.server_port) EditText editTextPort;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        //Set current connection values from SharedPreferences if available.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ip = prefs.getString(Utility.IP_KEY, "");
        String port = prefs.getString(Utility.PORT_KEY, getString(R.string.server_ip_default_port));
        String pw = prefs.getString(Utility.PW_KEY, "");
        editTextIPAddress.setText(ip);
        editTextPort.setText(port);
        editTextPw.setText(pw);

        buttonConnect.setOnClickListener(this);

        return rootView;
    }

    public void connectButtonClick() {
        String address = editTextIPAddress.getText().toString();
        String port = editTextPort.getText().toString();
        String pw = editTextPw.getText().toString();
        checkIfValid(address, port, pw);
    }

    //Tries to connect to server. If successful then save the values in SharedPreferences and go back, otherwise display an error.
    public void checkIfValid(final String ip, final String port, final String pw) {
        String url = "http://"+ ip + ":" + port + "/api/";
        final ProgressDialog progress;
        progress = ProgressDialog.show(getContext(), getString(R.string.progress_connecting),
                getString(R.string.progress_connecting_message), true);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(getString(R.string.connect_error_dialog));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert11 = builder1.create();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("API running.")) {
                                Utility.storeConnectionInfo(getContext(), ip, port, pw);
                                getActivity().onBackPressed();
                                Toast.makeText(getContext(), getString(R.string.connect_success_toast), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                alert11.show();
                            }
                        }
                        catch(JSONException e) {
                            alert11.show();
                        }
                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handle error
                progress.dismiss();
                alert11.show();
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
