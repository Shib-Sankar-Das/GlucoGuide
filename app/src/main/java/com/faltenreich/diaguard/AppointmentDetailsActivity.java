package com.faltenreich.diaguard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentDetailsActivity extends AppCompatActivity {

    private ImageView packageImageView;
    private TextView packageNameTextView, labNameTextView, packageDescriptionTextView, packagePrerequisitesTextView, packagePriceTextView, labAddressTextView, labPhoneTextView, dateTextView, timeTextView, appointmentIdTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        packageImageView = findViewById(R.id.package_image);
        packageNameTextView = findViewById(R.id.package_name);
        labNameTextView = findViewById(R.id.lab_name);
        packageDescriptionTextView = findViewById(R.id.package_description);
        packagePrerequisitesTextView = findViewById(R.id.package_prerequisites);
        packagePriceTextView = findViewById(R.id.package_price);
        labAddressTextView = findViewById(R.id.lab_address);
        labPhoneTextView = findViewById(R.id.lab_phone);
        dateTextView = findViewById(R.id.date_text);
        timeTextView = findViewById(R.id.time_text);
        appointmentIdTextView = findViewById(R.id.appointment_id);

        db = FirebaseFirestore.getInstance();

        AppointmentModel appointmentModel = (AppointmentModel) getIntent().getSerializableExtra("APPOINTMENT_MODEL");
        if (appointmentModel != null) {
            packageNameTextView.setText(appointmentModel.getTestName());
            labNameTextView.setText(appointmentModel.getLabName());
            packageDescriptionTextView.setText(appointmentModel.getDescription());
            packagePrerequisitesTextView.setText(appointmentModel.getPrerequisites());
            packagePriceTextView.setText(String.valueOf(appointmentModel.getPrice()) + " Rs.");
            labAddressTextView.setText(appointmentModel.getLabAddress());
            labPhoneTextView.setText(appointmentModel.getLabPhoneNumber());
            dateTextView.setText(appointmentModel.getDate());
            timeTextView.setText(appointmentModel.getTime());
            appointmentIdTextView.setText(appointmentModel.getAppointmentId());

            if (appointmentModel.getTestImage() != null && !appointmentModel.getTestImage().isEmpty()) {
                Glide.with(this)
                        .load(appointmentModel.getTestImage())
                        .apply(new RequestOptions().placeholder(R.drawable.lab))
                        .into(packageImageView);
            } else {
                packageImageView.setImageResource(R.drawable.lab);
            }
        }

        findViewById(R.id.cancel_button).setOnClickListener(v -> showCancelDialog(appointmentModel));
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
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> Log.w("AppointmentDetailsActivity", "Error deleting document", e));
    }
}