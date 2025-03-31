package com.vahagn.barber_line.Admin;

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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminSettingsActivity extends AppCompatActivity {

    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
    DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("pending_barbershops");
    DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");


    public static String imageUrl,name,rating,address;
    public static  List<Barbers> ListSpecialist = new ArrayList<>();
    public static List<Services> ListService = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        LinearLayout confirm_barbershops_container = findViewById(R.id.confirm_barbershops_list);
        LinearLayout pending_barbershops_container = findViewById(R.id.pending_barbershops_list);
        LinearLayout rejected_barbershops_container = findViewById(R.id.rejected_barbershops_list);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String ownerEmail = "";

        if (currentUser != null) {
            ownerEmail = currentUser.getEmail();
            Log.d("FirebaseAuth", "Current user email: " + ownerEmail);
        } else {
            Log.e("FirebaseAuth", "No user is signed in");
        }

        String OwnerEmail = ownerEmail;
        barberShopsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    if (shop != null && Objects.equals(shop.getOwnerEmail(), OwnerEmail)) {
                        Log.i("getServices", shop.getServices().toString());
                        addBarbershop(confirm_barbershops_container, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices());
                    }
//                    Log.i("getServices", shop.getServices().toString());
//                    addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });

        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    if (shop != null && Objects.equals(shop.getOwnerEmail(), OwnerEmail)) {
                        Log.i("getServices", shop.getServices().toString());
                        addBarbershop(pending_barbershops_container, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices());
                    }
//                    Log.i("getServices", shop.getServices().toString());
//                    addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });

        rejectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    if (shop != null && Objects.equals(shop.getOwnerEmail(), OwnerEmail)) {
                        Log.i("getServices", shop.getServices().toString());
                        addBarbershop(rejected_barbershops_container, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices(), shop.getReason());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });

    }

    public void addBarbershop(LinearLayout container, String logo, String imageUrl, String name, double rating, String address, List<Barbers> ListSpecialist, List<Services> ListService) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);
        ImageView logoImageView = barbershopView.findViewById(R.id.logo);

        Glide.with(this)
                .load(logo)
                .into(logoImageView);

        TextView nameTextView = barbershopView.findViewById(R.id.name);
        TextView addressTextView = barbershopView.findViewById(R.id.address);

        nameTextView.setText(name);
        addressTextView.setText(address);


        barbershopView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//            intent.putExtra("from_where", "BarbersActivity");
//            intent.putExtra("image", image);
//            intent.putExtra("name", name);
//            intent.putExtra("rating", String.valueOf(rating));
//            intent.putExtra("address", address);
//            intent.putExtra("ListSpecialist", (Serializable) ListSpecialist);
//            intent.putExtra("ListService", (Serializable) ListService);

            AdminSettingsActivity.imageUrl = imageUrl;
            AdminSettingsActivity.name = name;
            AdminSettingsActivity.rating = String.valueOf(rating);
            AdminSettingsActivity.address = address;
            AdminSettingsActivity.ListSpecialist = ListSpecialist;
            AdminSettingsActivity.ListService = ListService;

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });

        container.addView(barbershopView);
    }

    public void addBarbershop(LinearLayout container, String logo, String imageUrl, String name, double rating, String address, List<Barbers> ListSpecialist, List<Services> ListService, String reason) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);
        ImageView logoImageView = barbershopView.findViewById(R.id.logo);

        Glide.with(this)
                .load(logo)
                .into(logoImageView);

        TextView nameTextView = barbershopView.findViewById(R.id.name);
        TextView addressTextView = barbershopView.findViewById(R.id.address);

//        nameTextView.setText(name);
        nameTextView.setText(reason);
        addressTextView.setText(address);


        barbershopView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//            intent.putExtra("from_where", "BarbersActivity");
//            intent.putExtra("image", imageUrl);
//            intent.putExtra("name", name);
//            intent.putExtra("rating", String.valueOf(rating));
//            intent.putExtra("address", address);
//            intent.putExtra("ListSpecialist", (Serializable) ListSpecialist);
//            intent.putExtra("ListService", (Serializable) ListService);

            AdminSettingsActivity.imageUrl = imageUrl;
            AdminSettingsActivity.name = name;
            AdminSettingsActivity.rating = String.valueOf(rating);
            AdminSettingsActivity.address = address;
            AdminSettingsActivity.ListSpecialist = ListSpecialist;
            AdminSettingsActivity.ListService = ListService;

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });

        container.addView(barbershopView);
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToCreateBarberShop(View view) {
        navigateTo(CreateBarberShopActivity.class);
    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }

    public void ToBook(View view) {
        navigateTo(AdminActivity.class);
    }
}