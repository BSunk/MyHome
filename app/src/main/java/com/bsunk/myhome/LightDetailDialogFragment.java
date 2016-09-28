package com.bsunk.myhome;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bharat on 9/27/2016.
 */

public class LightDetailDialogFragment extends DialogFragment {

    static LightDetailDialogFragment newInstance() {
        LightDetailDialogFragment f = new LightDetailDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_light_detail_dialog, container, false);

        return v;
    }

}
