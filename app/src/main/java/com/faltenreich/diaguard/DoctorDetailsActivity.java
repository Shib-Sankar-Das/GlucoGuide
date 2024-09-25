package com.faltenreich.diaguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pdfjet.Text;

public class DoctorDetailsActivity extends AppCompatActivity {

    private TextView doctorName, doctorSpeciality, doctorAddress, doctorHospital, doctorEmail, doctorContact, textEdit;
    private CheckBox doctorStatus;
    private LinearLayout myLinearLayout;
    private boolean status;
    private String email;
    private FloatingActionButton saveButton;
    private ImageView backButton;
    private BroadcastReceiver updateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        doctorName = findViewById(R.id.Dname);
        doctorSpeciality = findViewById(R.id.Dspeciality);
        doctorAddress = findViewById(R.id.Daddress);
        doctorHospital = findViewById(R.id.Dhospital);
        doctorEmail = findViewById(R.id.Demail);
        doctorContact = findViewById(R.id.Dcontact);
        doctorStatus = findViewById(R.id.Dstatus);
        myLinearLayout = findViewById(R.id.ReportLayout);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        textEdit = findViewById(R.id.TextEdit);

        Intent intent = getIntent();
        String name = intent.getStringExtra("DOCTOR_NAME");
        String speciality = intent.getStringExtra("DOCTOR_SPECIALITY");
        String address = intent.getStringExtra("DOCTOR_ADDRESS");
        String hospital = intent.getStringExtra("DOCTOR_HOSPITAL");
        email = intent.getStringExtra("DOCTOR_EMAIL");
        String contact = intent.getStringExtra("DOCTOR_CONTACT");
        status = intent.getBooleanExtra("DOCTOR_STATUS", false);

        doctorName.setText(name);
        doctorSpeciality.setText(speciality);
        doctorAddress.setText(address);
        doctorHospital.setText(hospital);
        doctorEmail.setText(email);
        doctorContact.setText(contact);
        doctorStatus.setChecked(status);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorDetailsActivity.this.finish();
            }
        });

        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDetailsActivity.this, EditDoctor.class);
                intent.putExtra("DOCTOR_NAME", name);
                intent.putExtra("DOCTOR_SPECIALITY", speciality);
                intent.putExtra("DOCTOR_ADDRESS", address);
                intent.putExtra("DOCTOR_HOSPITAL", hospital);
                intent.putExtra("DOCTOR_EMAIL", email);
                intent.putExtra("DOCTOR_CONTACT", contact);
                intent.putExtra("DOCTOR_STATUS", status);
                startActivity(intent);
            }
        });

        myLinearLayout.setVisibility(doctorStatus.isChecked() ? View.VISIBLE : View.GONE);

        doctorStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    myLinearLayout.setVisibility(View.GONE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newStatus = doctorStatus.isChecked();
                if (newStatus != status) {
                    updateDoctorStatus(newStatus);
                }
            }
        });

        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("DOCTOR_DATA_UPDATED".equals(intent.getAction())) {
                    String name = intent.getStringExtra("DOCTOR_NAME");
                    String speciality = intent.getStringExtra("DOCTOR_SPECIALITY");
                    String address = intent.getStringExtra("DOCTOR_ADDRESS");
                    String hospital = intent.getStringExtra("DOCTOR_HOSPITAL");
                    String email = intent.getStringExtra("DOCTOR_EMAIL");
                    String contact = intent.getStringExtra("DOCTOR_CONTACT");
                    boolean status = intent.getBooleanExtra("DOCTOR_STATUS", false);

                    updateUI(name, speciality, address, hospital, email, contact, status);
                }
            }
        };


        }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, new IntentFilter("DOCTOR_DATA_UPDATED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
    }

    private void updateUI(String name, String speciality, String address, String hospital, String email, String contact, boolean status) {
        doctorName.setText(name);
        doctorSpeciality.setText(speciality);
        doctorAddress.setText(address);
        doctorHospital.setText(hospital);
        doctorEmail.setText(email);
        doctorContact.setText(contact);
        doctorStatus.setChecked(status);
    }


        private void updateDoctorStatus(boolean newStatus) {
            DbQuery.updateDoctorStatus(email, newStatus, new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(DoctorDetailsActivity.this, "Doctor status updated successfully", Toast.LENGTH_SHORT).show();
                    status = newStatus;
                    finish();
                    // Optionally, you can finish the activity or update the UI as needed
                }

                @Override
                public void onFailure() {
                    Toast.makeText(DoctorDetailsActivity.this, "Failed to update doctor status", Toast.LENGTH_SHORT).show();
                    // Reset the switch to its previous state
                    doctorStatus.setChecked(status);
                }
            });
        }


    }
