package com.example.rodrigo.githubapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rodrigo.githubapi.Classes.Repository;
import com.example.rodrigo.githubapi.Classes.SearchItem;
import com.example.rodrigo.githubapi.Classes.User;

import java.util.List;


public class ListAdapter extends BaseAdapter {

    private List<SearchItem> mItemsList;
    private LayoutInflater mInflater;
    private Context mContext;

    public ListAdapter(Context context, List<SearchItem> items){

        mContext = context;
        mItemsList = items;
    }

    @Override
    public int getCount() {
        return mItemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItemsList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(mInflater == null){
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = mInflater.inflate(R.layout.list_item_layout, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView type = (TextView) view.findViewById(R.id.type);

        SearchItem item = mItemsList.get(i);

        if(item instanceof User) {
            title.setText(((User)item).getLogin());
            type.setText("USER");
        }else {
            title.setText(((Repository)item).getFullName());
            type.setText("REPOSITORY");
        }


        return view;
    }
}
