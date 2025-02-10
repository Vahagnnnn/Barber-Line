package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Classes.Users;
import com.vahagn.barber_line.R;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    TextView click_to_register;
    EditText email, password;
    FrameLayout login_button;
    SignInButton googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        click_to_register = findViewById(R.id.click_to_register);
        googleSignInButton = findViewById(R.id.sign_in_button);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        login_button.setOnClickListener(view -> {
            if (!validateEmail() || !validatePassword()) {
                Toast.makeText(LoginActivity.this, "Invalid information", Toast.LENGTH_SHORT).show();
            } else {
                signInUser();
            }
        });

        click_to_register.setOnClickListener(v -> ToRegister(v));

        googleSignInButton.setOnClickListener(v -> signIn());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // From google-services.json
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void signInUser() {
        String email_str = email.getText().toString().trim();
        String password_str = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Users user_DB = new Users(email_str, password_str);
                            usersRef.child(user.getUid()).setValue(user_DB)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("email", email_str);
                                            editor.putString("password", password_str);
                                            editor.apply();
                                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                            MainActivity.isLogin = true;
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, "Please verify your email address.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Toast.makeText(LoginActivity.this, "Intent", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());

            String fullName = account.getDisplayName();
            String email = account.getEmail();
            Uri photoUrl = account.getPhotoUrl();
            Log.i("email", "email " + email + " fullName " + fullName + " photoUrl " + photoUrl);

            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null && user.isEmailVerified()) {
                Users user_DB = new Users(fullName, email, photoUrl);
                usersRef.child(user.getUid()).setValue(user_DB)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("firstname_lastnameText", fullName);
                                editor.putString("photoUrl", String.valueOf(photoUrl));
                                editor.apply();

                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                MainActivity.isLogin = true;
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                            }
                        });

                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Please verify your email address.", Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Log.w("FirebaseAuth", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    public boolean validateEmail() {
        String val = email.getText().toString();
        if (val.isEmpty()) {
            email.setError("Email can't be empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password can't be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToRegister(View view) {
        navigateTo(RegisterActivity.class);
    }

    public void ToForgot(View view) {
        navigateTo(ForgotPasswordActivity.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.container_login),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
}