package com.vahagn.barber_line.Admin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vahagn.barber_line.R;

public class AdminBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);

    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }
    public void ToSetting(View view) {
        navigateTo(AdminSettingsActivity.class);
    }

}