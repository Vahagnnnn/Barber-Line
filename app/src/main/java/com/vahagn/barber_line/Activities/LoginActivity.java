package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Classes.Users;
import com.vahagn.barber_line.R;


public class LoginActivity extends AppCompatActivity {
    TextView click_to_register;
    EditText email, password;
    FrameLayout login_button;
    private FirebaseAuth mAuth;

    private DatabaseReference usersRef;

    SettingsActivity settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        click_to_register = findViewById(R.id.click_to_register);
        settingsActivity = new SettingsActivity();

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        login_button.setOnClickListener(view -> {
            if (!validateEmail() || !validatePassword()) {
                Toast.makeText(LoginActivity.this, "Invalid information", Toast.LENGTH_SHORT).show();
            } else {
                signInUser();
            }
        });

        click_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToRegister(v);
            }
        });
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
                                            Log.i("email","send");
                                            editor.apply();
                                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                            MainActivity.isLogin=true;
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

//    private void loginUser() {
//        String email_str = email.getText().toString().trim();
//        String password_str = password.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email_str) || TextUtils.isEmpty(password_str)) {
//            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        usersRef.child(email_str).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String dbName = dataSnapshot.child("name").getValue(String.class);
//                    String dbPhone = dataSnapshot.child("phone").getValue(String.class);
//                    String dbPassword = dataSnapshot.child("password").getValue(String.class);
//                    if (dbPassword != null && dbPassword.equals(password)) {
//                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfromation", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("name", dbName);
//                        editor.putString("phone", dbPhone);
//                        editor.apply();
//
////                        Log.i("Name",dbName);
////                        Log.i("Phone",dbPhone);
//
//                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                        ToHome(null);
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToRegister(View view) {
        navigateTo(RegisterActivity.class);
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