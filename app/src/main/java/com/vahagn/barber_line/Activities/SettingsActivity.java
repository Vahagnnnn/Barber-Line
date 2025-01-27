package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vahagn.barber_line.R;

public class SettingsActivity extends AppCompatActivity {
    FirebaseAuth auth;
    TextView phoneNumber_text,logout_button;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth = FirebaseAuth.getInstance();
        logout_button = findViewById(R.id.logout_button);
        phoneNumber_text = findViewById(R.id.phoneNumber_text);
        user = auth.getCurrentUser();
        Log.i("TEST","Test");
        if (user != null) {
            phoneNumber_text.setText(user.getPhoneNumber());
        }
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }
    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }
}