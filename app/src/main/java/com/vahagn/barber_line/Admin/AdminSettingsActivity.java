package com.vahagn.barber_line.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
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
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminSettingsActivity extends AppCompatActivity {

    public static String imageUrl, logo, name, rating, address, OwnerEmail, coordinates;
    public static int barberShopsId;
    public static List<Barbers> ListSpecialist = new ArrayList<>();
    public static List<Reviews> ListReviews = new ArrayList<>();
    public static Map<String, TimeRange> openingTimes = new HashMap<>();

    TextView nameText, addressText, ratingText, reject_reason_Text;
    ImageView barbershop_logo;

    LinearLayout aboutLayout, wait_for_confirmation, rejected;
    ConstraintLayout settingLayout;

    public static List<Barbers> applicant_barber = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        ReadBarberID();

        FrameLayout createBarberShop = findViewById(R.id.createBarberShop);

        createBarberShop.setOnClickListener(view -> {
            if (AdminActivity.myBarbershopName == null && AdminActivity.myWorkplaceName == null) {
                navigateTo(CreateBarberShopActivity.class);
                AdminActivity.AdminActivity = true;
            } else if (AdminActivity.myBarbershopName != null && !Objects.equals(AdminActivity.status, "rejected"))
                Toast.makeText(this, "You already have your own barbershop", Toast.LENGTH_SHORT).show();
            else if (AdminActivity.myBarbershopName != null && Objects.equals(AdminActivity.status, "rejected"))
                navigateTo(CreateBarberShopActivity.class);
            else
                Toast.makeText(this, "You have already joined the barbershop.", Toast.LENGTH_SHORT).show();
        });
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

//            String uid = currentUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());


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
                        Integer myBarbershopId = dataSnapshot.child("myBarbershopId").getValue(Integer.class);

                        if (myBarbershopId != null) {
                            setInfo(myBarbershopId);

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
    }

    private void setInfo(Integer myBarbershopId) {
        DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops").child(String.valueOf(myBarbershopId));

        barberShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BarberShops barberShops = snapshot.getValue(BarberShops.class);
                if (barberShops != null) {
                    nameText.setText(barberShops.getName());
                    addressText.setText(barberShops.getAddress());
                    ratingText.setText(barberShops.getRating() + "★");


                    barberShopsId = barberShops.getBarberShopsId();
                    imageUrl = barberShops.getImage();
                    logo = barberShops.getLogo();
                    name = barberShops.getName();
                    rating = String.valueOf(barberShops.getRating());
                    address = barberShops.getAddress();
                    OwnerEmail = barberShops.getOwnerEmail();
                    coordinates = barberShops.getCoordinates();
                    ListSpecialist = barberShops.getSpecialists();
                    ListReviews = barberShops.getReviews();
                    openingTimes = barberShops.getOpeningTimes();

//                    ToBarbershopAbout(null, id, image, logo, name, rating, address, ownerEmail, coordinates, specialists, reviews, openingTimes);
//                    ToBarbershopAbout(null, barberShops.getBarberShopsId(), barberShops.getImage(), barberShops.getLogo(),  barberShops.getName(), barberShops.getRating(), barberShops.getAddress(), barberShops.getOwnerEmail(), barberShops.getCoordinates(), barberShops.getSpecialists(), barberShops.getReviews(), barberShops.getOpeningTimes());


                    String image = barberShops.getLogo();
                    if (image != null && !image.isEmpty()) {
                        Glide.with(AdminSettingsActivity.this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(100))).into(barbershop_logo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    //    public void ToBarbershopAbout(View view, int barberShopsId, String imageUrl, String logo,  String name, double rating, String address, String OwnerEmail, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes) {
    public void ToBarbershopAbout(View view) {
        Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
        intent.putExtra("from_where", "AdminSettingsActivity");

//        AdminSettingsActivity.barberShopsId = barberShopsId;
//        AdminSettingsActivity.imageUrl = imageUrl;
//        AdminSettingsActivity.logo = logo;
//        AdminSettingsActivity.name = name;
//        AdminSettingsActivity.rating = rating;
//        AdminSettingsActivity.address = address;
//        AdminSettingsActivity.OwnerEmail = OwnerEmail;
//        AdminSettingsActivity.coordinates = coordinates;
//        AdminSettingsActivity.ListSpecialist = ListSpecialist;
//        AdminSettingsActivity.ListReviews = ListReviews;
//        AdminSettingsActivity.openingTimes = openingTimes;

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        startActivity(intent, options.toBundle());
    }

    public void RemoveBarbershop(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to remove your barbershop?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser == null) {
                        return;
                    }

//                    String uid = currentUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(currentUser.getUid());

                    Map<String, Object> updates = new HashMap<>();


                    updates.put("myBarbershopName", null);//Vahagn Confirm
                    updates.put("myBarbershopId", null);//2
                    updates.put("status", null);

                    userRef.updateChildren(updates)
                            .addOnSuccessListener(aVoid -> Log.d("RemoveBarberAccount", "User fields cleared"))
                            .addOnFailureListener(e -> Log.e("RemoveBarberAccount", "Failed to update user fields", e));

                    String barberShopsId = String.valueOf(AdminActivity.myBarbershopId);

                    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance()
                            .getReference("barberShops")
                            .child(barberShopsId);

                    Map<String, Object> update_status = new HashMap<>();
                    update_status.put("status", "deleted");

                    barberShopsRef.updateChildren(update_status)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("RemoveBarberAccount", "Specialist status set to deleted");
                                navigateTo(AdminActivity.class);
                            })
                            .addOnFailureListener(e -> Log.e("RemoveBarberAccount", "Failed to update specialist status", e));
                })
                .setNegativeButton("Cancel", null)
                .show();

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
    public void ToHistory(View view) {
        navigateTo(HistoryActivity.class);
    }
    public void ToBooks(View view) {
        if (Objects.equals(AdminActivity.status, "pending")) {
            Toast.makeText(this, "Wait for confirmation", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(AdminActivity.status, "rejected")) {
            Toast.makeText(this, "Your request has been rejected, please join again.", Toast.LENGTH_SHORT).show();
        } else
            navigateTo(AdminBooksActivity.class);
    }


    public void ToApplicant_Barber(View view) {
        if (AdminActivity.myBarbershopName != null) {
            DatabaseReference applicant_barbersShopsRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");
            applicant_barber.clear();
            applicant_barbersShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean found = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Barbers barber = snapshot.getValue(Barbers.class);
                        if (barber != null && Objects.equals(barber.getWorkPlace(), AdminActivity.myBarbershopName)) {
                            Log.i("ddd", String.valueOf(barber.getBarberId()));
                            Log.i("ddd", String.valueOf(barber.getBarberShopsId()));
                            applicant_barber.add(barber);
                            found = true;
                            Log.i("workPlace", "IF " + AdminActivity.myBarbershopName);
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
}