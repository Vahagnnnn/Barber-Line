package com.vahagn.barber_line;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vahagn.barber_line.database.BarberLineDatabaseHelper;

import java.util.ArrayList;

public class BarbersActivity extends AppCompatActivity {

    BarberLineDatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);
        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
    }


    public void addBarbershop(LinearLayout container, String titleText, String addressText, int imageResId) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);

        TextView title = barbershopView.findViewById(R.id.title);
        TextView address = barbershopView.findViewById(R.id.address);
        ImageView logo = barbershopView.findViewById(R.id.logo);

        title.setText(titleText);
        address.setText(addressText);
        logo.setImageResource(imageResId);

        container.addView(barbershopView);
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }
    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }
    public void ToSettings(View view) {
        navigateTo(SettingsActivity.class);
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