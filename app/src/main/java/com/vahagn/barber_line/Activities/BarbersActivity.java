package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.List;

public class BarbersActivity extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("barberShops");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);
        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    Log.i("getServices",shop.getServices().toString());
                    addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
//        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
    }


    public void addBarbershop(LinearLayout container, String logo, String image, String name, double rating, String address, List<Barbers> ListSpecialist, List<Services> ListService) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);

        ImageView logoImageView = barbershopView.findViewById(R.id.logo);
        int logoResId = getResources().getIdentifier(logo, "drawable", getPackageName());
        logoImageView.setImageResource(logoResId);

        TextView nameTextView = barbershopView.findViewById(R.id.name);
        TextView addressTextView = barbershopView.findViewById(R.id.address);

        nameTextView.setText(name);
        addressTextView.setText(address);


        barbershopView.setOnClickListener(v -> {
            Intent intent = new Intent(BarbersActivity.this, BarberShopsAboutActivity.class);
            intent.putExtra("from_where", "BarbersActivity");
            intent.putExtra("image", image);
            intent.putExtra("name", name);
            intent.putExtra("rating", String.valueOf(rating));
            intent.putExtra("address", address);
            intent.putExtra("ListSpecialist", (Serializable) ListSpecialist);
            intent.putExtra("ListService", (Serializable) ListService);

            startActivity(intent);
        });

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