package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.R;

public class HairCutsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_cuts);

    }

    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }

    public void ToSetting(View view) {
        navigateTo(SettingsActivity.class);
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }


    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }

    public void To(View view) {
        Log.i("isLogin", String.valueOf(MainActivity.isLogin));
        if (MainActivity.isLogin) {
            Log.i("isLogin", String.valueOf(MainActivity.isLogin));
            navigateTo(SettingsActivity.class);
        } else {
            Log.i("isLogin", String.valueOf(MainActivity.isLogin));
            navigateTo(LoginActivity.class);
        }

    }

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }

    public void ToAdmin(View view) {
        if (MainActivity.isLogin)
            navigateTo(AdminActivity.class);
        else
            navigateTo(LoginActivity.class);
    }
    public void ToBooks(View view) {
        if (MainActivity.isLogin)
            navigateTo(BooksActivity.class);
        else
            navigateTo(LoginActivity.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

}