// MedicineDetailsActivity.java
package com.faltenreich.diaguard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MedicineDetailsActivity extends AppCompatActivity {

    private ImageView medicineImage;
    private TextView medicineName;
    private TextView medicineDescription;
    private TextView medicinePrice;
    private TextView medicineAvailability;
    private Button addToCartButton;
    private Button buyNowButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        medicineImage = findViewById(R.id.medicine_image);
        medicineName = findViewById(R.id.medicine_name);
        medicineDescription = findViewById(R.id.medicine_description);
        medicinePrice = findViewById(R.id.medicine_price);
        medicineAvailability = findViewById(R.id.medicine_availability);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_button);

        firestore = FirebaseFirestore.getInstance();

        // Assuming medicine is passed as a Parcelable extra
        Medicine medicine = getIntent().getParcelableExtra("medicine");

        if (medicine != null) {
            Log.d("MedicineDetailsActivity", "Image Reference: " + medicine.getImagePath());

            // Load image using Glide
            Glide.with(this).load(medicine.getImagePath()).into(medicineImage);

            medicineName.setText(medicine.getName());
            medicineDescription.setText(medicine.getDescription());
            medicinePrice.setText("Price: " + medicine.getPrice());
            medicineAvailability.setText("Available: " + medicine.getAvailableUnits());

            addToCartButton.setOnClickListener(v -> addToCart(medicine));
            buyNowButton.setOnClickListener(v -> buyNow(medicine));
        }
    }

    private void addToCart(Medicine medicine) {
        Cart cart = Cart.getInstance(); // Assuming Cart is a singleton class managing the cart items
        cart.addMedicine(medicine);

        // Add item to Firestore
        firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("CART").document(medicine.getName()).set(medicine)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, medicine.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                });
    }

    private void buyNow(Medicine medicine) {
        // Add item to Firestore
        firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("CART").document(medicine.getName()).set(medicine)
                .addOnSuccessListener(aVoid -> {
                    // Navigate to CartActivity
                    Intent intent = new Intent(this, CartActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                });
    }
}