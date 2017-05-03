package com.example.rodrigo.githubapi.Classes;

import java.util.Comparator;

public class SearchItem {

    private long id;
    private String url;

    public static Comparator<SearchItem> getComparator(){

        Comparator mComparator = new Comparator<SearchItem>() {
            @Override
            public int compare(SearchItem item, SearchItem item2) {

                long id1 = item.getId();
                long id2 = item2.getId();

                if(id1 < id2)
                    return -1;
                else if(id1 > id2)
                    return 1;
                else
                    return 0;
            }
        };

        return mComparator;
    }

    public SearchItem(long id, String url){
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
