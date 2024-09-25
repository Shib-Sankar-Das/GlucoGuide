package com.faltenreich.diaguard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PredctionResult extends AppCompatActivity {

    private TextView resultP,ageR, bmiR, pregnanciesR, glucoseR, bloodPressureR, skinThicknessR, insulinR, dpfR;
    private ImageView icon;
    private String result, age, bmi, pregnancies, glucose, bloodPressure, skinThickness, insulin, dpf;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_predction_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.predction_result), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rootView = findViewById(R.id.predction_result);
        icon = findViewById(R.id.iconP);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();

        resultP = findViewById(R.id.resultP);
        ageR = findViewById(R.id.ageR);
        bmiR = findViewById(R.id.bmiR);
        pregnanciesR = findViewById(R.id.pregnanciesR);
        glucoseR = findViewById(R.id.GlucoseR);
        bloodPressureR = findViewById(R.id.BloodPressureR);
        skinThicknessR = findViewById(R.id.SkinThicknessR);
        insulinR = findViewById(R.id.InsulinR);
        dpfR = findViewById(R.id.DPFR);


        result = intent.getStringExtra("outcome");
        age = intent.getStringExtra("age");
        bmi = intent.getStringExtra("bmi");
        pregnancies = intent.getStringExtra("pregnancies");
        glucose = intent.getStringExtra("glucose");
        bloodPressure = intent.getStringExtra("bloodPressure");
        skinThickness = intent.getStringExtra("skinThickness");
        insulin = intent.getStringExtra("insulin");
        dpf = intent.getStringExtra("dpf");


        if(result.equals("1"))
        {

            rootView.setBackgroundResource(R.drawable.predction_diabetic);
            icon.setImageResource(R.drawable.glucose);
            result = "You are not safe!";
        }
        else
        {
            rootView.setBackgroundResource(R.drawable.predction_non_diabetic);
            icon.setImageResource(R.drawable.safe);
            result = "You are safe!";

        }

        resultP.setText(result);
        ageR.setText("Age: "+age+" years");
        bmiR.setText("BMI: "+bmi);
        pregnanciesR.setText("Pregnancies: "+pregnancies);
        glucoseR.setText("Glucose: "+glucose+" mg/dL");
        bloodPressureR.setText("Blood Pressure: "+bloodPressure+" mmHg");
        skinThicknessR.setText("Skin Thickness: "+skinThickness+" mm");
        insulinR.setText("Insulin: "+insulin+" mU/L");
        dpfR.setText("DPF: "+dpf);


    }
}