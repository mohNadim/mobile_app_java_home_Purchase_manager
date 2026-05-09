package com.example.bmp601_hw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bmp601_hw.R;
import com.example.bmp601_hw.model.Category;

import java.util.List;
import java.util.Locale;

/**
 * محوّل البيانات (Adapter) لعرض المصاريف حسب الفئة
 * يتعامل مع عرض الفئات والمصاريف المرتبطة بها
 */
public class CategoryExpenseAdapter extends RecyclerView.Adapter<CategoryExpenseAdapter.ViewHolder> {

    private List<Category> categories;
    private Context context;

    /**
     * منشئ CategoryExpenseAdapter
     */
    public CategoryExpenseAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    /**
     * إنشاء ViewHolder جديد
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_category_expense, parent, false));
    }

    /**
     * ربط البيانات بـ ViewHolder
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    /**
     * الحصول على عدد العناصر
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * تحديث قائمة الفئات
     */
    public void updateCategories(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder الداخلي
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvCategoryExpense;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryExpense = itemView.findViewById(R.id.tvCategoryExpense);
        }

        /**
         * ربط بيانات الفئة بـ TextViews
         */
        public void bind(Category category) {
            tvCategoryName.setText(category.getName());
            
            // الوصف يحتوي على المبلغ (حل مؤقت)
            try {
                double expense = Double.parseDouble(category.getDescription());
                tvCategoryExpense.setText(String.format(Locale.getDefault(), "%.2f", expense));
            } catch (NumberFormatException e) {
                tvCategoryExpense.setText("0.00");
            }
        }
    }
}
