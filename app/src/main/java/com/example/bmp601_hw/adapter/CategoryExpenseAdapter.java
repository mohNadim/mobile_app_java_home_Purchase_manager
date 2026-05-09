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

public class CategoryExpenseAdapter extends RecyclerView.Adapter<CategoryExpenseAdapter.ViewHolder> {

    private List<Category> categories;
    private Context context;

    public CategoryExpenseAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_category_expense, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvCategoryExpense;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryExpense = itemView.findViewById(R.id.tvCategoryExpense);
        }

        public void bind(Category category) {
            tvCategoryName.setText(category.getName());
            try {
                double expense = Double.parseDouble(category.getDescription());
                tvCategoryExpense.setText(String.format(Locale.getDefault(), "%.2f", expense));
            } catch (NumberFormatException e) {
                tvCategoryExpense.setText("0.00");
            }
        }
    }
}
