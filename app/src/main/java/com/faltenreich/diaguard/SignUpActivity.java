package com.faltenreich.diaguard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, pass, confirmPass;
    private Button signupB;
    private TextView loginb;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailid);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        signupB = findViewById(R.id.signupb);
        loginb = findViewById(R.id.loginb);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Signing Up...");

        mAuth = FirebaseAuth.getInstance();

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateData()) {
                    signupNewUser();
                }


            }
        });

        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this,LoginPage.class);
                startActivity(intent);
                finish();

            }
        });



    }

    private boolean validateData()
    {
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if (nameStr.isEmpty())
        {
            name.setError("Name is required");
            name.requestFocus();
            return false;
        }

        if (emailStr.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        }

        if (passStr.isEmpty())
        {
            pass.setError("Password is required");
            pass.requestFocus();
            return false;
        }

        if (confirmPassStr.isEmpty())
        {
            confirmPass.setError("Confirm Password is required");
            confirmPass.requestFocus();
            return false;
        }

        if (!passStr.equals(confirmPassStr))
        {
            confirmPass.setError("Password and Confirm Password should be same");
            confirmPass.requestFocus();
            return false;
        }

        return true;
    }


    private void signupNewUser()
    {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {

                                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                                        DbQuery.createUserData(emailStr,nameStr, new MyCompleteListener(){

                                            @Override
                                            public void onSuccess(){

                                                progressDialog.dismiss();
                                                mAuth.signOut();

                                                Intent intent = new Intent(SignUpActivity.this,LoginPage.class);
                                                intent.putExtra("text","Please verify your email address");
                                                startActivity(intent);
                                                SignUpActivity.this.finish();

                                            }

                                            @Override
                                            public void onFailure(){

                                                Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                            }


                                        });


                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Failed to send Verification Email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });// Add code here to sign up a new user
    }


}