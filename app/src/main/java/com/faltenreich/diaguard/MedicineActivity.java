package com.faltenreich.diaguard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MedicineActivity extends AppCompatActivity {

    private ImageView backButton;
    private CardView FindDoctorCard, LabTestCard, MedicineCard, OrderCard, ArticleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        backButton = findViewById(R.id.back_B);
        FindDoctorCard = findViewById(R.id.FindDoctorCard);
        LabTestCard = findViewById(R.id.LabTestCard);
        MedicineCard = findViewById(R.id.MedicineCard);
        OrderCard = findViewById(R.id.OrderCard);
        ArticleCard = findViewById(R.id.ArticleCard);




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineActivity.this.finish();
            }
        });


        FindDoctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbQuery.loadDoctorsData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(MedicineActivity.this, FindDoctorActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {

                    }
                });

                /*Intent intent = new Intent(MedicineActivity.this, FindDoctorActivity.class);
                startActivity(intent);*/

            }
        });

        LabTestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MedicineActivity.this, LabTestActivity.class);
                startActivity(intent);


            }
        });

        ArticleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MedicineActivity.this, ArticleActivity.class);
                startActivity(intent);

            }
        });

        MedicineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MedicineActivity.this, StoreActivity.class);
                startActivity(intent);

            }
        });

        OrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MedicineActivity.this, OrderActivity.class);
                startActivity(intent);

            }
        });

    }
}