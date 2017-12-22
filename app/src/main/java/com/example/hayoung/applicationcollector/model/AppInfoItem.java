package com.example.hayoung.applicationcollector.model;

import android.graphics.drawable.Drawable;

/**
 * Created by hayoung on 2017. 12. 22..
 **/

public class AppInfoItem {
    private String appName;
    private Drawable appIcon;

    public AppInfoItem(String appName, Drawable appIcon) {
        this.appName = appName;
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
