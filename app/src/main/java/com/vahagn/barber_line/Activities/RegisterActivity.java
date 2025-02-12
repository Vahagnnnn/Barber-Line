package com.vahagn.barber_line.Activities;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.Users;
import com.vahagn.barber_line.R;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText first_name, last_name, email, password, phoneNumber;
    FrameLayout register_button;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        register_button = findViewById(R.id.register_button);

        SharedPreferences sharedPreferences = getSharedPreferences("email_str", MODE_PRIVATE);
        email.setText(sharedPreferences.getString("email", " "));

        register_button.setOnClickListener(view -> {
            String first_name_str = first_name.getText().toString();
            String last_name_str = last_name.getText().toString();
            String email_str = email.getText().toString();
            String password_str = password.getText().toString();
            String phoneNumber_str = phoneNumber.getText().toString();

            if (validateInput(first_name_str, last_name_str, email_str, password_str, phoneNumber_str)) {
                signUpUser(first_name_str, last_name_str, email_str, password_str, phoneNumber_str);
            }
        });
    }

    private boolean validateInput(String first_name, String last_name, String email, String password, String phoneNumber) {
        if (first_name.isEmpty() || last_name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(RegisterActivity.this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            Toast.makeText(RegisterActivity.this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phoneNumberPattern = "^\\+374[0-9]{8}$";
        if (!phoneNumber.matches(phoneNumberPattern)) {
            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signUpUser(String first_name, String last_name, String email, String password, String phoneNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // Send verification email
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Verification email sent!", Toast.LENGTH_SHORT).show();

                                            SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("email", email);
                                            editor.apply();

                                            Users user_DB = new Users(first_name + " " + last_name, email, password, phoneNumber, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxJCybEdDgkbOAh8b0fJiCpwmvsGO4-RnLhA&s");
                                            usersRef.child(user.getUid()).setValue(user_DB)
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this, "Store successful", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(RegisterActivity.this, "Failed to store user data: " + task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Failed to send verification email: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //    public void signInUser() {
//
//        String email_str = email.getText().toString().trim();
//        String password_str = password.getText().toString().trim();
//
//        mAuth.signInWithEmailAndPassword(email_str, password_str)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null && user.isEmailVerified()) {
//                            Log.i("email", "email_str " + email_str);
//                            Log.i("email", "password_str " + password_str);
//                            MainActivity.isLogin = true;
//
//                            FirebaseUser currentUser = mAuth.getCurrentUser();
//                            if (currentUser == null) {
//                                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            Users user_DB = new Users(fullName, email, password, phoneNumber, photoUrl);
//
//                            usersRef.child(currentUser.getUid()).setValue(user_DB)
//                                    .addOnCompleteListener(task -> {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(RegisterActivity.this, "Store successful", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        } else {
//                                            Toast.makeText(RegisterActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(PhoneNumberActivity.this, "Please verify your email address.", Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        usersRef.orderByChild("email").equalTo(email_str).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Log.w("DatabaseError", "User check cancelled", databaseError.toException());
//                            }
//                        });
//                    }
//
//                });
//    }
    public void ToHome(View v) {
        navigateTo(MainActivity.class);
    }

    public void ToLogin(View v) {
        navigateTo(LoginActivity.class);
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
