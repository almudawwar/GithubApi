package com.example.rodrigo.githubapi;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rodrigo.githubapi.Classes.Repository;
import com.example.rodrigo.githubapi.Classes.SearchItem;
import com.example.rodrigo.githubapi.Classes.User;

import java.util.List;

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder>{

    private List<SearchItem> searchItemList;
    private MyItemClickListener clickListener;

    public MyRecycleAdapter(List<SearchItem> list, MyItemClickListener listener){

        searchItemList = list;
        clickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.bind(searchItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, type;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
        }

        public void bind(final SearchItem item){

            if(item instanceof User){

                title.setText(((User)item).getLogin());
                type.setText("USER");
            }else{

                title.setText(((Repository)item).getFullName());
                type.setText("REPOSITORY");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(item);
                }
            });
        }
    }

    public interface MyItemClickListener{

        void onItemClick(SearchItem item);
    }
}
