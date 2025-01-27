package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.R;


public class LoginActivity extends AppCompatActivity {
    TextView click_to_register;
    EditText editTextPhone, editTextPassword;
    FrameLayout login_button;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    SettingsActivity settingsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        click_to_register = findViewById(R.id.click_to_register);

        settingsActivity = new SettingsActivity();

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        login_button.setOnClickListener(view -> loginUser());

        click_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToRegister(v);
            }
        });
    }

    private void loginUser() {
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String dbName = dataSnapshot.child("name").getValue(String.class);
                    String dbPhone = dataSnapshot.child("phone").getValue(String.class);
                    String dbPassword = dataSnapshot.child("password").getValue(String.class);
                    if (dbPassword != null && dbPassword.equals(password)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfromation", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", dbName);
                        editor.putString("phone", dbPhone);
                        editor.apply();

//                        Log.i("Name",dbName);
//                        Log.i("Phone",dbPhone);

                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        ToHome(null);
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


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