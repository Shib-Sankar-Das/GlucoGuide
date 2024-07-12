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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.faltenreich.diaguard.feature.navigation.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView App_name;

    @SuppressLint("MissingInflatedId")
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

        new Thread(){

            @Override
            public void run()
            {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();

            }

        }.start();
    }
}