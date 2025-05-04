package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

public class AdminActivity extends AppCompatActivity {

    public  static  boolean AdminActivity;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        SpecialistsFragment.CanBook = false;

        FrameLayout createBarberShop = findViewById(R.id.createBarberShop);
        FrameLayout joinToBarberShop = findViewById(R.id.joinToBarberShop);


        createBarberShop.setOnClickListener(view -> {navigateTo(CreateBarberShopActivity.class);
            AdminActivity = true;});
        joinToBarberShop.setOnClickListener(view -> navigateTo(JoinToBarberShopActivity.class));
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
    public void ToSetting(View view) {
        navigateTo(AdminSettingsActivity.class);
    }
    public void ToBooks(View view) {
        navigateTo(AdminBooksActivity.class);
    }

}