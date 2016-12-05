package com.example.rodrigo.githubapi.Classes;

public class User extends SearchItem{

    private String mLogin;
    private String mAvatar;
    private int mFollowers;

    public User(long id, String url, String login){

        super(id, url);
        mLogin = login;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public int getFollowers() {
        return mFollowers;
    }

    public void setFollowers(int followers) {
        mFollowers = followers;
    }

    public String getLogin() {
        return mLogin;
    }

}
