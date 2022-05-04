package com.example.mobilalkproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AruItemAdapter extends RecyclerView.Adapter<AruItemAdapter.ViewHolder> implements Filterable {
    private static final String LOG_TAG = AruItemAdapter.class.getName();
    private ArrayList<AruItem> mAruItemsData;
    private ArrayList<AruItem> mAruItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;


    AruItemAdapter(Context context, ArrayList<AruItem> itemsData) {
        this.mAruItemsData = itemsData;
        this.mAruItemsDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AruItemAdapter.ViewHolder holder, int position) {
        AruItem currentItem = mAruItemsData.get(position);

        holder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return mAruItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return aruFilter;
    }

    private Filter aruFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<AruItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mAruItemsDataAll.size();
                results.values   = mAruItemsDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (AruItem item : mAruItemsDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)
                            || item.getDesc().toLowerCase().contains(filterPattern)
                            || item.getTag().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mAruItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameText;
        private TextView mDescText;
        private TextView mTagsText;
        private ImageView mImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameText = itemView.findViewById(R.id.itemName);
            mDescText = itemView.findViewById(R.id.itemDescription);
            mTagsText = itemView.findViewById(R.id.itemTags);
            mImage = itemView.findViewById(R.id.itemImage);
        }


        public void bindTo(AruItem currentItem) {
            mNameText.setText(currentItem.getName());
            mDescText.setText(currentItem.getDesc());
            mTagsText.setText(currentItem.getTag());
            Glide.with(mContext).load(currentItem.getImageResource()).into(mImage);
        }
    };
}