package com.example.hayoung.applicationcollector.model;

import android.graphics.drawable.Drawable;

/**
 * Created by hayoung on 2017. 12. 22..
 **/

public class AppInfoItem {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private long installedTime;
    private UsageStat usageStat;

    public static class UsageStat {
        public long totalUsageTime;
        public long lastUsedTime;
        public long startTime;
        public long endTime;
        public float averageUsageMin;

        public UsageStat(long totalUsageTime, long lastUsedTime, long startTime, long endTime) {
            this.totalUsageTime = totalUsageTime;
            this.lastUsedTime = lastUsedTime;
            this.startTime = startTime;
            this.endTime = endTime;

            float usageMin = totalUsageTime / (1000f * 60);
            float usageDay = (endTime - startTime) / (1000f * 86400);
            averageUsageMin = (usageMin / usageDay);
        }
    }

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

    public UsageStat getUsageStats() {
        return usageStat;
    }

    public void setUsageStats(UsageStat usageStats) {
        this.usageStat = usageStats;
    }
}
