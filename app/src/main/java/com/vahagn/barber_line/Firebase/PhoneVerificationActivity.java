package com.vahagn.barber_line.Firebase;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.R;

public class PhoneVerificationActivity extends AppCompatActivity {
    EditText editTextCode;
    Button verifyButton;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        editTextCode = findViewById(R.id.verification_code);
        verifyButton = findViewById(R.id.verify_button);

        // Get verificationId from intent
        verificationId = getIntent().getStringExtra("verificationId");

        verifyButton.setOnClickListener(view -> verifyCode());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("event_name", "phone_verification_started");  // Add any other parameters if needed
        mFirebaseAnalytics.logEvent("phone_verification", bundle);
    }

    private void verifyCode() {
        String code = editTextCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Enter the verification code", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PhoneVerificationActivity.this, "Phone verified!", Toast.LENGTH_SHORT).show();
                        navigateTo(MainActivity.class);
                    } else {
                        Toast.makeText(PhoneVerificationActivity.this, "Verification failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        finish();
    }
}
