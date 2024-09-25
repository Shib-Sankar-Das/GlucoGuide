package com.faltenreich.diaguard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FoodScannetActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final String API_URL = "https://vision.foodvisor.io/api/1.0/en/analysis/";
    private static final String API_KEY = "E7EJtGJN.6lL9dIXhBi1HDx60wclsRNWTWqxUN8tp";

    private TextView resultTextView;
    private ImageView capturedImageView;
    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_scannet); // Ensure this is the correct layout file

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        resultTextView = findViewById(R.id.resultTextView);
        capturedImageView = findViewById(R.id.capturedImageView);
        captureButton = findViewById(R.id.captureButton); // Initialize the Button

        if (captureButton != null) {
            captureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCameraPermission();
                }
            });
        } else {
            Log.e("FoodScannetActivity", "Capture button is null");
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                if (imageBitmap != null) {
                    capturedImageView.setImageBitmap(imageBitmap);
                    capturedImageView.setVisibility(View.VISIBLE);
                    sendImageToApi(imageBitmap);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No image data found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendImageToApi(Bitmap imageBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "food.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), byteArray))
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Api-Key " + API_KEY)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(FoodScannetActivity.this, "API request failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        StringBuilder resultBuilder = new StringBuilder();

                        // Log the entire response for debugging
                        Log.d("API_RESPONSE", responseData);

                        if (jsonObject.has("items") && jsonObject.getJSONArray("items").length() > 0) {
                            JSONObject firstItem = jsonObject.getJSONArray("items").getJSONObject(0);
                            if (firstItem.has("food") && firstItem.getJSONArray("food").length() > 0) {
                                JSONObject firstFood = firstItem.getJSONArray("food").getJSONObject(0);
                                if (firstFood.has("food_info")) {
                                    JSONObject foodInfo = firstFood.getJSONObject("food_info");
                                    if (foodInfo.has("nutrition")) {
                                        JSONObject nutrition = foodInfo.getJSONObject("nutrition");

                                        addNutritionInfo(resultBuilder, nutrition, "glycemic_index", "Glycemic Index");
                                        addNutritionInfo(resultBuilder, nutrition, "carbs_100g", "Carbs");
                                        addNutritionInfo(resultBuilder, nutrition, "fibers_100g", "Fibers");
                                        addNutritionInfo(resultBuilder, nutrition, "sugars_100g", "Sugars");
                                        addNutritionInfo(resultBuilder, nutrition, "proteins_100g", "Proteins");
                                        addNutritionInfo(resultBuilder, nutrition, "fat_100g", "Fat");

                                        if (foodInfo.has("g_per_serving")) {
                                            resultBuilder.append(String.format("G per serving: %.1f\n", foodInfo.getDouble("g_per_serving")));
                                        }

                                        if (firstFood.has("quantity")) {
                                            resultBuilder.append(String.format("Quantity: %.1f\n", firstFood.getDouble("quantity")));
                                        }
                                    } else {
                                        resultBuilder.append("Nutrition information not found in the response.");
                                    }
                                } else {
                                    resultBuilder.append("Food info not found in the response.");
                                }
                            } else {
                                resultBuilder.append("Food array is empty or not found in the response.");
                            }
                        } else {
                            resultBuilder.append("Items array is empty or not found in the response.");
                        }

                        final String result = resultBuilder.toString();
                        runOnUiThread(() -> resultTextView.setText(result));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSON_PARSE_ERROR", "Error parsing JSON: " + e.getMessage());
                        runOnUiThread(() -> {
                            Toast.makeText(FoodScannetActivity.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            resultTextView.setText("Error parsing JSON. Please check the logs for details.");
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(FoodScannetActivity.this, "API request failed: " + response.code(), Toast.LENGTH_LONG).show();
                        resultTextView.setText("API request failed with code: " + response.code());
                    });
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void addNutritionInfo(StringBuilder builder, JSONObject nutrition, String key, String label) {
        try {
            if (nutrition.has(key)) {
                builder.append(String.format("%s: %.1f g/100g\n", label, nutrition.getDouble(key)));
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", "Error parsing " + key + ": " + e.getMessage());
        }
    }
}