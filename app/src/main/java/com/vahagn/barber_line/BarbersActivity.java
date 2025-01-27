package com.vahagn.barber_line;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.FirebaseDatabaseClasses.BarberShops;
import com.vahagn.barber_line.FirebaseDatabaseClasses.BarberShopsDetail;
import com.vahagn.barber_line.database.BarberLineDatabaseHelper;

import java.util.ArrayList;

public class BarbersActivity extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("topBarberShops");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);
        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShopsDetail shop = snapshot.getValue(BarberShopsDetail.class);
                    int logoResId = getResources().getIdentifier(shop.getLogo(), "drawable", getPackageName());
                    addBarbershop(secondActivityContainer, shop.getName(), shop.getAddress(), logoResId);
                    Log.d("Firebase", "Barber Shop: " + shop.getName() + ", Address: " + shop.getAddress()+ ", imageResId: " + logoResId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
//        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
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