package com.bsunk.myhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bharat on 9/24/2016.
 */

public class Utility {

    public static String IP_KEY = "ip";
    public static String PORT_KEY = "key";
    public static String PW_KEY = "pw";

    public static void storeConnectionInfo(Context context, String ip, String port, String pw) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IP_KEY, ip);
        editor.putString(PORT_KEY, port);
        editor.putString(PW_KEY,pw);
        editor.apply();
    }

    public static String buildConnectionBaseURL(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = prefs.getString(Utility.IP_KEY, null);
        String port = prefs.getString(Utility.PORT_KEY, context.getString(R.string.server_ip_default_port));

        if(ip!=null) {
            return "http://" + ip + ":" + port + "";
        }
        else {
            return null;
        }
    }

    public static String getPW(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Utility.PW_KEY, "");
    }

}
