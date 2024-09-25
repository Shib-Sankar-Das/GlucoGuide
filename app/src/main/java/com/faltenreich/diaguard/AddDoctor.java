package com.faltenreich.diaguard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.faltenreich.diaguard.feature.navigation.MainActivity;

public class AddDoctor extends AppCompatActivity {

    private EditText DocName, DocSpeciality, DocAddress, DocHospital, DocEmail, DocContact;
    private CheckBox DocStatus;
    private Button DocSubmit;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddDoctor.this, FindDoctorActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        DocName = findViewById(R.id.doctorName);
        DocSpeciality = findViewById(R.id.docSpeciality);
        DocAddress = findViewById(R.id.docAdd);
        DocHospital = findViewById(R.id.docHospital);
        DocEmail = findViewById(R.id.docEmail);
        DocContact = findViewById(R.id.docPhone);

        DocStatus = findViewById(R.id.checkBox);
        DocSubmit = findViewById(R.id.addButton);

        DocName.setSingleLine(true);
        DocSpeciality.setSingleLine(true);
        DocAddress.setSingleLine(true);
        DocHospital.setSingleLine(true);


        DocSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = DocName.getText().toString();
                String speciality = DocSpeciality.getText().toString();
                String address = DocAddress.getText().toString();
                String hospital = DocHospital.getText().toString();
                String email = DocEmail.getText().toString();
                String contact = DocContact.getText().toString();
                boolean status = DocStatus.isChecked();

                DbQuery.createDoctor(name, speciality, address, hospital, email, contact, status, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {

                        Toast.makeText(AddDoctor.this, "Doctor Added", Toast.LENGTH_SHORT).show();
                        DbQuery.loadDoctorsData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(AddDoctor.this, FindDoctorActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(AddDoctor.this, "Failed to add Doctor", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(AddDoctor.this, "Failed to add Doctor", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });





    }
}