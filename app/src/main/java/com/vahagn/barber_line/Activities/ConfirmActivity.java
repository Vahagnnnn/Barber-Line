package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.R;

public class ConfirmActivity extends AppCompatActivity {

    ImageView BarberShopImage;
    TextView BarberShopName, BarberShopAddress, weekDay_monthName_dayOfMonth, Time, ServiceName, Duration, Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();

        BarberShopImage = findViewById(R.id.BarberShopImage);
        BarberShopName = findViewById(R.id.BarberShopName);
        BarberShopAddress = findViewById(R.id.BarberShopAddress);
        weekDay_monthName_dayOfMonth = findViewById(R.id.weekDay_monthName_dayOfMonth);
        Time = findViewById(R.id.Time);
        ServiceName = findViewById(R.id.ServiceName);
        Price = findViewById(R.id.Price);
        Duration = findViewById(R.id.Duration);

        Glide.with(this)
                .load(BarbersActivity.imageUrl)
                .into(BarberShopImage);
        BarberShopName.setText(BarbersActivity.name);
        BarberShopAddress.setText(BarbersActivity.address);
        weekDay_monthName_dayOfMonth.setText(intent.getStringExtra("weekDay_monthName_dayOfMonth"));
        Time.setText(intent.getStringExtra("Time"));
        ServiceName.setText(ServicesFragment.name);
        Price.setText(ServicesFragment.price);
        Duration.setText(ServicesFragment.duration);
    }

    public void Back(View view) {
        onBackPressed();
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
}