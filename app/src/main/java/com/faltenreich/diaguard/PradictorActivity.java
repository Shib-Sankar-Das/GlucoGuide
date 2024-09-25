package com.faltenreich.diaguard;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PradictorActivity extends AppCompatActivity {

    private EditText pregnancies, skinThickness, dpf, glucose, insulin, age, bloodPressure, bmi;
    private View predictB;
    private String pregnanciesStr, skinThicknessStr, dpfStr, glucoseStr, insulinStr, ageStr, bloodPressureStr, bmiStr;
    private int pregnanciesVal, skinThicknessVal, glucoseVal, insulinVal, ageVal, bloodPressureVal;
    private float bmiVal, dpfVal;
    private String url = "https://diabetesprediction-84qe.onrender.com/predict";
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pradictor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pregnancies = findViewById(R.id.pregnancies);
        skinThickness = findViewById(R.id.SkinThickness);
        dpf = findViewById(R.id.DPF);
        glucose = findViewById(R.id.Glucose);
        insulin = findViewById(R.id.Insulin);
        age = findViewById(R.id.age);
        bloodPressure = findViewById(R.id.BloodPressure);
        bmi = findViewById(R.id.bmi);

        predictB = findViewById(R.id.predictB);

        progressDialog = new Dialog(PradictorActivity.this);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Predicting...");

        predictB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()) {
                    predict();
                }
            }
        });
    }

    private boolean validateData() {
        pregnanciesStr = pregnancies.getText().toString();
        skinThicknessStr = skinThickness.getText().toString();
        dpfStr = dpf.getText().toString();
        glucoseStr = glucose.getText().toString();
        insulinStr = insulin.getText().toString();
        ageStr = age.getText().toString();
        bloodPressureStr = bloodPressure.getText().toString();
        bmiStr = bmi.getText().toString();

        if (pregnanciesStr.isEmpty()) {
            pregnancies.setError("Enter a valid number");
            return false;
        }
        if (skinThicknessStr.isEmpty()) {
            skinThickness.setError("Enter a valid number");
            return false;
        }
        if (dpfStr.isEmpty()) {
            dpf.setError("Enter a valid number");
            return false;
        }
        if (glucoseStr.isEmpty()) {
            glucose.setError("Enter a valid number");
            return false;
        }
        if (insulinStr.isEmpty()) {
            insulin.setError("Enter a valid number");
            return false;
        }
        if (ageStr.isEmpty()) {
            age.setError("Enter a valid number");
            return false;
        }
        if (bloodPressureStr.isEmpty()) {
            bloodPressure.setError("Enter a valid number");
            return false;
        }
        if (bmiStr.isEmpty()) {
            bmi.setError("Enter a valid number");
            return false;
        }

        pregnanciesVal = Integer.parseInt(pregnanciesStr);
        skinThicknessVal = Integer.parseInt(skinThicknessStr);
        glucoseVal = Integer.parseInt(glucoseStr);
        insulinVal = Integer.parseInt(insulinStr);
        ageVal = Integer.parseInt(ageStr);
        bloodPressureVal = Integer.parseInt(bloodPressureStr);
        bmiVal = Float.parseFloat(bmiStr);
        dpfVal = Float.parseFloat(dpfStr);

        if (skinThicknessVal == 0) {
            skinThickness.setError("Enter a valid number");
            return false;
        }
        if (glucoseVal == 0) {
            glucose.setError("Enter a valid number");
            return false;
        }
        if (insulinVal == 0) {
            insulin.setError("Enter a valid number");
            return false;
        }
        if (ageVal == 0) {
            age.setError("Enter a valid number");
            return false;
        }
        if (bloodPressureVal == 0) {
            bloodPressure.setError("Enter a valid number");
            return false;
        }
        if (bmiVal == 0) {
            bmi.setError("Enter a valid number");
            return false;
        }
        if (dpfVal == 0) {
            dpf.setError("Enter a valid number");
            return false;
        }

        return true;
    }

    private void predict() {
        progressDialog.show();

        Map<String, Object> params = new HashMap<>();
        params.put("Pregnancies", pregnanciesStr);
        params.put("Glucose", glucoseStr);
        params.put("BloodPressure", bloodPressureStr);
        params.put("SkinThickness", skinThicknessStr);
        params.put("Insulin", insulinStr);
        params.put("BMI", bmiStr);
        params.put("DiabetesPedigreeFunction", dpfStr);
        params.put("Age", ageStr);

        JSONObject jsonParams = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String outcome = response.getString("Diabetic");

                            Toast.makeText(PradictorActivity.this, "Output" + outcome, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(PradictorActivity.this, PredctionResult.class);
                            intent.putExtra("outcome", outcome);
                            intent.putExtra("age", ageStr);
                            intent.putExtra("bmi", bmiStr);
                            intent.putExtra("bloodPressure", bloodPressureStr);
                            intent.putExtra("diabetesPedigreeFunction", dpfStr);
                            intent.putExtra("glucose", glucoseStr);
                            intent.putExtra("insulin", insulinStr);
                            intent.putExtra("pregnancies", pregnanciesStr);
                            intent.putExtra("skinThickness", skinThicknessStr);

                            startActivity(intent);
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(PradictorActivity.this, "JSON parsing error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        NetworkResponse networkResponse = volleyError.networkResponse;
                        if (networkResponse != null) {
                            int statusCode = networkResponse.statusCode;
                            String errorMsg = new String(networkResponse.data);
                            Toast.makeText(PradictorActivity.this, "Error: " + statusCode + " -> " + errorMsg, Toast.LENGTH_LONG).show();
                            Log.e("VolleyError", "Error: " + statusCode + " -> " + errorMsg);
                        } else {
                            Toast.makeText(PradictorActivity.this, "Network error: " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("VolleyError", "Network error", volleyError);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PradictorActivity.this);
        requestQueue.add(jsonObjectRequest);
    }
}
