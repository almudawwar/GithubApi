package com.example.rodrigo.githubapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rodrigo on 11/11/2016.
 */

public class ListAdapter extends BaseAdapter {

    private List<User> list_items;
    private LayoutInflater inflater;
    private Context context;

    public ListAdapter(Context context, List<User> items){

        this.context = context;
        list_items = items;
    }

    @Override
    public int getCount() {
        return list_items.size();
    }

    @Override
    public Object getItem(int i) {
        return list_items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.list_item_layout, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView url = (TextView) view.findViewById(R.id.url);

        User user = list_items.get(i);

        title.setText(user.getLogin());
        url.setText(user.getUrl());

        return view;
    }
}
