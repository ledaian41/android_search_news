package com.example.anlee.searchnews.model;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.anlee.searchnews.activity.MainActivity;
import com.example.anlee.searchnews.utils.UiUtils;

import static com.example.anlee.searchnews.model.Constant.BASE_URL_IMAGE;

/**
 * Created by An Lee on 6/21/2017.
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
        return UiUtils.getScreenWidth() / 2;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return (UiUtils.getScreenWidth() / 2) * height / width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
