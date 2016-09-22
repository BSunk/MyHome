package com.bsunk.myhome;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bharat on 9/21/2016.
 */

public class LightsViewHolder extends RecyclerView.ViewHolder  {
    @BindView(R.id.light_name_textview) TextView lightNameTextView;
    @BindView(R.id.light_bulb_imageView) ImageView lightImageView;
    @BindView(R.id.light_seek_bar) SeekBar lightSeekBar;
    @BindView(R.id.light_brightness) TextView lightBrightness;
    @BindView(R.id.light_off) TextView lightOff;

    public LightsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
