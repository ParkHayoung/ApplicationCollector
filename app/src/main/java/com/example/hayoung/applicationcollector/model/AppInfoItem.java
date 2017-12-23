package com.example.hayoung.applicationcollector.model;

import android.app.usage.UsageStats;
import android.graphics.drawable.Drawable;

/**
 * Created by hayoung on 2017. 12. 22..
 **/

public class AppInfoItem {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private long installedTime;
    private UsageStats usageStats;

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

    public long getInstalledTime() {
        return installedTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setInstalledTime(long installedTime) {
        this.installedTime = installedTime;
    }

    public UsageStats getUsageStats() {
        return usageStats;
    }

    public void setUsageStats(UsageStats usageStats) {
        this.usageStats = usageStats;
    }
}
