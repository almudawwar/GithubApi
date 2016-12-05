package com.example.rodrigo.githubapi.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rodrigo.githubapi.Classes.MySingleton;
import com.example.rodrigo.githubapi.Classes.User;
import com.example.rodrigo.githubapi.Constants;
import com.example.rodrigo.githubapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailsActivity extends AppCompatActivity {

    String mUserUrl;
    ImageView mAvatar;
    TextView mUsername, mStars, mFollowers;
    Button mBackButton;

    ProgressDialog mDialog;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mUserUrl = getIntent().getStringExtra("userUrl");

        setComponents();

        setListeners();

        MySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, mUserUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    mUser = new User(response.getLong("id"), response.getString("url"), response.getString("login"));

                    mUser.setFollowers(response.getInt("followers"));
                    mUser.setAvatar(response.getString("avatar_url"));

                    mUsername.setText(mUser.getLogin());
                    mFollowers.setText("Followers: " + mUser.getFollowers());

                    makeAvatarRequest();
                    makeStarsRequest();

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

    private void makeStarsRequest(){

        String url = Constants.USER + mUser.getLogin() + Constants.STARRED;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                mStars.setText("Stars: " + response.length());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(UserDetailsActivity.this, "Some problem occured.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(arrayRequest);
    }

    private void makeAvatarRequest(){

        ImageRequest avatarRequest = new ImageRequest(mUser.getAvatar(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                mAvatar.setImageBitmap(response);

                mDialog.cancel();
            }
        }, 0, 0, null,
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(UserDetailsActivity.this, "Some problem occured.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });

        MySingleton.getInstance(UserDetailsActivity.this).addToRequestQueue(avatarRequest);
    }

    private void setComponents(){

        mAvatar = (ImageView) findViewById(R.id.avatar);
        mUsername = (TextView) findViewById(R.id.username);
        mStars = (TextView) findViewById(R.id.stars);
        mFollowers = (TextView) findViewById(R.id.followers);
        mBackButton = (Button) findViewById(R.id.back_button);

        mDialog = ProgressDialog.show(this, "Please wait", "Loading...", true, false);
    }

    private void setListeners(){

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
