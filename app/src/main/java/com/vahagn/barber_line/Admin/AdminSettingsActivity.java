package com.vahagn.barber_line.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminSettingsActivity extends AppCompatActivity {

    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
    DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("pending_barbershops");
    DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");

    public static String imageUrl, name, logo, rating, address,coordinates;
    public static List<Barbers> ListSpecialist = new ArrayList<>();
    public static List<Reviews> ListReviews = new ArrayList<>();
    public static Map<String, TimeRange> openingTimes = new HashMap<>();
    public static String workPlace;

    String ownerEmail;

//    FrameLayout Confirm_List_Layout, Wait_List_Layout, Rejected_List_Layout;
    TextView List_TextView;
    LinearLayout barbershops_list;

//    TextView Confirm_List_TextView, Wait_List_TextView, Rejected_List_TextView;

    public static  List<Barbers> applicant_barber = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);
        barbershops_list = findViewById(R.id.barbershops_list);
//        LinearLayout confirm_barbershops_container = findViewById(R.id.confirm_barbershops_list);
//        LinearLayout pending_barbershops_container = findViewById(R.id.pending_barbershops_list);
//        LinearLayout rejected_barbershops_container = findViewById(R.id.rejected_barbershops_list);

        List_TextView = findViewById(R.id.List_TextView);

//        Confirm_List_TextView = findViewById(R.id.Confirm_List_TextView);
//        Wait_List_TextView = findViewById(R.id.Wait_List_TextView);
//        Rejected_List_TextView = findViewById(R.id.Rejected_List_TextView);

//        Confirm_List_TextView.setVisibility(GONE);
//        Wait_List_TextView.setVisibility(GONE);
//        Rejected_List_TextView.setVisibility(GONE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            ownerEmail = currentUser.getEmail();
        } else {
            ownerEmail = "";
        }
        AddConfirm(null);

//        Confirm_List_Layout = findViewById(R.id.Confirm_List_Layout);
//        Wait_List_Layout = findViewById(R.id.Wait_List_Layout);
//        Rejected_List_Layout = findViewById(R.id.Rejected_List_Layout);
//
//        Confirm_List_Layout.setOnClickListener(v -> AddConfirm(ownerEmail));
//        Wait_List_Layout.setOnClickListener(v -> AddWait(ownerEmail));
//        Rejected_List_Layout.setOnClickListener(v -> AddRejected(ownerEmail));

    }

    public void AddConfirm(View view) {
        List_TextView.setText("Operating Barbershops");
        barbershops_list.removeAllViews();
        barberShopsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(),shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
                        workPlace = shop.getName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void AddWait(View view) {
        List_TextView.setText("Pending Barbershops");
        barbershops_list.removeAllViews();

        pendingRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        Log.i("pendingRef",shop.getName());
                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(),shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void AddRejected(View view) {
        List_TextView.setText("Rejected Barbershops");
        barbershops_list.removeAllViews();

        rejectedRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getCoordinates(),shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes(), shop.getReason());
                        List_TextView.setText("Rejected Barbershops");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }


    public void addBarbershop(String logo, String imageUrl, String name, double rating, String address,String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, barbershops_list, false);
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

            AdminSettingsActivity.imageUrl = imageUrl;
            AdminSettingsActivity.name = name;
            AdminSettingsActivity.logo = logo;
            AdminSettingsActivity.rating = String.valueOf(rating);
            AdminSettingsActivity.address = address;
            AdminSettingsActivity.coordinates = coordinates;
            AdminSettingsActivity.ListSpecialist = ListSpecialist;
            AdminSettingsActivity.ListReviews = ListReviews;
            AdminSettingsActivity.openingTimes = openingTimes;

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });

//        barbershops_list.removeAllViews();
        barbershops_list.addView(barbershopView);
    }

    public void addBarbershop(String logo, String imageUrl, String name, double rating, String address, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes, String reason) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, barbershops_list, false);
        ImageView logoImageView = barbershopView.findViewById(R.id.logo);

        Glide.with(this)
                .load(logo)
                .into(logoImageView);

        TextView nameTextView = barbershopView.findViewById(R.id.name);
        TextView addressTextView = barbershopView.findViewById(R.id.address);

        nameTextView.setText(name);
//        nameTextView.setText(reason);
        addressTextView.setText(address);


        barbershopView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);

            AdminSettingsActivity.imageUrl = imageUrl;
            AdminSettingsActivity.name = name;
            AdminSettingsActivity.logo = logo;
            AdminSettingsActivity.rating = String.valueOf(rating);
            AdminSettingsActivity.coordinates = coordinates;
            AdminSettingsActivity.address = address;
            AdminSettingsActivity.ListSpecialist = ListSpecialist;
            AdminSettingsActivity.ListReviews = ListReviews;
            AdminSettingsActivity.openingTimes = openingTimes;

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });
//        barbershops_list.removeAllViews();
        barbershops_list.addView(barbershopView);
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

    public void ToBooks(View view) {
        navigateTo(AdminBooksActivity.class);
    }

    public void ToApplicant_Barber(View view) {
        if (workPlace != null) {
            DatabaseReference applicant_barbersShopsRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");
            applicant_barber.clear();
            applicant_barbersShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Barbers barber = snapshot.getValue(Barbers.class);
                        if (barber != null && Objects.equals(barber.getWorkPlace(), workPlace)) {
                            applicant_barber.add(barber);
                            found = true;
                            Log.i("workPlace", "IF " + workPlace);
                        }
                    }

                    if (found) {
                        navigateTo(Applicant_BarberActivity.class);
                    } else {
                        Log.i("workPlace", "No matching barbers");
                        Toast.makeText(AdminSettingsActivity.this, "You don't have Applicant Barbers", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Firebase", "Failed to read value.", databaseError.toException());
                }
            });
        } else {
            Toast.makeText(this, "You don't have your own BarberShop", Toast.LENGTH_SHORT).show();
        }
    }

    public void ToBarberProfile(View view) {
        navigateTo(BarberProfileActivity.class);
    }
}