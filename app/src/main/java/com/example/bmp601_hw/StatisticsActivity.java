package com.example.bmp601_hw;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmp601_hw.adapter.CategoryExpenseAdapter;
import com.example.bmp601_hw.db.DatabaseHelper;
import com.example.bmp601_hw.model.Category;
import com.example.bmp601_hw.model.Purchase;
import com.example.bmp601_hw.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * نشاط الإحصائيات
 * يعرض إجمالي المصاريف والمصاريف حسب الفئة
 */
public class StatisticsActivity extends AppCompatActivity {

    private TextView tvTotalExpenses;
    private RecyclerView recyclerViewCategoryExpenses;
    private DatabaseHelper databaseHelper;
    private PreferenceManager preferenceManager;
    private CategoryExpenseAdapter categoryExpenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceManager = new PreferenceManager(this);
        applyTheme(preferenceManager.getTheme());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statistics);

        initializeComponents();
        loadStatistics();
    }

    /**
     * تهيئة المكونات
     */
    private void initializeComponents() {
        databaseHelper = new DatabaseHelper(this);

        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        recyclerViewCategoryExpenses = findViewById(R.id.recyclerViewCategoryExpenses);

        recyclerViewCategoryExpenses.setLayoutManager(new LinearLayoutManager(this));
        categoryExpenseAdapter = new CategoryExpenseAdapter(new ArrayList<>(), this);
        recyclerViewCategoryExpenses.setAdapter(categoryExpenseAdapter);
    }

    /**
     * تحميل الإحصائيات
     */
    private void loadStatistics() {
        double totalExpenses = databaseHelper.getTotalExpenses();
        tvTotalExpenses.setText(String.format(Locale.getDefault(), "%.2f", totalExpenses));

        List<Purchase> purchases = databaseHelper.getAllPurchases();
        Map<String, Double> categoryExpenses = new HashMap<>();

        for (Purchase purchase : purchases) {
            String category = purchase.getCategory() != null ? purchase.getCategory() : "بدون فئة";
            categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0.0) + purchase.getTotalCost());
        }

        List<Category> categoryList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            Category category = new Category(entry.getKey());
            category.setDescription(String.valueOf(entry.getValue()));
            categoryList.add(category);
        }

        categoryExpenseAdapter.updateCategories(categoryList);
    }

    /**
     * تطبيق الثيم
     */
    private void applyTheme(String theme) {
        switch (theme) {
            case PreferenceManager.THEME_DARK:
                setTheme(R.style.Theme_BMP601_HW_Dark);
                break;
            case PreferenceManager.THEME_ACCENT:
                setTheme(R.style.Theme_BMP601_HW_Accent);
                break;
            case PreferenceManager.THEME_LIGHT:
            default:
                setTheme(R.style.Theme_BMP601_HW_Light);
                break;
        }
    }
}
