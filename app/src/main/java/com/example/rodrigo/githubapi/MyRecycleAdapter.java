package com.example.rodrigo.githubapi;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.githubapi.Classes.Repository;
import com.example.rodrigo.githubapi.Classes.SearchItem;
import com.example.rodrigo.githubapi.Classes.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder>{

    private List<SearchItem> mSearchItemList;
    private MyItemClickListener mClickListener;
    private Context mContext;

    public MyRecycleAdapter(List<SearchItem> list, MyItemClickListener listener, Context context){

        mSearchItemList = list;
        mClickListener = listener;
        mContext = context;
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
        ImageView avatar;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            avatar= (ImageView) itemView.findViewById(R.id.avatar_preview);
        }

        public void bind(final SearchItem item){

            if(item instanceof User){

                title.setText(((User) item).getLogin());
                Picasso.with(mContext)
                        .load(((User) item).getAvatar())
                        .fit()
                        .into(avatar);
            }else{

                title.setText(((Repository)item).getFullName());
            }

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
