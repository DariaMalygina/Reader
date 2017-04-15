package com.example.asus.reader.gui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asus.reader.R;
import com.example.asus.reader.db.Feed;

import java.util.ArrayList;

final class FeedAdapter extends ArrayAdapter<Feed> {



    FeedAdapter(final Context context, final ArrayList<Feed> feeds) {
        super(context, 0, feeds);
    }

    private static class ViewHolder {
        TextView name;
        TextView url;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final Feed feed = getItem(position);
        final ViewHolder viewHolder;
        final LayoutInflater inflater;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_feed, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name_feed);
            viewHolder.url = (TextView) convertView.findViewById(R.id.tv_url_feed);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(feed != null) {
            viewHolder.name.setText(feed.getNameFeed());
            viewHolder.url.setText(feed.getUrlFeed());
        }


        return convertView;
    }


}
