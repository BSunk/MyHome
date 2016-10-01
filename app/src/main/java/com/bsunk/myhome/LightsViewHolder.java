package com.bsunk.myhome;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bsunk.myhome.data.MyHomeContract;
import com.bsunk.myhome.service.ConfigDataPullService;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Bharat on 9/21/2016.
 */

public class LightsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.light_name_textview) TextView lightNameTextView;
    @BindView(R.id.light_bulb_imageView) ImageView lightImageView;
    @BindView(R.id.light_seek_bar) SeekBar lightSeekBar;
    @BindView(R.id.light_brightness) TextView lightBrightness;
    @BindView(R.id.light_off) TextView lightOff;

    Context mContext;

    public LightsViewHolder(View itemView, Context context) {
        super(itemView);
        itemView.setOnClickListener(this);
        mContext = context;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View view) {
        showLightDetails();
    }

    void showLightDetails() {
        FragmentTransaction ft = ((Activity)mContext).getFragmentManager().beginTransaction();
        LightDetailDialogFragment newFragment = LightDetailDialogFragment.newInstance();
        Bundle args = new Bundle();

        newFragment.show(ft, "dialog");
    }

}
