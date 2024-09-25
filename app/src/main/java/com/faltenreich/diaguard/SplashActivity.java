package com.faltenreich.diaguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private TextView App_name;
    private FirebaseAuth mAuth1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        App_name = findViewById(R.id.App_name);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.mouldy);
        App_name.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        App_name.setAnimation(anim);

        mAuth1 = FirebaseAuth.getInstance();

        DbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread(){

            @Override
            public void run()
            {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(mAuth1.getCurrentUser() != null)
                {
                    if(mAuth1.getCurrentUser().isEmailVerified())
                    {

                        DbQuery.loadTestData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {

                                DbQuery.loadUserData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {

                                        Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                                        intent.putExtra("Email",User.getEmail());
                                        intent.putExtra("Name",User.getName());
                                        startActivity(intent);
                                        SplashActivity.this.finish();
                                    }

                                    @Override
                                    public void onFailure() {
                                        // Failed to load user data
                                        Toast.makeText(getApplicationContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this, LoginPage.class);
                        intent.putExtra("text","Please verify your email address");
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }
                else
                {
                    Intent intent = new Intent(SplashActivity.this, LoginPage.class);
                    //intent.putExtra("text","Welcome to GlucoGuide");
                    startActivity(intent);
                    SplashActivity.this.finish();
                }


            }

        }.start();
    }
}