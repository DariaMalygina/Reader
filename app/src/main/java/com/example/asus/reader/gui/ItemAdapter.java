package com.example.asus.reader.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asus.reader.R;
import com.example.asus.reader.db.Item;

import java.util.ArrayList;

final class ItemAdapter extends ArrayAdapter<Item> {

    ItemAdapter(final Context context, final ArrayList<Item> items) {
        super(context, 0, items);
    }

    private static class ViewHolder {
        TextView title;
        TextView text;
        TextView date;
        TextView url;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final Item item = getItem(position);
        final ViewHolder viewHolder;
        final LayoutInflater inflater;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.items_list_view_items, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitleItem);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvTextItem);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDateItem);
            viewHolder.url = (TextView) convertView.findViewById(R.id.tvUrlItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(item != null) {
            viewHolder.title.setText(item.getTitleItem());
            viewHolder.text.setText(Html.fromHtml(item.getTextItem()));
            viewHolder.date.setText(item.getDateItem());
            viewHolder.url.setText(item.getUrlItem());
        }


        return convertView;
    }


}