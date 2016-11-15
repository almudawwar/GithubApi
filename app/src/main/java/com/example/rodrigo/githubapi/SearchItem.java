package com.example.rodrigo.githubapi;

/**
 * Created by Rodrigo on 15/11/2016.
 */

public class SearchItem {

    private long id;
    private String full_name;
    private String login;
    private String url;

    //This will tell if it's a user or repository  - 0 user  - 1 repository
    private int type;

    public SearchItem(long id){
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
