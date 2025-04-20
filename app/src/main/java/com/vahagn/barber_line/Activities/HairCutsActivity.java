package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.R;

import java.net.URI;


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