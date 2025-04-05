package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.AppointmentAdapter;

public class AppointmentsAboutActivity extends AppCompatActivity {
    DatabaseReference barberShops = FirebaseDatabase.getInstance().getReference("barberShops");

    ImageView BarberShopImage;
    TextView BarberShopName, weekDay_monthName_dayOfMonth, ServiceDuration, BarberShopAddress, ServiceName, ServiceDuration1;

    LinearLayout venue_details;

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

        venue_details = findViewById(R.id.venue_details);
        venue_details.setOnClickListener(v -> {
            Intent intent = new Intent(this, BarberShopsAboutActivity.class);
            intent.putExtra("from_where", "AppointmentsAboutActivity");

            BarbersActivity.imageUrl = AppointmentAdapter.BarbershopImageUrl;
            BarbersActivity.name = getIntent().getStringExtra("BarberShopName");
            BarbersActivity.rating = getIntent().getStringExtra("BarbershopRating");
            BarbersActivity.address = getIntent().getStringExtra("BarberShopAddress");

            barberShops.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BarberShops shop = snapshot.getValue(BarberShops.class);
                        if (shop != null && shop.getName().equals(BarbersActivity.name)) {
                            BarbersActivity.ListSpecialist = shop.getSpecialists();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Firebase", "Failed to read value.", databaseError.toException());
                }
            });


            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });
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