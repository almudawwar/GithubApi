package com.example.rodrigo.githubapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    EditText search_text;
    ListView result_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_text = (EditText) findViewById(R.id.search_text);
        result_list = (ListView) findViewById(R.id.result_list);
    }
}
