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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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

//    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
//    DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("pending_barbershops");
//    DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");

    public static String imageUrl, name, logo, rating, address,coordinates;
    public static List<Barbers> ListSpecialist = new ArrayList<>();
    public static List<Reviews> ListReviews = new ArrayList<>();
    public static Map<String, TimeRange> openingTimes = new HashMap<>();
    public static String workPlace;

//
//    String ownerEmail;
//
//    TextView List_TextView;
//    LinearLayout barbershops_list;
//    private ProgressBar loadingProgressBar;
//
    public static  List<Barbers> applicant_barber = new ArrayList<>();
//
//    LinearLayout wait_for_confirmation;

    TextView nameText,addressText,ratingText,reject_reason_Text;
    ImageView barbershop_logo ;

    LinearLayout aboutLayout, wait_for_confirmation, rejected;
    ConstraintLayout settingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);







//        barbershops_list = findViewById(R.id.barbershops_list);
//        List_TextView = findViewById(R.id.List_TextView);
//        loadingProgressBar = findViewById(R.id.loading_progress_bar);
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        wait_for_confirmation = findViewById(R.id.wait_for_confirmation);
//        wait_for_confirmation.setVisibility(View.VISIBLE);
//
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            ownerEmail = currentUser.getEmail();
//        } else {
//            ownerEmail = "";
//        }
//        AddConfirm(null);
    }


    private void ReadBarberID() {

        barbershop_logo = findViewById(R.id.barbershop_logo);
        nameText = findViewById(R.id.nameText);
        addressText = findViewById(R.id.addressText);
        ratingText = findViewById(R.id.ratingText);

        aboutLayout = findViewById(R.id.aboutLayout);
        settingLayout = findViewById(R.id.settingLayout);

        wait_for_confirmation = findViewById(R.id.wait_for_confirmation);
        rejected = findViewById(R.id.rejected);
        reject_reason_Text = findViewById(R.id.reject_reason_Text);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            String uid = currentUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);


            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (Objects.equals(dataSnapshot.child("status").getValue(String.class), "pending")) {
                        rejected.setVisibility(GONE);
                        wait_for_confirmation.setVisibility(VISIBLE);
                    } else if (Objects.equals(dataSnapshot.child("status").getValue(String.class), "rejected")) {
                        wait_for_confirmation.setVisibility(GONE);
                        rejected.setVisibility(VISIBLE);
                        reject_reason_Text.setText("Reason: " + dataSnapshot.child("rejection_reason").getValue(String.class));
                    } else {
                        Integer myWorkplaceId = dataSnapshot.child("myWorkplaceId").getValue(Integer.class);
                        Integer myIdAsBarber = dataSnapshot.child("myIdAsBarber").getValue(Integer.class);

                        if (myWorkplaceId != null && myIdAsBarber != null) {
                            setInfo(myIdAsBarber, myWorkplaceId);

                            aboutLayout.setVisibility(VISIBLE);
                            settingLayout.setVisibility(VISIBLE);
                            wait_for_confirmation.setVisibility(GONE);
                            rejected.setVisibility(GONE);
                        } else {
                            Toast.makeText(AdminSettingsActivity.this, "Insufficient data to load the profile", Toast.LENGTH_SHORT).show();
                            Log.e("barberShopsId", "myWorkplaceId or myIdAsBarber is null");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Database error: " + databaseError.getMessage());
                }
            });
        }

        if (nameText.getText() == "Name Surname") {

        }
    }

    private void setInfo(Integer barberShopsId, Integer barberId) {
        DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops").child(String.valueOf(barberShopsId)).child("specialists");
        Log.i("myWorkplaceId", "barberShopsId = " + barberShopsId);

        barberShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("myWorkplaceId", String.valueOf(barberId));

                for (DataSnapshot child : snapshot.getChildren()) {
                    Barbers barber = child.getValue(Barbers.class);
                    if (barber != null && barberId == barber.getBarberId()) {
                        nameText.setText(barber.getName());
                        String phone = barber.getPhoneNumber().substring(0, 5) + " " +
                                barber.getPhoneNumber().substring(5, 7) + " " +
                                barber.getPhoneNumber().substring(7, 9) + " " +
                                barber.getPhoneNumber().substring(9);
                        addressText.setText(phone);
                        ratingText.setText(barber.getRating() + "★");

                        String image = barber.getImage();
                        if (image != null && !image.isEmpty()) {
                            Glide.with(AdminSettingsActivity.this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(100))).into(barbershop_logo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }


//    public void AddConfirm(View view) {
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        List_TextView.setText("Operating Barbershops");
//        barbershops_list.removeAllViews();
//        barberShopsRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                barbershops_list.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BarberShops shop = snapshot.getValue(BarberShops.class);
//                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(),shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
//                        workPlace = shop.getName();
//                    }
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }

//    public void AddWait(View view) {
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        List_TextView.setText("Pending Barbershops");
//        barbershops_list.removeAllViews();
//
//        pendingRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                barbershops_list.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BarberShops shop = snapshot.getValue(BarberShops.class);
//                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(),shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
//                    }
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }

//    public void AddRejected(View view) {
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        List_TextView.setText("Rejected Barbershops");
//        barbershops_list.removeAllViews();
//
//        rejectedRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                barbershops_list.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BarberShops shop = snapshot.getValue(BarberShops.class);
//                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getCoordinates(),shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes(), shop.getReason());
//                        List_TextView.setText("Rejected Barbershops");
//                    }
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }


//    public void addBarbershop(String logo, String imageUrl, String name, double rating, String address,String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes) {
//        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, barbershops_list, false);
//        ImageView logoImageView = barbershopView.findViewById(R.id.logo);
//
//        Glide.with(this)
//                .load(logo)
//                .into(logoImageView);
//
//        TextView nameTextView = barbershopView.findViewById(R.id.name);
//        TextView addressTextView = barbershopView.findViewById(R.id.address);
//
//        nameTextView.setText(name);
//        addressTextView.setText(address);
//
//
//        barbershopView.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//
//            AdminSettingsActivity.imageUrl = imageUrl;
//            AdminSettingsActivity.name = name;
//            AdminSettingsActivity.logo = logo;
//            AdminSettingsActivity.rating = String.valueOf(rating);
//            AdminSettingsActivity.address = address;
//            AdminSettingsActivity.coordinates = coordinates;
//            AdminSettingsActivity.ListSpecialist = ListSpecialist;
//            AdminSettingsActivity.ListReviews = ListReviews;
//            AdminSettingsActivity.openingTimes = openingTimes;
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                    this,
//                    findViewById(R.id.bottom_navigation),
//                    "sharedImageTransition");
//            startActivity(intent, options.toBundle());
//        });
//
////        barbershops_list.removeAllViews();
//        barbershops_list.addView(barbershopView);
//    }

//    public void addBarbershop(String logo, String imageUrl, String name, double rating, String address, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes, String reason) {
//        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, barbershops_list, false);
//        ImageView logoImageView = barbershopView.findViewById(R.id.logo);
//
//        Glide.with(this)
//                .load(logo)
//                .into(logoImageView);
//
//        TextView nameTextView = barbershopView.findViewById(R.id.name);
//        TextView addressTextView = barbershopView.findViewById(R.id.address);
//
//        nameTextView.setText(name);
////        nameTextView.setText(reason);
//        addressTextView.setText(address);
//
//
//        barbershopView.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//
//            AdminSettingsActivity.imageUrl = imageUrl;
//            AdminSettingsActivity.name = name;
//            AdminSettingsActivity.logo = logo;
//            AdminSettingsActivity.rating = String.valueOf(rating);
//            AdminSettingsActivity.coordinates = coordinates;
//            AdminSettingsActivity.address = address;
//            AdminSettingsActivity.ListSpecialist = ListSpecialist;
//            AdminSettingsActivity.ListReviews = ListReviews;
//            AdminSettingsActivity.openingTimes = openingTimes;
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                    this,
//                    findViewById(R.id.bottom_navigation),
//                    "sharedImageTransition");
//            startActivity(intent, options.toBundle());
//        });
////        barbershops_list.removeAllViews();
//        barbershops_list.addView(barbershopView);
//    }


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
                            Log.i("ddd", String.valueOf(barber.getBarberId()));
                            Log.i("ddd", String.valueOf(barber.getBarberShopsId()));
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