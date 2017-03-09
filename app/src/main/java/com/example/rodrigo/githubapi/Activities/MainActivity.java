package com.example.rodrigo.githubapi.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rodrigo.githubapi.Classes.MySingleton;
import com.example.rodrigo.githubapi.Classes.Repository;
import com.example.rodrigo.githubapi.Classes.SearchItem;
import com.example.rodrigo.githubapi.Classes.User;
import com.example.rodrigo.githubapi.Constants;
import com.example.rodrigo.githubapi.MyRecycleAdapter;
import com.example.rodrigo.githubapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MyRecycleAdapter.MyItemClickListener{

    private EditText mSearchText;

    private RecyclerView mRecyclerView;
    private MyRecycleAdapter mAdapter;

    private List<SearchItem> mSearchItemList = new ArrayList<>();

    RequestQueue requestQueue;

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

        requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();

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

                                //In case of the mUser writes whitespaces, it replaces them for "+", so an error doesn't occur
                                String text = mSearchText.getText().toString().replace(" ", "+");

                                String userSearchUrl =
                                        Constants.USER_SEARCH  + text + Constants.SEARCH_LOGIN + Constants.ASC_ORDER;
                                String repSearchUrl = Constants.REP_SEARCH + text +  Constants.ASC_ORDER;

                                MySingleton.getInstance(MainActivity.this).addToRequestQueue(makeUsersRequest(userSearchUrl));
                                MySingleton.getInstance(MainActivity.this).addToRequestQueue(makeRepositoriesRequest(repSearchUrl));
                            }
                        },
                        DELAY
                );
            }
        });
    }

    private JsonObjectRequest makeUsersRequest(String userSearchUrl){

        JsonObjectRequest usersRequest =
                new JsonObjectRequest(Request.Method.GET, userSearchUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        mSearchItemList.clear();

                        fillList(response);

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Error", error.toString());
                    }
                });

        return usersRequest;
    }

    private JsonObjectRequest makeRepositoriesRequest(String repSearchUrl){

        JsonObjectRequest repRequest =
                new JsonObjectRequest(Request.Method.GET, repSearchUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        fillList(response);

                        Collections.sort(mSearchItemList, SearchItem.getComparator());

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        if(cm.getActiveNetworkInfo() == null){
                            Toast.makeText(MainActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                        }

                        Log.d("Error", error.toString());
                    }
                });

        return repRequest;
    }

    private void fillList(JSONObject response){

        try {
            JSONArray itemsArray = response.getJSONArray("items");

            for(int i = 0; i < itemsArray.length(); i++){

                JSONObject object = itemsArray.getJSONObject(i);

                SearchItem item = null;

                try {

                    item = new User(object.getLong("id"), object.getString("url"), object.getString("login"));

                }catch(JSONException ex){

                    if(object.getString("full_name") != null) {
                        item = new Repository(object.getLong("id"), object.getString("url"), object.getString("full_name"));
                    }else
                        ex.printStackTrace();

                }

                mSearchItemList.add(item);
            }

            if(mSearchItemList.isEmpty()){

                Toast.makeText(MainActivity.this, "Nothing was found.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
