package com.example.rodrigo.githubapi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.rodrigo.githubapi.Classes.GitResponse;
import com.example.rodrigo.githubapi.Classes.RepositoryList;
import com.example.rodrigo.githubapi.Classes.SearchItem;
import com.example.rodrigo.githubapi.Classes.User;
import com.example.rodrigo.githubapi.Classes.UserList;
import com.example.rodrigo.githubapi.Constants;
import com.example.rodrigo.githubapi.MyRecycleAdapter;
import com.example.rodrigo.githubapi.R;
import com.example.rodrigo.githubapi.Remote.GithubService;
import com.example.rodrigo.githubapi.Remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MyRecycleAdapter.MyItemClickListener{

    private SearchView mSearchView;
    private TabLayout mTabLayout;

    private RecyclerView mRecyclerView;
    private MyRecycleAdapter mAdapter;

    private List<SearchItem> mSearchItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.result_recycler_view);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        mAdapter = new MyRecycleAdapter(mSearchItemList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setListeners();
    }


    private void setListeners(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //In case of the user writes whitespaces, it replaces them for "+", so an error doesn't occur
                String text = mSearchView.getQuery().toString().replace(" ", "+");

                GithubService githubService = RetrofitClient.getClient(Constants.BASE_URL).create(GithubService.class);

                switch (mTabLayout.getSelectedTabPosition()){

                    case 0: {
                        Call<UserList> userCall = githubService.getUsers(text);

                        userCall.enqueue(new Callback<UserList>() {
                            @Override
                            public void onResponse(Call<UserList> call, Response<UserList> response) {

                                fillList(response.body().getItems());
                            }

                            @Override
                            public void onFailure(Call<UserList> call, Throwable t) {

                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    }

                    case 1: {
                        Call<RepositoryList> repositoryCall = githubService.getRepositories(text);

                        repositoryCall.enqueue(new Callback<RepositoryList>() {
                            @Override
                            public void onResponse(Call<RepositoryList> call, Response<RepositoryList> response) {

                                fillList(response.body().getItems());
                            }

                            @Override
                            public void onFailure(Call<RepositoryList> call, Throwable t) {

                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mSearchView.isActivated()) {
                    mSearchView.onActionViewExpanded();
                }

            }
        });
    }

    private void fillList(List<? extends SearchItem> items){

        mSearchItemList.clear();

        for(int i = 0; i < items.size(); i++){

            mSearchItemList.add(items.get(i));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(SearchItem item) {

        if(item instanceof User) {
            //Send the URL of the selected mUser to the details activity
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            intent.putExtra("userUrl", item.getUrl());

            startActivity(intent);
        }else
            Toast.makeText(MainActivity.this, "Details only for users.", Toast.LENGTH_SHORT).show();
    }
}
