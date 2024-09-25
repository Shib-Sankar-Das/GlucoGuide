// app/src/main/java/com/faltenreich/diaguard/OrderDetailActivity.java
package com.faltenreich.diaguard;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView medicineImage;
    private TextView medicineName, medicineDescription, medicinePrice, address, dateTime, paymentMethod, orderId;
    private Button cancelButton;
    private FirebaseFirestore firestore;
    private String orderIdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        medicineImage = findViewById(R.id.medicine_image);
        medicineName = findViewById(R.id.medicine_name);
        medicineDescription = findViewById(R.id.medicine_description);
        medicinePrice = findViewById(R.id.medicine_price);
        address = findViewById(R.id.address);
        dateTime = findViewById(R.id.date_time);
        paymentMethod = findViewById(R.id.payment_method);
        orderId = findViewById(R.id.order_id);
        cancelButton = findViewById(R.id.cancel_button);

        firestore = FirebaseFirestore.getInstance();

        // Get orderId from intent
        orderIdValue = getIntent().getStringExtra("orderId");

        if (orderIdValue != null) {
            loadOrderDetails(orderIdValue);
        } else {
            Toast.makeText(this, "Order ID is missing.", Toast.LENGTH_SHORT).show();
            finish();
        }

        cancelButton.setOnClickListener(v -> cancelOrder());
    }

    private void loadOrderDetails(String orderId) {
        firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ORDERS").document(orderId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Order order = documentSnapshot.toObject(Order.class);
                        if (order != null) {
                            Medicine medicine = order.getCartList().get(0); // Assuming one medicine per order for simplicity
                            Glide.with(this).load(medicine.getImagePath()).into(medicineImage);
                            medicineName.setText(medicine.getName());
                            medicineDescription.setText(medicine.getDescription());
                            medicinePrice.setText(String.valueOf(medicine.getPrice()));
                            address.setText(order.getAddress());
                            dateTime.setText(order.getDateTime());
                            paymentMethod.setText(order.getPaymentMethod());
                            this.orderId.setText(order.getOrderId());
                        }
                    } else {
                        Toast.makeText(this, "Order not found.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load order details.", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void cancelOrder() {
        firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ORDERS").document(orderIdValue).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(OrderDetailActivity.this, "Order cancelled successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderDetailActivity.this, "Failed to cancel order.", Toast.LENGTH_SHORT).show();
                });
    }
}