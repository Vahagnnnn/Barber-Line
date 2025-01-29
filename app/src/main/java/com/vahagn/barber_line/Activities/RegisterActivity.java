package com.vahagn.barber_line.Activities;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextName, editTextPhone, editTextPassword;
    TextView click_to_login;
    FrameLayout register_button;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        editTextName = findViewById(R.id.name);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);
        click_to_login = findViewById(R.id.click_to_login);

        // Initialize Firebase888888
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        // Register button click
        register_button.setOnClickListener(view -> registerUser());

        // Login link click
        click_to_login.setOnClickListener(v -> ToLogin());



    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the phone number starts with Armenian country code (+374)
        if (!phone.startsWith("+374")) {
            Toast.makeText(this, "Please enter a valid Armenian phone number with the +374 country code.", Toast.LENGTH_LONG).show();
            return;
        }

        // Save user details in Firebase Database (before phone verification)
        Users user = new Users(name, phone, password);
        usersRef.child(phone).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration successful! Sending verification code...", Toast.LENGTH_SHORT).show();
                        sendVerificationCode(phone);
                    } else {
                        Toast.makeText(this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)       // Phone number to verify (e.g., +37499123456)
                .setTimeout(60L, TimeUnit.SECONDS)  // Timeout duration
                .setActivity(this)             // Activity for callback
                .setCallbacks(mCallbacks)     // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {



            if (e instanceof FirebaseNetworkException) {
                // Log and show message
                Log.e("VerificationFailed", "Network error", e);
                Toast.makeText(RegisterActivity.this, "Network error. Please check your internet connection.", Toast.LENGTH_LONG).show();
            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Log and show message
                Log.e("VerificationFailed", "Invalid phone number format", e);
                Toast.makeText(RegisterActivity.this, "Invalid phone number format. Please check the number.", Toast.LENGTH_LONG).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // Log and show message
                Log.e("VerificationFailed", "Too many requests", e);
                Toast.makeText(RegisterActivity.this, "Too many verification requests. Please try again later.", Toast.LENGTH_LONG).show();
            } else {
                // Log and show message
                Log.e("VerificationFailed", "Unexpected error", e);
                Toast.makeText(RegisterActivity.this, "Verification failed due to an unexpected error. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            Log.i("onCodeSent","onCodeSent");
            // Code sent, redirect to the phone verification activity
            Intent intent = new Intent(RegisterActivity.this, PhoneVerificationActivity.class);
            intent.putExtra("verificationId", verificationId);  // Pass the verification ID to the next activity
            startActivity(intent);
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        // Successfully signed in
                        Toast.makeText(RegisterActivity.this, "Phone verified!", Toast.LENGTH_SHORT).show();
                        // Proceed to the home screen
                        ToHome();
                    } else {
                        // Sign in failed
                        Toast.makeText(RegisterActivity.this, "Phone verification failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ToHome() {
        navigateTo(MainActivity.class);
    }

    public void ToLogin() {
        navigateTo(LoginActivity.class);
        finish();
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
