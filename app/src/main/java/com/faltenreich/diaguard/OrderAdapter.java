package com.faltenreich.diaguard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.Medicine;
import com.faltenreich.diaguard.Order;
import com.faltenreich.diaguard.OrderDetailActivity;
import com.faltenreich.diaguard.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Medicine> medicineList;
    private String orderId; // Add orderId field
    private String dateTime; // Add dateTime field

    public OrderAdapter(List<Medicine> medicineList, String orderId, String dateTime) {
        this.medicineList = medicineList;
        this.orderId = orderId; // Initialize orderId
        this.dateTime = dateTime; // Initialize dateTime
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.medicineName.setText(medicine.getName());
        holder.medicinePrice.setText(String.valueOf(medicine.getPrice()));
        holder.deliveryDate.setText(dateTime); // Set the dateTime

        holder.cancelButton.setOnClickListener(v -> {
            // Remove item from database and update UI
            removeMedicineFromDatabase(medicine.getName(), position);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OrderDetailActivity.class);
            intent.putExtra("medicineName", medicine.getName());
            intent.putExtra("orderId", orderId); // Pass orderId to OrderDetailActivity
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private void removeMedicineFromDatabase(String medicineName, int position) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Remove the medicine from the list
        medicineList.remove(position);
        notifyItemRemoved(position);

        // Update the order in the database
        firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .collection("ORDERS").document(orderId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Order order = documentSnapshot.toObject(Order.class);
                        if (order != null) {
                            List<Medicine> cartList = order.getCartList();
                            cartList.removeIf(medicine -> medicine.getName().equals(medicineName));

                            if (cartList.isEmpty()) {
                                // If the cart list is empty, delete the entire order document
                                firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                                        .collection("ORDERS").document(orderId).delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Order document deleted successfully
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle the error
                                        });
                            } else {
                                // Update the order document with the new cart list
                                order.setCartList(cartList);
                                firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                                        .collection("ORDERS").document(orderId).set(order)
                                        .addOnSuccessListener(aVoid -> {
                                            // Order document updated successfully
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle the error
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName;
        TextView medicinePrice;
        TextView deliveryDate;
        Button cancelButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicinePrice = itemView.findViewById(R.id.medicine_price);
            deliveryDate = itemView.findViewById(R.id.delivery_date);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}