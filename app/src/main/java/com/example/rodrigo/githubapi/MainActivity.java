package com.example.rodrigo.githubapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText search_text;
    ListView result_list;
    ListAdapter adapter;
    List<SearchItem> list = new ArrayList<>();

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_text = (EditText) findViewById(R.id.search_text);
        result_list = (ListView) findViewById(R.id.result_list);

        adapter = new ListAdapter(this, list);
        result_list.setAdapter(adapter);

        requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();

        setListeners();
    }


    private void setListeners(){
        search_text.addTextChangedListener(new TextWatcher() {

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

                                String text = search_text.getText().toString();
                                String url = Constants.USER_SEARCH  + text + Constants.SEARCH_LOGIN + Constants.ASC_ORDER;


                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        list.clear();

                                        try {
                                            JSONArray users_array = response.getJSONArray("items");

                                            for(int i = 0; i < users_array.length(); i++){

                                                JSONObject object = users_array.getJSONObject(i);

                                                SearchItem item = new SearchItem(object.getLong("id"));
                                                item.setUrl(object.getString("url"));

                                                if(object.getString("login") == null) {
                                                    item.setType(1);
                                                    item.setFull_name(object.getString("full_name"));
                                                }else {
                                                    item.setType(0);
                                                    item.setLogin(object.getString("login"));
                                                }

                                                list.add(item);
                                            }

                                            if(users_array.length() == 0){

                                                Toast.makeText(MainActivity.this, "Nothing was found.", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        adapter.notifyDataSetChanged();
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Log.d("Error", error.toString());
                                    }
                                });

                                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
                            }
                        },
                        DELAY
                );
            }
        });


        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Send the URL of the selected user to the details activity
                Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
                intent.putExtra("user_url", list.get(i).getUrl());

                startActivity(intent);
            }
        });
    }
}
