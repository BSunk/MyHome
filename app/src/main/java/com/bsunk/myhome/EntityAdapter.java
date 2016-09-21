package com.bsunk.myhome;

import android.content.Context;
import android.database.Cursor;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bsunk.myhome.data.MyHomeContract;

/**
 * Created by Bharat on 9/13/2016.
 */
public class EntityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int SENSOR_TYPE = 1;
    public static int LIGHT_TYPE = 2;
    public static int MEDIA_PLAYER_TYPE = 3;
    public static final double brightnessMax = 255.0;

    public static class SensorViewHolder extends RecyclerView.ViewHolder {
        public TextView sensorNameTextView;
        public TextView sensorStateTextView;
        public ImageView sensorIconImageView;

        public SensorViewHolder(View itemView) {
            super(itemView);
            sensorNameTextView = (TextView) itemView.findViewById(R.id.sensor_name_textview);
            sensorStateTextView = (TextView) itemView.findViewById(R.id.sensor_state_textview);
            sensorIconImageView = (ImageView) itemView.findViewById(R.id.sensor_icon_imageview);
        }
    }

    public static class LightsViewHolder extends RecyclerView.ViewHolder  {
        public TextView lightNameTextView;
        public ImageView lightImageView;
        public SeekBar lightSeekBar;
        public TextView lightBrightness;
        public TextView lightOff;

        public LightsViewHolder(View itemView) {
            super(itemView);
            lightNameTextView = (TextView) itemView.findViewById(R.id.light_name_textview);
            lightImageView = (ImageView) itemView.findViewById(R.id.light_bulb_imageView);
            lightSeekBar = (SeekBar) itemView.findViewById(R.id.light_seek_bar);
            lightBrightness = (TextView) itemView.findViewById(R.id.light_brightness);
            lightOff = (TextView) itemView.findViewById(R.id.light_off);
        }
    }

    private Cursor cursor;
    private Context mContext;

    public EntityAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == LIGHT_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.light_item, viewGroup, false);
            return new LightsViewHolder(v);
        }
        else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.sensor_item, viewGroup, false);
            return new SensorViewHolder(v);
        }
    }

    public Cursor swapCursor(Cursor c) {
        if (cursor == c) {
            return null;
        }
        Cursor oldCursor = cursor;
        this.cursor = c;
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        String type = cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_TYPE));
        if (type.equals("Sensors")) {
            return SENSOR_TYPE;
        }
        else if (type.equals("Lights")) {
            return LIGHT_TYPE;
        }
        else if  (type.equals("Media Players")) {
            return 3;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        cursor.moveToPosition(position);
        String entityName = cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_NAME));

        if ( viewHolder.getItemViewType() == SENSOR_TYPE) {
            String units = cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_UNITS));
            String icon = cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_ICON));
            SensorViewHolder holder = (SensorViewHolder) viewHolder;
            holder.sensorNameTextView.setText(entityName);
            if(units!=null) {
                holder.sensorStateTextView.setText(cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_STATE)) + " " + units);
            }
            else {
                holder.sensorStateTextView.setText(cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_STATE)));
            }
        }
        else if (viewHolder.getItemViewType() == LIGHT_TYPE) {
            final LightsViewHolder holder = (LightsViewHolder) viewHolder;
            holder.lightNameTextView.setText(entityName);
            if(cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_STATE)).equals("on")) {
                holder.lightImageView.setColorFilter(mContext.getResources().getColor(R.color.bulb_on));
                holder.lightBrightness.setVisibility(View.VISIBLE);
                holder.lightSeekBar.setVisibility(View.VISIBLE);
                holder.lightOff.setVisibility(View.GONE);
            }
            else {
                holder.lightImageView.setColorFilter(mContext.getResources().getColor(R.color.bulb_off));
                holder.lightBrightness.setVisibility(View.GONE);
                holder.lightSeekBar.setVisibility(View.GONE);
                holder.lightOff.setVisibility(View.VISIBLE);
            }
            String brightness = cursor.getString(cursor.getColumnIndex(MyHomeContract.MyHome.COLUMN_BRIGHTNESS));
            if (brightness!=null) {
                int progress = Integer.parseInt(brightness);
                holder.lightSeekBar.setProgress(progress);
                double percentBrightness = (progress/brightnessMax) * 100.0;
                holder.lightBrightness.setText(Math.round(percentBrightness)+"%");
            }

            holder.lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    double percentBrightness = (progress/255.0) * 100.0;
                    holder.lightBrightness.setText(Math.round(percentBrightness)+"%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            //StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            //layoutParams.setFullSpan(true);
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

}

