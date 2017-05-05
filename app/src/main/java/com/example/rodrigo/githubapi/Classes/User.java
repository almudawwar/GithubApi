package com.example.rodrigo.githubapi.Classes;

public class User extends SearchItem{

    private String login;
    private String avatar_url;
    private String followers_url;

    public User(long id, String url, String login){

        super(id, url);
        this.login = login;
    }

    public String getAvatar() {
        return avatar_url;
    }

    public void setAvatar(String avatar) {
        this.avatar_url = avatar;
    }

    public String getFollowers() {
        return followers_url;
    }

    public void setFollowers(String followers) {
        this.followers_url = followers;
    }

    public String getLogin() {
        return login;
    }

}
