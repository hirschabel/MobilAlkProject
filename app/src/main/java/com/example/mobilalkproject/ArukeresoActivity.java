package com.example.mobilalkproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArukeresoActivity extends AppCompatActivity {
    private static final String LOG_TAG = ArukeresoActivity.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private boolean direction = true;
    private int queryLimit = 10;
    private RecyclerView mRecyclerView;
    private ArrayList<AruItem> mItemList;
    private AruItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private NotificationHandler mHandler;

    private int gridNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arukereso);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        }



        mHandler = new NotificationHandler(this);
        mHandler.cancel();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();
        mAdapter = new AruItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);


        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");


        queryData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);


    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null) {
                return;
            }

            switch(action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 100;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 5;
                    break;
            }

            queryData();
        }
    };

    private void queryData() {
        mItemList.clear();

        //mItems.whereEqualTo()
        mItems.orderBy("name").limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                AruItem item = document.toObject(AruItem.class);
                mItemList.add(item);
            }

            if (mItemList.size() == 0) {
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });

    }

    private void queryDataDescend() {
        mItemList.clear();

        //mItems.whereEqualTo()
        mItems.orderBy("name", Query.Direction.DESCENDING).limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                AruItem item = document.toObject(AruItem.class);
                mItemList.add(item);
            }

            if (mItemList.size() == 0) {
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        String[] itemsName = getResources().getStringArray(R.array.aru_item_names);
        String[] itemsDesc = getResources().getStringArray(R.array.aru_item_desc);
        String[] itemsTag = getResources().getStringArray(R.array.aru_item_tag);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.aru_item_pic);

        // mItemList.clear();

        for (int i = 0; i < itemsName.length; i++) {
            mItems.add(new AruItem(
                            itemsName[i],
                            itemsDesc[i],
                            itemsTag[i],
                            itemsImageResource.getResourceId(i, 0)));
            /*
            mItemList.add(new AruItem(
                    itemsName[i],
                    itemsDesc[i],
                    itemsTag[i],
                    itemsImageResource.getResourceId(i, 0)
            ));
             */
        }



        itemsImageResource.recycle();
        // mAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }
    /*

     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.delete_user_button:
                user.delete();
                finish();
                return true;
            case R.id.sort_items:
                mHandler.send("hehe");
                if (direction) {
                    queryDataDescend();
                    item.setIcon(R.drawable.ic_text_sort_reverse);
                } else {
                    queryData();
                    item.setIcon(R.drawable.ic_text_sort_normal);
                }
                direction = !direction;

                return true;
            case R.id.change_password_button:
                Intent intent = new Intent(this, PasswordChangeActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }
}