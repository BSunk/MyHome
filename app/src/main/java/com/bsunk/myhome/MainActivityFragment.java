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

import com.bsunk.myhome.data.MyHomeContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.entity_recyclerview) RecyclerView EntityRecyclerView;
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
        ButterKnife.bind(rootView, getActivity());

        Bundle args = this.getArguments();
        if (args!=null) {
            LOADER = args.getInt(MainActivity.TYPE_KEY);
            Log.v("TAG", Integer.toString(LOADER));
        }

        sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        EntityRecyclerView.setLayoutManager(sglm);
        adapter = new EntityAdapter(getContext(), null);
        EntityRecyclerView.setAdapter(adapter);
        return rootView ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ALL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                MyHomeContract.MyHome.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

}
