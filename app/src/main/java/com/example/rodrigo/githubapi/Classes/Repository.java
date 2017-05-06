package com.example.rodrigo.githubapi.Classes;


public class Repository extends SearchItem{

    private String full_name;

    public Repository(long id, String url, String fullName) {
        super(id, url);

        full_name = fullName;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String FullName) {
        full_name = FullName;
    }
}
