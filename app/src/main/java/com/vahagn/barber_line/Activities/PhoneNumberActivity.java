package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Classes.Users;
import com.vahagn.barber_line.R;

import java.util.Objects;

public class PhoneNumberActivity extends AppCompatActivity {

    private TextInputEditText phoneNumberEditText;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumberEditText = findViewById(R.id.phoneNumber);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    public void Reg(View view) {
        String phoneNumber = Objects.requireNonNull(phoneNumberEditText.getText()).toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneNumberPattern = "^\\+374[0-9]{8}$";
        if (!phoneNumber.matches(phoneNumberPattern)) {
            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("firstname_lastnameText", " ");
        String email = sharedPreferences.getString("email", " ");
        String password = sharedPreferences.getString("password", " ");
        String photoUrl = sharedPreferences.getString("photoUrl", " ");

        Users user_DB = new Users(fullName, email, password, phoneNumber, photoUrl);

        usersRef.child(currentUser.getUid()).setValue(user_DB)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PhoneNumberActivity.this, "Store successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PhoneNumberActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ToLogin(View view) {
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
