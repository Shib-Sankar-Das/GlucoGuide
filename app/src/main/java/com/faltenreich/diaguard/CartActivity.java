// app/src/main/java/com/faltenreich/diaguard/CartActivity.java
package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemRemoveListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Medicine> cartList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private TextView totalCost;
    private EditText addressInput;
    private EditText dateTimeInput;
    private RadioGroup paymentMethodGroup;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalCost = findViewById(R.id.total_cost);
        addressInput = findViewById(R.id.address_input);
        dateTimeInput = findViewById(R.id.date_time_input);
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        orderButton = findViewById(R.id.order_button);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartList);
        cartAdapter.setOnItemRemoveListener(this);
        cartRecyclerView.setAdapter(cartAdapter);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadCartItems();

        orderButton.setOnClickListener(v -> placeOrder());
    }

    private void loadCartItems() {
        firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .collection("CART").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        cartList.clear();
                        cartList.addAll(queryDocumentSnapshots.toObjects(Medicine.class));
                        cartAdapter.notifyDataSetChanged();
                        calculateTotalCost();
                    } else {
                        cartList.clear();
                        cartAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Failed to load cart items.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemRemove(Medicine medicine) {
        removeItemFromCart(medicine);
    }

    private void addItemToCart(Medicine medicine) {
        firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .collection("CART").document(medicine.getName()).set(medicine)
                .addOnSuccessListener(aVoid -> {
                    cartList.add(medicine);
                    cartAdapter.notifyDataSetChanged();
                    calculateTotalCost();
                    Toast.makeText(CartActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                });
    }

    private void removeItemFromCart(Medicine medicine) {
        firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .collection("CART").document(medicine.getName()).delete()
                .addOnSuccessListener(aVoid -> {
                    cartList.remove(medicine);
                    cartAdapter.notifyDataSetChanged();
                    calculateTotalCost();
                    Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
                });
    }

    private void calculateTotalCost() {
        double total = 0;
        for (Medicine medicine : cartList) {
            total += medicine.getPrice();
        }
        totalCost.setText(String.format("%.2f Rs.", total));
    }

    private void placeOrder() {
        String address = addressInput.getText().toString();
        String dateTime = dateTimeInput.getText().toString();
        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        RadioButton selectedPaymentMethod = findViewById(selectedPaymentMethodId);
        String paymentMethod = selectedPaymentMethod.getText().toString();

        String orderId = generateOrderId();
        Order order = new Order(cartList, address, dateTime, paymentMethod, false, orderId);

        firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                .collection("ORDERS").document(orderId).set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Clear the cart list and update the adapter
                    cartList.clear();
                    cartAdapter.notifyDataSetChanged();
                    addressInput.setText("");
                    dateTimeInput.setText("");
                    paymentMethodGroup.clearCheck();

                    // Remove items from the cart in the database
                    firestore.collection("USERS").document(auth.getCurrentUser().getUid())
                            .collection("CART").get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    document.getReference().delete();
                                }
                            });

                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private String generateOrderId() {
        return firestore.collection("ORDERS").document().getId();
    }
}