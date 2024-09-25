// CartAdapter.java
package com.faltenreich.diaguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Medicine> cartList;
    private OnItemRemoveListener onItemRemoveListener;

    public CartAdapter(List<Medicine> cartList) {
        this.cartList = cartList;
    }

    public void setOnItemRemoveListener(OnItemRemoveListener listener) {
        this.onItemRemoveListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Medicine medicine = cartList.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.medicinePrice.setText(String.format("%.2f Rs.", medicine.getPrice()));
        holder.removeButton.setOnClickListener(v -> {
            if (onItemRemoveListener != null) {
                onItemRemoveListener.onItemRemove(medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public interface OnItemRemoveListener {
        void onItemRemove(Medicine medicine);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName;
        TextView medicinePrice;
        Button removeButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicinePrice = itemView.findViewById(R.id.medicine_price);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}