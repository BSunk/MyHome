package com.bsunk.myhome;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bsunk.myhome.data.MyHomeContract;
import com.bsunk.myhome.service.ConfigDataPullService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bharat on 9/27/2016.
 */

public class LightDetailDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.light_close_dialog) ImageView closeButton;

    private static final int URL_LOADER = 0;


    static LightDetailDialogFragment newInstance() {
        LightDetailDialogFragment f = new LightDetailDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_light_detail_dialog, container, false);
        ButterKnife.bind(this, v);
        getLoaderManager().initLoader(URL_LOADER, null, this);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle)
    {
        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(),
                        MyHomeContract.MyHome.CONTENT_URI, null, MyHomeContract.MyHome.COLUMN_TYPE + " = ?", new String[] {ConfigDataPullService.TYPE[1]}, null);
            default:
                return null;
        }
    }





}
