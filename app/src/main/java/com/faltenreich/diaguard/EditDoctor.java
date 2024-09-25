package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class EditDoctor extends AppCompatActivity {

    private EditText DocName, DocSpeciality, DocAddress, DocHospital, DocEmail, DocContact;
    private ImageView backButton;
    private Button DocSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        DocName = findViewById(R.id.doctorName);
        DocSpeciality = findViewById(R.id.docSpeciality);
        DocAddress = findViewById(R.id.docAdd);
        DocHospital = findViewById(R.id.docHospital);
        DocEmail = findViewById(R.id.docEmail);
        DocContact = findViewById(R.id.docPhone);

        DocSubmit = findViewById(R.id.addButton);

        backButton = findViewById(R.id.backButton);

        DocName.setSingleLine(true);
        DocSpeciality.setSingleLine(true);
        DocAddress.setSingleLine(true);
        DocHospital.setSingleLine(true);

        Intent intent = getIntent();
        String name = intent.getStringExtra("DOCTOR_NAME");
        String speciality = intent.getStringExtra("DOCTOR_SPECIALITY");
        String address = intent.getStringExtra("DOCTOR_ADDRESS");
        String hospital = intent.getStringExtra("DOCTOR_HOSPITAL");
        String email = intent.getStringExtra("DOCTOR_EMAIL");
        String contact = intent.getStringExtra("DOCTOR_CONTACT");
        boolean status = intent.getBooleanExtra("DOCTOR_STATUS", false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DocName.setText(name);
        DocSpeciality.setText(speciality);
        DocAddress.setText(address);
        DocHospital.setText(hospital);
        DocEmail.setText(email);
        DocContact.setText(contact);

        DocSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = DocName.getText().toString();
                String newSpeciality = DocSpeciality.getText().toString();
                String newAddress = DocAddress.getText().toString();
                String newHospital = DocHospital.getText().toString();
                String newEmail = DocEmail.getText().toString();
                String newContact = DocContact.getText().toString();

                DoctorModle updatedDoctor = new DoctorModle(newName, newSpeciality, newAddress, newHospital, newEmail, newContact, status);

                DbQuery.updateDoctorData(email, updatedDoctor, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Doctor data updated successfully", Toast.LENGTH_SHORT).show();

                        Intent updateIntent = new Intent("DOCTOR_DATA_UPDATED");
                        updateIntent.putExtra("DOCTOR_NAME", updatedDoctor.getDoctorName());
                        updateIntent.putExtra("DOCTOR_SPECIALITY", updatedDoctor.getDocSpeciality());
                        updateIntent.putExtra("DOCTOR_ADDRESS", updatedDoctor.getDocAddress());
                        updateIntent.putExtra("DOCTOR_HOSPITAL", updatedDoctor.getDocHospital());
                        updateIntent.putExtra("DOCTOR_EMAIL", updatedDoctor.getDocEmail());
                        updateIntent.putExtra("DOCTOR_CONTACT", updatedDoctor.getDocContact());
                        updateIntent.putExtra("DOCTOR_STATUS", updatedDoctor.isDocStatus());
                        LocalBroadcastManager.getInstance(EditDoctor.this).sendBroadcast(updateIntent);

                        finish(); // Close the EditDoctor activity
                        // Perform any UI updates or navigation here
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getApplicationContext(), "Failed to update doctor data", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}