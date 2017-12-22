package com.example.hayoung.applicationcollector.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hayoung.applicationcollector.R;
import com.example.hayoung.applicationcollector.model.AppInfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hayoung on 2017. 12. 22..
 **/

public class AppInfoListAdapter extends RecyclerView.Adapter<AppInfoListAdapter.ViewHolder> {
    private List<AppInfoItem> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppInfoItem item = items.get(position);

        holder.items = items;
        holder.appIconView.setImageDrawable(item.getAppIcon());
        holder.appNameTextView.setText(item.getAppName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<AppInfoItem> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public List<AppInfoItem> items;
        public ImageView appIconView;
        public TextView appNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            appIconView = itemView.findViewById(R.id.app_icon);
            appNameTextView = itemView.findViewById(R.id.app_name);
        }


    }


}
