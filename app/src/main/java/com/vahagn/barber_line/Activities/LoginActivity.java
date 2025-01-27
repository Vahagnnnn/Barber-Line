package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.R;


public class LoginActivity extends AppCompatActivity {
    TextView click_to_register;
    EditText editTextPhone, editTextPassword;
    DatabaseReference MyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextPhone = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);
        click_to_register = findViewById(R.id.click_to_register);
        MyDB = FirebaseDatabase.getInstance().getReference("message");

        click_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToRegister(v);
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