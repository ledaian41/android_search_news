package com.example.anlee.searchnews.api;

import com.example.anlee.searchnews.model.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Dell on 6/21/2017.
 */

public interface ArticleApi {
    @GET("articlesearch.json")
    Call<SearchResult> search(@QueryMap(encoded = true) Map<String, String> options);
}
