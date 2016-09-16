package com.bsunk.myhome;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsunk.myhome.data.MyHomeContract;

/**
 * Created by Bharat on 9/13/2016.
 */
public class LightsAdapter extends RecyclerView.Adapter<LightsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public IMyViewHolderClicks mListener;
        public TextView nameTextView;

        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            nameTextView.setOnClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.imageLink(view, getAdapterPosition());
        }

        public static interface IMyViewHolderClicks {
            public void imageLink(View image, int pos);
        }
    }

    Cursor cursor;
    Context mContext;

    public LightsAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        mContext = context;
        setHasStableIds(true);
    }

    @Override
    public LightsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View lightItemView = inflater.inflate(R.layout.light_item, parent, false);

        LightsAdapter.ViewHolder vh = new ViewHolder(lightItemView, new LightsAdapter.ViewHolder.IMyViewHolderClicks() {
            public void imageLink(View artwork, int pos) {
                if (null !=  Long.toString(getItemId(pos))) {
//                    Intent i = new Intent(context, DetailActivity.class)
//                            .putExtra(DetailFragment.FAV_ID_KEY, Long.toString(getItemId(pos)));
//                    context.startActivity(i);
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(LightsAdapter.ViewHolder viewHolder, int position) {
        cursor.moveToPosition(position);
        String entityName = cursor.getString(cursor.getColumnIndex(MyHomeContract.Lights.COLUMN_NAME));
        viewHolder.nameTextView.setText(entityName);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                return cursor.getInt(cursor.getColumnIndex(MyHomeContract.Lights.COLUMN_ID));
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

}

