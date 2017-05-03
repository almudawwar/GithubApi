package com.example.rodrigo.githubapi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.githubapi.Classes.GitResponse;
import com.example.rodrigo.githubapi.Classes.SearchItem;
import com.example.rodrigo.githubapi.Classes.User;
import com.example.rodrigo.githubapi.Constants;
import com.example.rodrigo.githubapi.MyRecycleAdapter;
import com.example.rodrigo.githubapi.R;
import com.example.rodrigo.githubapi.Remote.GithubService;
import com.example.rodrigo.githubapi.Remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements MyRecycleAdapter.MyItemClickListener{

    private EditText mSearchText;

    private RecyclerView mRecyclerView;
    private MyRecycleAdapter mAdapter;

    private List<SearchItem> mSearchItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchText = (EditText) findViewById(R.id.search_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.result_recycler_view);

        mAdapter = new MyRecycleAdapter(mSearchItemList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setListeners();
    }


    private void setListeners(){
        mSearchText.addTextChangedListener(new TextWatcher() {

            private Timer timer=new Timer();
            private final long DELAY = 1000;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {

                                //In case of the user writes whitespaces, it replaces them for "+", so an error doesn't occur
                                String text = mSearchText.getText().toString().replace(" ", "+");

                                String userSearchUrl =
                                        Constants.USER_SEARCH  + text + Constants.SEARCH_LOGIN + Constants.ASC_ORDER;
                                String repSearchUrl = Constants.REP_SEARCH + text +  Constants.ASC_ORDER;

//                                MySingleton.getInstance(MainActivity.this).addToRequestQueue(makeUsersRequest(userSearchUrl));
//                                MySingleton.getInstance(MainActivity.this).addToRequestQueue(makeRepositoriesRequest(repSearchUrl));

                                //Retrofit

                                GithubService githubService = RetrofitClient.getClient(Constants.BASE_URL).create(GithubService.class);

                                Call<GitResponse> users_call = githubService.getUsers(text);
                                //Call<GitResponse> repository_call = githubService.getRepositories(text);

                                users_call.enqueue(new Callback<GitResponse>() {
                                    @Override
                                    public void onResponse(Call<GitResponse> call, retrofit2.Response<GitResponse> response) {

                                        fillList(response.body().items);

                                    }

                                    @Override
                                    public void onFailure(Call<GitResponse> call, Throwable t) {

                                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        },
                        DELAY
                );
            }
        });
    }

    private void fillList(List<SearchItem> items){

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
