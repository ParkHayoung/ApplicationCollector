package com.example.hayoung.applicationcollector;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hayoung.applicationcollector.adapter.AppInfoListAdapter;
import com.example.hayoung.applicationcollector.model.AppInfoItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<AppInfoItem> appInfoItemList = new ArrayList<>();
    private AppInfoListAdapter appInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, new LinearLayoutManager(this).getOrientation()));

        getUserApplicationList();

        recyclerView.setAdapter(appInfoListAdapter);
    }

    private void getUserApplicationList() {
        appInfoListAdapter = new AppInfoListAdapter();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        ApplicationInfo applicationInfo;

        for (ResolveInfo pkgAppList : pkgAppsList) {
            applicationInfo  = pkgAppList.activityInfo.applicationInfo;
            String packageName = applicationInfo.packageName;

            if (!packageName.contains("google") && !packageName.contains("kt")
                    && !packageName.contains("samsung") && !packageName.contains("sec")
                    && !packageName.contains("setting") && !packageName.contains("vending")) {
                String appName = getPackageManager().getApplicationLabel(applicationInfo).toString();
                Drawable appIcon = applicationInfo.loadIcon(getPackageManager());

                appInfoItemList.add(new AppInfoItem(appName, appIcon));
            }
        }
        appInfoListAdapter.setItems(appInfoItemList);
    }
}
