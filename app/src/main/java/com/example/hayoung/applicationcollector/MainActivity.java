package com.example.hayoung.applicationcollector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hayoung.applicationcollector.adapter.AppInfoListAdapter;
import com.example.hayoung.applicationcollector.model.AppInfoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_ACTIVITY_APP_USAGE = 100;

    // 구글플레이 패키지
    private static final String PKG_GOOGLE_PLAY = "com.android.vending";

    // 원스토어 패키지
    // SKT: com.skt.skaf.A000Z00040
    private static final String PKG_ONE_SKT = "com.skt.skaf.A000Z00040";
    // KT: com.kt.olleh.storefront
    private static final String PKG_ONE_KT = "com.kt.olleh.storefront";
    // LG: android.lgt.appstore
    private static final String PKG_ONE_LG = "android.lgt.appstore";

    private List<AppInfoItem> appInfoItemList = new ArrayList<>();
    private AppInfoListAdapter appInfoListAdapter;
    private Map<String, UsageStats> usageStatsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, new LinearLayoutManager(this).getOrientation()));

        appInfoListAdapter = new AppInfoListAdapter();
        recyclerView.setAdapter(appInfoListAdapter);

        getUserApplicationList();

        if (!isAppUsagePermGranted()) {
            showAppUsagePermGuideDialog();
        } else {
            loadAppUsageStatsAndReorderList();
        }
    }

    private void goToAppUsageSetting() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, REQ_ACTIVITY_APP_USAGE);
    }

    private void showAppUsagePermGuideDialog() {
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("사용자의 앱 사용패턴을 분석하기 위해서는 사용정보 제공을 허용해 주셔야 합니다.\n[확인] 을 눌러 이동된 화면에서 이 앱의 사용정보 접근을 허용해 주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAppUsageSetting();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reorderListByInstalledTime();
                    }
                })
                .show();
    }

    private boolean isAppUsagePermGranted() {
        AppOpsManager appOps = (AppOpsManager)getSystemService(APP_OPS_SERVICE);
        if (appOps == null) {
            return false;
        }

        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void loadAppUsageStatsAndReorderList() {
        long endTime = Calendar.getInstance().getTimeInMillis();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -1);

        @SuppressLint("WrongConstant")
        UsageStatsManager usageStatsManager = (UsageStatsManager)getSystemService("usagestats");
        if (usageStatsManager != null) {
            usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startCalendar.getTimeInMillis(), endTime);
            if (usageStatsMap.size() > 0) {
                for (AppInfoItem item : appInfoItemList) {
                    item.setUsageStats(usageStatsMap.get(item.getPackageName()));
                }
                reorderListByAppTotalUsageTimeInMonth();
            }
        }
    }

    private void getUserApplicationList() {
        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : pkgAppsList) {
            String installer = packageManager.getInstallerPackageName(info.activityInfo.packageName);
            if (!PKG_GOOGLE_PLAY.equals(installer)
                    && !PKG_ONE_KT.equals(installer)
                    && !PKG_ONE_LG.equals(installer)
                    && !PKG_ONE_SKT.equals(installer)) {
                // 마켓(플레이스토어, 원스토어)에서 설치되지 않은 앱은 제외
                continue;
            }
            ApplicationInfo applicationInfo  = info.activityInfo.applicationInfo;
            String appName = packageManager.getApplicationLabel(applicationInfo).toString();
            Drawable appIcon = applicationInfo.loadIcon(getPackageManager());
            long installedTime;
            try {
                PackageInfo pkgInfo = getPackageManager().getPackageInfo(info.activityInfo.packageName, 0);
                installedTime = pkgInfo.firstInstallTime;
            } catch (PackageManager.NameNotFoundException e) {
                installedTime = 0;
            }
            AppInfoItem appItem = new AppInfoItem(appName, appIcon);
            appItem.setInstalledTime(installedTime);
            appItem.setPackageName(applicationInfo.packageName);
            appInfoItemList.add(appItem);
        }
    }

    private void reorderListByAppTotalUsageTimeInMonth() {
        Collections.sort(appInfoItemList, new Comparator<AppInfoItem>() {
            @Override
            public int compare(AppInfoItem o1, AppInfoItem o2) {
                long time1 = 0;
                if (o1.getUsageStats() != null) {
                    time1 = o1.getUsageStats().getTotalTimeInForeground();
                }
                long time2 = 0;
                if (o2.getUsageStats() != null) {
                    time2 = o2.getUsageStats().getTotalTimeInForeground();
                }
                if (time1 == time2) {
                    return 0;
                }
                return time1 < time2 ? 1 : -1;
            }
        });

        if (appInfoListAdapter.getItemCount() > 0) {
            appInfoListAdapter.notifyItemRangeChanged(0, appInfoListAdapter.getItemCount());
        } else {
            appInfoListAdapter.setItems(appInfoItemList);
            appInfoListAdapter.notifyDataSetChanged();
        }
    }

    private void reorderListByInstalledTime() {
        Collections.sort(appInfoItemList, new Comparator<AppInfoItem>() {
            @Override
            public int compare(AppInfoItem o1, AppInfoItem o2) {
                if (o1.getInstalledTime() == o2.getInstalledTime()) {
                    return 0;
                }
                return o1.getInstalledTime() < o2.getInstalledTime() ? 1 : -1;
            }
        });

        appInfoListAdapter.setItems(appInfoItemList);
        appInfoListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ACTIVITY_APP_USAGE) {
            if (isAppUsagePermGranted()) {
                loadAppUsageStatsAndReorderList();
            } else {
                showAppUsagePermGuideDialog();
            }
        }
    }
}
