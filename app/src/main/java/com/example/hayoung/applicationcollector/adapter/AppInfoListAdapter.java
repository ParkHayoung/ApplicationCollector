package com.example.hayoung.applicationcollector.adapter;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hayoung.applicationcollector.R;
import com.example.hayoung.applicationcollector.model.AppInfoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by hayoung on 2017. 12. 22..
 **/

public class AppInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private List<AppInfoItem> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info_view, parent, false);
        return new AppItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AppItemViewHolder) {
            bindAppItemViewHolder((AppItemViewHolder)holder, position);
        }
    }

    @SuppressLint("SetTextI18n")
    private void bindAppItemViewHolder(AppItemViewHolder holder, int position) {
        AppInfoItem item = items.get(position);
        holder.appIconView.setImageDrawable(item.getAppIcon());
        holder.appNameTextView.setText(item.getAppName());
        holder.numberTextView.setText(String.valueOf(position + 1) + ".");

        UsageStats usageStats = item.getUsageStats();
        if (usageStats == null || usageStats.getLastTimeUsed() == 0) {
            holder.usageTimeTextView.setVisibility(View.GONE);
            holder.lastUsedTimeTextView.setVisibility(View.GONE);
        } else {
            holder.usageTimeTextView.setVisibility(View.VISIBLE);
            holder.lastUsedTimeTextView.setVisibility(View.VISIBLE);

            // 평균 하루 사용 시간
            float usageMin = item.getUsageStats().getTotalTimeInForeground() / (1000f * 60);
            float useDay = (item.getUsageStats().getLastTimeStamp() - item.getUsageStats().getFirstTimeStamp()) / (1000f * 86400);
            int averageUsageMin = (int)(usageMin / useDay);
            holder.usageTimeTextView.setText(String.format(Locale.getDefault(), "하루평균 %s분", averageUsageMin));

            // 최근 사용 일자
            int hours = (int)((System.currentTimeMillis() - usageStats.getLastTimeUsed()) / (1000f * 60 * 60));
            if (hours == 0) {
                holder.lastUsedTimeTextView.setText("조금 전");
            } else {
                if (hours > 24) {
                    int days = (int)(hours / 24f);
                    holder.lastUsedTimeTextView.setText(String.format(Locale.getDefault(), "%d일 전 마지막", days));
                } else {
                    holder.lastUsedTimeTextView.setText(String.format(Locale.getDefault(), "%d시간 전 마지막", hours));
                }

            }
        }
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getPackageName().hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<AppInfoItem> items) {
        this.items = items;
    }

    public List<AppInfoItem> getItems() {
        return items;
    }

    private static class AppItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView appIconView;
        private TextView appNameTextView;
        private TextView numberTextView;
        private TextView usageTimeTextView;
        private TextView lastUsedTimeTextView;

        AppItemViewHolder(View itemView) {
            super(itemView);

            appIconView = itemView.findViewById(R.id.app_icon);
            appNameTextView = itemView.findViewById(R.id.app_name);
            numberTextView = itemView.findViewById(R.id.number);
            usageTimeTextView = itemView.findViewById(R.id.usage_time);
            lastUsedTimeTextView = itemView.findViewById(R.id.last_used_time);
        }


    }


}
