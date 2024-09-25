package com.faltenreich.diaguard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.UUID;

public class PackageDetailsActivity extends AppCompatActivity {

    private ImageView packageImageView;
    private TextView packageNameTextView, packagePriceTextView, packageDescriptionTextView, packagePrerequisitesTextView, dateTextView, timeTextView;
    private Button setDateButton, setTimeButton, setAppointmentButton;
    private String date, time;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        packageImageView = findViewById(R.id.package_image);
        packageNameTextView = findViewById(R.id.package_name);
        packagePriceTextView = findViewById(R.id.package_price);
        packageDescriptionTextView = findViewById(R.id.package_description);
        packagePrerequisitesTextView = findViewById(R.id.package_prerequisites);
        dateTextView = findViewById(R.id.date_text);
        timeTextView = findViewById(R.id.time_text);
        setDateButton = findViewById(R.id.set_date_button);
        setTimeButton = findViewById(R.id.set_time_button);
        setAppointmentButton = findViewById(R.id.set_appointment_button);

        db = FirebaseFirestore.getInstance();

        PackageModel packageModel = (PackageModel) getIntent().getSerializableExtra("PACKAGE_MODEL");

        if (packageModel != null) {
            packageNameTextView.setText(packageModel.getPackageName());
            packagePriceTextView.setText(String.valueOf(packageModel.getPrice()) + " Rs.");
            packageDescriptionTextView.setText(packageModel.getDescription());
            packagePrerequisitesTextView.setText(packageModel.getPrerequisites());

            if (packageModel.getImage() != null && !packageModel.getImage().isEmpty()) {
                Glide.with(this)
                        .load(packageModel.getImage())
                        .apply(new RequestOptions().placeholder(R.drawable.lab))
                        .into(packageImageView);
            } else {
                packageImageView.setImageResource(R.drawable.lab);
            }
        }

        setDateButton.setOnClickListener(v -> showDatePicker());
        setTimeButton.setOnClickListener(v -> showTimePicker());
        setAppointmentButton.setOnClickListener(v -> setAppointment(packageModel));
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            date = dayOfMonth + "/" + (month + 1) + "/" + year;
            dateTextView.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            time = hourOfDay + ":" + minute;
            timeTextView.setText(time);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void setAppointment(PackageModel packageModel) {
        if (date == null || time == null) {
            Toast.makeText(this, "Please select both date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar selectedDateTime = Calendar.getInstance();
        String[] dateParts = date.split("/");
        String[] timeParts = time.split(":");
        selectedDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
        selectedDateTime.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) - 1);
        selectedDateTime.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));
        selectedDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
        selectedDateTime.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));

        if (selectedDateTime.before(Calendar.getInstance())) {
            Toast.makeText(this, "Please select a future date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String labId = getIntent().getStringExtra("LAB_ID");
        DocumentReference lab_db = FirebaseFirestore.getInstance().collection("LAB_TEST_DATA").document(labId);
        lab_db.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String labName = documentSnapshot.getString("NAME");
                String labAddress = documentSnapshot.getString("ADDRESS");
                String labContact = documentSnapshot.getString("CONTACT");

                String appointmentId = UUID.randomUUID().toString();
                AppointmentModel appointmentModel = new AppointmentModel(
                        labName,
                        labAddress,
                        labContact,
                        packageModel.getPackageName(),
                        packageModel.getImage(),
                        packageModel.getDescription(),
                        packageModel.getPrerequisites(),
                        packageModel.getPrice(),
                        date,
                        time,
                        appointmentId
                );

                db.collection("USERS").document(userId).collection("Appointments")
                        .document(appointmentId)
                        .set(appointmentModel)
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(PackageDetailsActivity.this, AppointmentActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Log.w("PackageDetailsActivity", "Error adding document", e));
            } else {
                Log.w("PackageDetailsActivity", "No such document");
            }
        }).addOnFailureListener(e -> Log.w("PackageDetailsActivity", "Error getting document", e));
    }
}