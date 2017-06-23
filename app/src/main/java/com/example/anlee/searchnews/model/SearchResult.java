package com.example.anlee.searchnews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by An Lee on 6/21/2017.
 */

public class SearchResult {
    @SerializedName("docs")
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

}
