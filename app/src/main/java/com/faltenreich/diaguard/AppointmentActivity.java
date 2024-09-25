package com.faltenreich.diaguard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    private RecyclerView appointmentRecyclerView;
    private List<AppointmentModel> appointmentList;
    private AppointmentAdapter appointmentAdapter;
    private FirebaseFirestore db;
    private TextView totalCostTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        totalCostTextView = findViewById(R.id.total_cost);
        appointmentRecyclerView = findViewById(R.id.appointment_recycler_view);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        appointmentRecyclerView.setAdapter(appointmentAdapter);

        db = FirebaseFirestore.getInstance();

        fetchAppointments();

        appointmentAdapter.setOnItemClickListener(new AppointmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppointmentModel appointmentModel) {
                Intent intent = new Intent(AppointmentActivity.this, AppointmentDetailsActivity.class);
                intent.putExtra("APPOINTMENT_MODEL", appointmentModel);
                startActivity(intent);
            }

            @Override
            public void onCancelClick(AppointmentModel appointmentModel) {
                showCancelDialog(appointmentModel);
            }
        });
    }

    private void fetchAppointments() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("USERS").document(userId).collection("Appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalCost = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AppointmentModel appointmentModel = document.toObject(AppointmentModel.class);
                            appointmentList.add(appointmentModel);
                            totalCost += appointmentModel.getPrice();
                        }
                        appointmentAdapter.notifyDataSetChanged();
                        totalCostTextView.setText("Total Cost: " + totalCost + " Rs.");
                    } else {
                        Log.w("AppointmentActivity", "Error getting documents.", task.getException());
                    }
                });
    }

    private void showCancelDialog(AppointmentModel appointmentModel) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Appointment")
                .setMessage("Do you want to cancel the appointment?")
                .setPositiveButton("Yes", (dialog, which) -> removeAppointment(appointmentModel))
                .setNegativeButton("No", null)
                .show();
    }

    private void removeAppointment(AppointmentModel appointmentModel) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("USERS").document(userId).collection("Appointments")
                .document(appointmentModel.getAppointmentId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    appointmentList.remove(appointmentModel);
                    appointmentAdapter.notifyDataSetChanged();
                    updateTotalCost();
                })
                .addOnFailureListener(e -> Log.w("AppointmentActivity", "Error deleting document", e));
    }

    private void updateTotalCost() {
        int totalCost = 0;
        for (AppointmentModel appointment : appointmentList) {
            totalCost += appointment.getPrice();
        }
        totalCostTextView.setText("Total Cost: $" + totalCost);
    }
}