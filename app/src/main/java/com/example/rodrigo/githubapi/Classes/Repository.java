package com.example.rodrigo.githubapi.Classes;


public class Repository extends SearchItem{

    private String mFullName;

    public Repository(long id, String url, String fullName) {
        super(id, url);

        mFullName = fullName;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String FullName) {
        mFullName = FullName;
    }
}
