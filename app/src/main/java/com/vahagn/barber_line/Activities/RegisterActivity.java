package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Firebase.Users;
import com.vahagn.barber_line.R;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextName, editTextPhone, editTextPassword;
    TextView click_to_login;
    FrameLayout register_button;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName = findViewById(R.id.name);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);
        click_to_login = findViewById(R.id.click_to_login);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        register_button.setOnClickListener(view -> registerUser());

        click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLogin();
            }
        });
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Users user = new Users(name, phone, password);
        usersRef.child(phone).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        ToLogin();
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ToHome(View view) {
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