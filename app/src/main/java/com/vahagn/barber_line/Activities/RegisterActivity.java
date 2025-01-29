package com.vahagn.barber_line.Activities;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseException;

import java.util.concurrent.TimeUnit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Firebase.PhoneVerificationActivity;
import com.vahagn.barber_line.Firebase.Users;
import com.vahagn.barber_line.R;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextName, editTextPhone, editTextPassword;
    TextView click_to_login;
    FrameLayout register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.name);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);
        click_to_login = findViewById(R.id.click_to_login);

        register_button.setOnClickListener(view -> registerUser());
        click_to_login.setOnClickListener(v -> ToLogin());

    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("phoneNumber", "Starting PhoneVerificationActivity with phone: " + phone);

        Intent intent = new Intent(RegisterActivity.this, PhoneVerificationActivity.class);
        intent.putExtra("phoneNumber", phone);
        startActivity(intent);

        Log.d("phoneNumber", "PhoneVerificationActivity should be opening now.");
    }



    public void ToHome(View v) {
        navigateTo(MainActivity.class);
    }

    public void ToLogin() {
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
