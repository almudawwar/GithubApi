package com.example.rodrigo.githubapi;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailsActivity extends AppCompatActivity {

    String user_url;
    ImageView avatar;
    TextView username, stars, followers;
    Button back_button;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        user_url = getIntent().getStringExtra("user_url");

        setComponents();

        setListeners();

        MySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, user_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    user = new User(response.getString("login"), response.getString("url"));

                    user.setFollowers(response.getInt("followers"));
                    user.setAvatar(response.getString("avatar_url"));

                    username.setText(user.getLogin());
                    followers.setText(user.getFollowers() + "");

                    makeAvatarRequest();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    private void makeAvatarRequest(){

        ImageRequest avatarRequest = new ImageRequest(user.getAvatar(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                avatar.setImageBitmap(response);
            }
        }, 0, 0, null,
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(avatarRequest);
    }

    private void setComponents(){

        avatar = (ImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        stars = (TextView) findViewById(R.id.stars);
        followers = (TextView) findViewById(R.id.followers);
        back_button = (Button) findViewById(R.id.back_button);
    }

    private void setListeners(){

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
