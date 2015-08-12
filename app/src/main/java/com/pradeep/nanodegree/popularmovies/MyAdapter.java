package com.pradeep.nanodegree.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Pradeep on 8/10/15.
 */
public class MyAdapter extends BaseAdapter {


    String[] title;
    String[] poster;
    private Context mContext;
    static LayoutInflater inflater = null;

    public MyAdapter(Activity context, String[] titles, String[] posters) {

        mContext = context;
        this.title = titles;
        this.poster = posters;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView iv;
        TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        View row = convertView;

        ImageView iv;

        if (row == null) {

            row = inflater.inflate(R.layout.grid_item, null);

            holder = new ViewHolder();
            holder.iv = (ImageView) row.findViewById(R.id.poster);
            holder.tv = (TextView) row.findViewById(R.id.title);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
            holder.tv.setText(title[position]);
            Picasso.with(mContext).load(poster[position]).into(holder.iv);
        }
        return row;
    }

}
