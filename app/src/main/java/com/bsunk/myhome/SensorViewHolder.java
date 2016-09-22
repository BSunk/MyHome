package com.bsunk.myhome;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bharat on 9/21/2016.
 */

public class SensorViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.sensor_name_textview) TextView sensorNameTextView;
    @BindView(R.id.sensor_state_textview) TextView sensorStateTextView;
    @BindView(R.id.sensor_icon_imageview) ImageView sensorIconImageView;

    public SensorViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
