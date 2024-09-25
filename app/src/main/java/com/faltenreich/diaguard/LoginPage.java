package com.faltenreich.diaguard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {

    private EditText email, pass;
    private Button loginB;
    private TextView forgotPassB, signupB;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private TextView dialogText;
    private RelativeLayout google;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 104;

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

        google = findViewById(R.id.googleB);

        progressDialog = new Dialog(LoginPage.this);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Signing In...");

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent intent = getIntent();

        if (intent.hasExtra("text")) {
            String email = intent.getStringExtra("text");
            Toast.makeText(LoginPage.this, email, Toast.LENGTH_SHORT).show();
        }

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

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
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
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                DbQuery.loadTestData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {

                                        DbQuery.loadUserData(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {

                                                progressDialog.dismiss();
                                                Intent intent = new Intent(LoginPage.this, HomeScreenActivity.class);
                                                intent.putExtra("Email",User.getEmail());
                                                intent.putExtra("Name",User.getName());
                                                startActivity(intent);
                                                LoginPage.this.finish();

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
                                        Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(LoginPage.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LoginPage.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(LoginPage.this, "Google Sign In Successful", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser())
                            {
                                DbQuery.createUserData(user.getEmail(),user.getDisplayName(), new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {

                                        DbQuery.loadTestData(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(LoginPage.this, HomeScreenActivity.class);
                                                intent.putExtra("Email",User.getEmail());
                                                intent.putExtra("Name",User.getName());
                                                startActivity(intent);
                                                LoginPage.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                progressDialog.dismiss();
                                                Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure() {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                DbQuery.loadTestData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        DbQuery.loadTestData(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(LoginPage.this, HomeScreenActivity.class);
                                                intent.putExtra("Email",User.getEmail());
                                                intent.putExtra("Name",User.getName());
                                                startActivity(intent);
                                                LoginPage.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                progressDialog.dismiss();
                                                Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure() {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LoginPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}