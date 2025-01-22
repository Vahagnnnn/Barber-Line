package com.vahagn.barber_line;

import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class OnClicks extends AppCompatActivity {

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }
}
