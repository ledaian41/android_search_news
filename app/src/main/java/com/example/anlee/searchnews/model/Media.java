package com.example.anlee.searchnews.model;

import static com.example.anlee.searchnews.model.Constant.BASE_URL_IMAGE;

/**
 * Created by Dell on 6/21/2017.
 */

public class Media {
    private String url;
    private String type;
    private int width;
    private int height;

    public String getUrl() {
        return BASE_URL_IMAGE + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
