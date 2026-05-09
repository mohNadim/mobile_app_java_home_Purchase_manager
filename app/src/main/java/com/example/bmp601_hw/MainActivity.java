package com.example.bmp601_hw;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmp601_hw.adapter.PurchaseAdapter;
import com.example.bmp601_hw.db.DatabaseHelper;
import com.example.bmp601_hw.model.Purchase;
import com.example.bmp601_hw.utils.AudioHelper;
import com.example.bmp601_hw.utils.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPurchases;
    private FloatingActionButton fabAddPurchase;
    private DatabaseHelper databaseHelper;
    private PurchaseAdapter purchaseAdapter;
    private List<Purchase> purchaseList;
    private PreferenceManager preferenceManager;
    private AudioHelper audioHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceManager = new PreferenceManager(this);
        applyTheme(preferenceManager.getTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        loadPurchases();
    }

    private void initializeComponents() {
        databaseHelper = new DatabaseHelper(this);
        audioHelper = new AudioHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewPurchases = findViewById(R.id.recyclerViewPurchases);
        fabAddPurchase = findViewById(R.id.fabAddPurchase);

        recyclerViewPurchases.setLayoutManager(new LinearLayoutManager(this));
        purchaseList = new ArrayList<>();
        purchaseAdapter = new PurchaseAdapter(purchaseList, this);
        recyclerViewPurchases.setAdapter(purchaseAdapter);

        purchaseAdapter.setOnItemClickListener(new PurchaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Purchase purchase) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("purchase_id", purchase.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Purchase purchase) {
            }

            @Override
            public void onDeleteClick(Purchase purchase) {
                audioHelper.playClickSound();
                databaseHelper.deletePurchase(purchase.getId());
                loadPurchases();
            }
        });

        fabAddPurchase.setOnClickListener(v -> {
            audioHelper.playClickSound();
            startActivity(new Intent(MainActivity.this, AddEditActivity.class));
        });
    }

    private void loadPurchases() {
        purchaseList.clear();
        purchaseList.addAll(databaseHelper.getAllPurchases());
        purchaseAdapter.notifyDataSetChanged();
    }

    private void applyTheme(String theme) {
        switch (theme) {
            case PreferenceManager.THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.Theme_BMP601_HW_Dark);
                break;
            case PreferenceManager.THEME_ACCENT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.Theme_BMP601_HW_Accent);
                break;
            case PreferenceManager.THEME_LIGHT:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                setTheme(R.style.Theme_BMP601_HW_Light);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_statistics) {
            startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            return true;
        } else if (itemId == R.id.menu_theme) {
            showThemeDialog();
            return true;
        } else if (itemId == R.id.menu_filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showThemeDialog() {
        String[] themes = {getString(R.string.theme_light), getString(R.string.theme_dark), getString(R.string.theme_accent)};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("اختر الثيم")
                .setItems(themes, (dialog, which) -> {
                    String selectedTheme;
                    switch (which) {
                        case 0: selectedTheme = PreferenceManager.THEME_LIGHT; break;
                        case 1: selectedTheme = PreferenceManager.THEME_DARK; break;
                        case 2: selectedTheme = PreferenceManager.THEME_ACCENT; break;
                        default: selectedTheme = PreferenceManager.THEME_LIGHT;
                    }
                    preferenceManager.setTheme(selectedTheme);
                    recreate();
                })
                .show();
    }

    private void showFilterDialog() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPurchases();
        if (audioHelper != null) audioHelper.resumeAllSounds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioHelper != null) audioHelper.stopAllSounds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioHelper != null) audioHelper.release();
    }
}
