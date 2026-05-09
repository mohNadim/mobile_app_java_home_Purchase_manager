package com.example.bmp601_hw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bmp601_hw.R;
import com.example.bmp601_hw.model.Purchase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private List<Purchase> purchases;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Purchase purchase);
        void onItemLongClick(Purchase purchase);
        void onDeleteClick(Purchase purchase);
    }

    public PurchaseAdapter(List<Purchase> purchases, Context context) {
        this.purchases = purchases;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_purchase, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Purchase purchase = purchases.get(position);
        holder.bind(purchase);
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    public void updatePurchases(List<Purchase> newPurchases) {
        this.purchases = newPurchases;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvCategory, tvDate, tvPrice, tvQuantity, tvTotalCost;
        ImageButton btnDelete;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalCost = itemView.findViewById(R.id.tvTotalCost);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(purchases.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemLongClick(purchases.get(position));
                    }
                }
                return true;
            });

            btnDelete.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onDeleteClick(purchases.get(position));
                    }
                }
            });
        }

        public void bind(Purchase purchase) {
            tvItemName.setText(purchase.getItemName());
            tvCategory.setText(purchase.getCategory() != null && !purchase.getCategory().isEmpty() ? purchase.getCategory() : "بدون فئة");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            tvDate.setText(sdf.format(new Date(purchase.getDate())));
            
            tvPrice.setText(String.format(Locale.getDefault(), "السعر: %.2f", purchase.getPrice()));
            tvQuantity.setText(String.format(Locale.getDefault(), "الكمية: %d", purchase.getQuantity()));
            tvTotalCost.setText(String.format(Locale.getDefault(), "الإجمالي: %.2f", purchase.getTotalCost()));
        }
    }
}
