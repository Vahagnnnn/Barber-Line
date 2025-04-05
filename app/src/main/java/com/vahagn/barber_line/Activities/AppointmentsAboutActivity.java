package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

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
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.AppointmentAdapter;

public class AppointmentsAboutActivity extends AppCompatActivity {

    ImageView BarberShopImage;
    TextView BarberShopName,weekDay_monthName_dayOfMonth,ServiceDuration,BarberShopAddress,ServiceName,ServiceDuration1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_about);

        BarberShopImage = findViewById(R.id.BarberShopImage);
        BarberShopName = findViewById(R.id.BarberShopName);
        weekDay_monthName_dayOfMonth = findViewById(R.id.weekDay_monthName_dayOfMonth);
        ServiceDuration = findViewById(R.id.ServiceDuration);
        BarberShopAddress = findViewById(R.id.BarberShopAddress);
        ServiceName = findViewById(R.id.ServiceName);
        ServiceDuration1 = findViewById(R.id.ServiceDuration1);

        Glide.with(this)
                .load(AppointmentAdapter.BarbershopImageUrl)
                .into(BarberShopImage);

        BarberShopName.setText(getIntent().getStringExtra("BarberShopName"));
        weekDay_monthName_dayOfMonth.setText(getIntent().getStringExtra("weekDay_monthName_dayOfMonth"));
        ServiceDuration.setText(getIntent().getStringExtra("ServiceDuration"));
        BarberShopAddress.setText(getIntent().getStringExtra("BarberShopAddress"));
        ServiceName.setText(getIntent().getStringExtra("ServiceName"));
        ServiceDuration1.setText(getIntent().getStringExtra("ServiceDuration"));
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
    public void ToManageAppointment(View view) {
        navigateTo(ManageAppointmentActivity.class);
    }

    public void ToBooks(View view) {
        if (isLogin)
            navigateTo(BooksActivity.class);
        else
            navigateTo(LoginActivity.class);
    }

    public void ToAdmin(View view) {
        if (isLogin)
            navigateTo(AdminActivity.class);
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