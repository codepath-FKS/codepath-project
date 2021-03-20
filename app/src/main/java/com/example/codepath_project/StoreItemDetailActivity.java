package com.example.codepath_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class StoreItemDetailActivity extends AppCompatActivity {

    public static final String TAG = "StoreItemDetailActivity";
    private ImageView ivStoreItem;
    private TextView tvStoreItemName;
    private TextView tvStoreItemCost;
    private Button btnPurchase;
    private Button btnEquip;
    private StoreItem storeItem;
    private int userTotalPoints;
    private int itemPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_item_detail);
        tvStoreItemName = findViewById(R.id.tvStoreItemName);
        tvStoreItemCost = findViewById(R.id.tvStoreItemCost);
        ivStoreItem = findViewById(R.id.ivStoreItem);
        btnEquip = findViewById(R.id.btnEquip);
        btnPurchase = findViewById(R.id.btnPurchase);

        Intent intent = getIntent();
        storeItem = intent.getParcelableExtra("storeItem");
        userTotalPoints = intent.getIntExtra("userPointTotal", 0);
        itemPosition = intent.getIntExtra("itemPosition", 0);
        tvStoreItemName.setText(storeItem.getName());
        tvStoreItemCost.setText(String.valueOf(storeItem.getCost()));
        ivStoreItem.setImageResource(storeItem.getRes());

        if (storeItem.isPurchased()) {
            btnPurchase.setEnabled(false);
        } else if (!storeItem.isPurchased() || storeItem.getId() < 2) { // if its not purchased or food, you can't equip it
            btnEquip.setEnabled(false);
        }

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userTotalPoints >= storeItem.getCost()) {
                    User.purchase(storeItem.getId(), storeItem.getCost());
                    Toast.makeText(StoreItemDetailActivity.this, "Successfully Purchased!", Toast.LENGTH_SHORT).show();

                    // https://stackoverflow.com/questions/14292398/how-to-pass-data-from-2nd-activity-to-1st-activity-when-pressed-back-android
                    Intent intent = new Intent();
                    intent.putExtra("position", itemPosition);
                    intent.putExtra("storeItem", storeItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                   Toast.makeText(StoreItemDetailActivity.this, "Oops! You don't have enough points to buy "+ storeItem.getName(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnEquip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.equipBG(storeItem.getId());
                Toast.makeText(StoreItemDetailActivity.this, "Successfully Equipped!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        // Create pop-up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width), (int) (height * .4));
    }


}