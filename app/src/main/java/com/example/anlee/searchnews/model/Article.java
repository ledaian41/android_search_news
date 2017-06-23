package com.example.anlee.searchnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by An Lee on 6/21/2017.
 */

public class Article implements Parcelable {
    @SerializedName("web_url")
    private String url;
    @SerializedName("snippet")
    private String snippet;
    @SerializedName("lead_paragraph")
    private String paragraph;
    @SerializedName("multimedia")
    private List<Media> multimedia;

    protected Article(Parcel in) {
        url = in.readString();
        snippet = in.readString();
        paragraph = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public List<Media> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Media> multimedia) {
        this.multimedia = multimedia;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(snippet);
        dest.writeString(paragraph);
    }
}
