package com.faltenreich.diaguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginPage extends AppCompatActivity {

    private EditText email, pass;
    private Button loginB;
    private TextView forgotPassB, signupB;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        loginB = findViewById(R.id.loginB);
        forgotPassB = findViewById(R.id.forgot_pass);
        signupB = findViewById(R.id.signupb);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateData())
                {
                    login();
                }

            }
        });

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private boolean validateData()
    {

        if (email.getText().toString().isEmpty())
        {
            email.setError("Enter Email-ID");
            return false;
        }

        if (pass.getText().toString().isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }

        return true;
    }


    private void login()
    {

    }

}