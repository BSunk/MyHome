package com.bsunk.myhome;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bsunk.myhome.data.MyHomeContract;
import com.bsunk.myhome.service.ConfigDataPullService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.entity_recyclerview) RecyclerView EntityRecyclerView;
    @BindView(R.id.empty_list_textview) TextView emptyListTextView;

    StaggeredGridLayoutManager sglm;
    EntityAdapter adapter;
    int LOADER;
    public static final int ALL_LOADER = 0;
    public static final int SENSORS_LOADER = 1;
    public static final int LIGHTS_LOADER = 2;
    public static final int MP_LOADER = 3;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = this.getArguments();
        if (args!=null) {
            LOADER = args.getInt(MainActivity.TYPE_KEY);
            getLoaderManager().initLoader(LOADER, null, this);
        }

        sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        EntityRecyclerView.setLayoutManager(sglm);
        adapter = new EntityAdapter(getContext(), null);
        EntityRecyclerView.setAdapter(adapter);

        return rootView ;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Defines an array to contain the selection arguments
        switch (i) {
            case ALL_LOADER:
                return new CursorLoader(getActivity(),
                        MyHomeContract.MyHome.CONTENT_URI, null, null, null, null);
            case SENSORS_LOADER:
                return new CursorLoader(getActivity(),
                        MyHomeContract.MyHome.CONTENT_URI, null, MyHomeContract.MyHome.COLUMN_TYPE + " = ?", new String[] {ConfigDataPullService.TYPE[0]}, null);
            case LIGHTS_LOADER:
                return new CursorLoader(getActivity(),
                        MyHomeContract.MyHome.CONTENT_URI, null, MyHomeContract.MyHome.COLUMN_TYPE + " = ?", new String[] {ConfigDataPullService.TYPE[1]}, null);
            case MP_LOADER:
                return new CursorLoader(getActivity(),
                        MyHomeContract.MyHome.CONTENT_URI, null, MyHomeContract.MyHome.COLUMN_TYPE + " = ?", new String[] {ConfigDataPullService.TYPE[2]}, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
        if(cursor.getCount()==0) {
            emptyListTextView.setVisibility(View.VISIBLE);
        }
        else {
            emptyListTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

}
