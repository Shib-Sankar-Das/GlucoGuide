package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageView cartImage;
    private RecyclerView medicineRecyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList = new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        searchBar = findViewById(R.id.search_bar);
        cartImage = findViewById(R.id.cart_image);
        medicineRecyclerView = findViewById(R.id.medicine_recycler_view);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineAdapter = new MedicineAdapter(medicineList, this::onMedicineClick);
        medicineRecyclerView.setAdapter(medicineAdapter);

        firestore = FirebaseFirestore.getInstance();
        loadMedicines();

        cartImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMedicines(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadMedicines() {
        firestore.collection("MEDICINES").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                medicineList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Medicine medicine = document.toObject(Medicine.class);
                    medicineList.add(medicine);
                }
                medicineAdapter.notifyDataSetChanged();
            }
        });
    }

    private void filterMedicines(String query) {
        List<Medicine> filteredList = new ArrayList<>();
        for (Medicine medicine : medicineList) {
            if (medicine.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(medicine);
            }
        }
        medicineAdapter.updateList(filteredList);
    }

    private void onMedicineClick(Medicine medicine) {
        Intent intent = new Intent(this, MedicineDetailsActivity.class);
        intent.putExtra("medicine", (Parcelable) medicine);
        startActivity(intent);
    }
}