package com.example.rodrigo.githubapi;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rodrigo.githubapi.Classes.SearchItem;

import java.util.List;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder>{

    private List<SearchItem> mSearchItemList;
    private MyItemClickListener mClickListener;

    public MyRecycleAdapter(List<SearchItem> list, MyItemClickListener listener){

        mSearchItemList = list;
        mClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.bind(mSearchItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSearchItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
        }

        public void bind(final SearchItem item){

            //if(item instanceof User){

                title.setText(item.getUrl());
//            }else{
//
//                title.setText(((Repository)item).getFullName());
//                type.setText("REPOSITORY");
//            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(item);
                }
            });
        }
    }

    public interface MyItemClickListener{

        void onItemClick(SearchItem item);
    }
}
