package com.vahagn.barber_line.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vahagn.barber_line.R;

public class BarberShopOwnerActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barber_shop);

        FrameLayout createBarberShop = findViewById(R.id.createBarberShop);
        FrameLayout joinToBarberShop = findViewById(R.id.joinToBarberShop);

        View.OnTouchListener touchEffect = (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().alpha(0.8f).setDuration(50).start();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().alpha(1.0f).setDuration(50).start();
                    break;
            }
            return false;
        };


        createBarberShop.setOnTouchListener(touchEffect);
        joinToBarberShop.setOnTouchListener(touchEffect);


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

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }
}