package com.example.anlee.searchnews.utils;

import android.content.res.Resources;

/**
 * Created by An Lee on 6/24/2017.
 */

public class UiUtils {
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreeHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
