package com.example.rodrigo.githubapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText search_text;
    ListView result_list;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_text = (EditText) findViewById(R.id.search_text);
        result_list = (ListView) findViewById(R.id.result_list);

        requestQueue = Volley.newRequestQueue(this);

        search_text.addTextChangedListener(new TextWatcher() {

            private Timer timer=new Timer();
            private final long DELAY = 2000;

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
                                String url = Constants.USER_URL + text + Constants.SEARCH_LOGIN + Constants.ASC_ORDER;


                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        System.out.println(response.toString());
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Log.d("Error", error.toString());
                                    }
                                });

                                requestQueue.add(jsonObjectRequest);
                            }
                        },
                        DELAY
                );
            }
        });
    }

}
