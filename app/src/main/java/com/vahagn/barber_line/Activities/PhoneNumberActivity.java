package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.vahagn.barber_line.R;

import java.util.Objects;

public class PhoneNumberActivity extends AppCompatActivity {

    private TextInputEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumberEditText = findViewById(R.id.phoneNumber);
    }

    public void Reg(View view) {
        String phoneNumber = Objects.requireNonNull(phoneNumberEditText.getText()).toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phoneNumber", phoneNumber);
        }
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
