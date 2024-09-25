package com.faltenreich.diaguard;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Medicine> medicineList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderRecyclerView = findViewById(R.id.order_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadOrders();
    }

    private void loadOrders() {
        firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .collection("ORDERS").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        medicineList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Order order = document.toObject(Order.class);
                            if (order.getCartList() != null) {
                                medicineList.addAll(order.getCartList());
                                orderAdapter = new OrderAdapter(medicineList, order.getOrderId(), order.getDateTime()); // Pass orderId and dateTime to OrderAdapter
                                orderRecyclerView.setAdapter(orderAdapter);
                            }
                        }
                    } else {
                        medicineList.clear();
                        if (orderAdapter != null) {
                            orderAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(OrderActivity.this, "No orders found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderActivity.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
                });
    }
}