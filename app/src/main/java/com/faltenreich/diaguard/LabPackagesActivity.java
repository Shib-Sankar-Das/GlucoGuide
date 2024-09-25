package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LabPackagesActivity extends AppCompatActivity {

    private RecyclerView packagesRecyclerView;
    private List<PackageModel> packageList;
    private PackageAdapter packageAdapter;
    private FirebaseFirestore db;
    private TextView labNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_packages);

        labNameTextView = findViewById(R.id.lab_name);
        packagesRecyclerView = findViewById(R.id.packages_recycler_view);
        packagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        packageList = new ArrayList<>();
        packageAdapter = new PackageAdapter(packageList);
        packagesRecyclerView.setAdapter(packageAdapter);

        db = FirebaseFirestore.getInstance();

        String labId = getIntent().getStringExtra("LAB_ID");
        if (labId != null) {
            fetchLabName(labId);
            fetchPackages(labId);
        }

        packageAdapter.setOnItemClickListener(new PackageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PackageModel packageModel) {
                Intent intent = new Intent(LabPackagesActivity.this, PackageDetailsActivity.class);
                intent.putExtra("PACKAGE_MODEL", packageModel);
                intent.putExtra("LAB_ID", labId);
                startActivity(intent);
            }
        });
    }

    private void fetchLabName(String labId) {
        db.collection("LAB_TEST_DATA").document(labId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String labName = document.getString("NAME");
                            labNameTextView.setText(labName);
                        } else {
                            Log.w("LabPackagesActivity", "No such document");
                        }
                    } else {
                        Log.w("LabPackagesActivity", "Error getting document.", task.getException());
                    }
                });
    }

    private void fetchPackages(String labId) {
        db.collection("LAB_TEST_DATA").document(labId).collection("LAB_INFO")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PackageModel packageModel = document.toObject(PackageModel.class);
                            packageList.add(packageModel);
                        }
                        packageAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("LabPackagesActivity", "Error getting documents.", task.getException());
                    }
                });
    }
}