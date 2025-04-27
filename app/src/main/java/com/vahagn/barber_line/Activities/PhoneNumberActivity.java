package com.vahagn.barber_line.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
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

    private final String prefix = "+374 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumberEditText = findViewById(R.id.phoneNumber);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();


        phoneNumberEditText.setText(prefix);

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || !s.toString().startsWith(prefix)) {
                    phoneNumberEditText.setText(prefix);
                    phoneNumberEditText.setSelection(prefix.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        phoneNumberEditText.setSelection(prefix.length());
    }

    public void Reg(View view) {
        String phoneNumber = Objects.requireNonNull(phoneNumberEditText.getText()).toString().trim();
//        phoneNumber = phoneNumber.substring(5);
//        if (phoneNumber.isEmpty()) {
//            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
//            return;
//        }
        Log.i("phoneNumber", String.valueOf(phoneNumber.length()));
        if (phoneNumber.length() == 13)
            phoneNumber = phoneNumber.substring(5);
        else {
            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("phoneNumber", phoneNumber);
        phoneNumber = "+374" + phoneNumber;
        Log.i("phoneNumber", phoneNumber);

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
        String first_name = sharedPreferences.getString("first_name", " ");
        String last_name = sharedPreferences.getString("last_name", " ");
        String email = sharedPreferences.getString("email", " ");
        String password = sharedPreferences.getString("password", " ");
        String photoUrl = sharedPreferences.getString("photoUrl", " ");

        Users user_DB = new Users(first_name, last_name, email, password, phoneNumber, photoUrl);
        MainActivity.userClass = user_DB;

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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        FirebaseAuth.getInstance().signOut();
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        Toast.makeText(PhoneNumberActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
//        MainActivity.isLogin = false;
//        Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
