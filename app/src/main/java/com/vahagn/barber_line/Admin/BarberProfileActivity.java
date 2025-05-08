package com.vahagn.barber_line.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BarberProfileActivity extends AppCompatActivity {
    public static boolean SpecialistActivity;

    ImageView profileImageView;
    TextView nameText, phoneNumberText, ratingText, reject_reason_Text;

    LinearLayout aboutLayout, wait_for_confirmation, rejected;
    ConstraintLayout settingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_profile);
        SpecialistActivity = true;

        ReadBarberID();

        FrameLayout joinToBarberShop = findViewById(R.id.joinToBarberShop);
        joinToBarberShop.setOnClickListener(view ->
        {
            if (AdminActivity.myBarbershopName == null && AdminActivity.myWorkplaceName == null) {
                navigateTo(JoinToBarberShopActivity.class);
            } else if (AdminActivity.myBarbershopName != null && !Objects.equals(AdminActivity.status, "rejected"))
                Toast.makeText(this, "You already have your own barbershop", Toast.LENGTH_SHORT).show();
            else if (AdminActivity.myWorkplaceName != null && Objects.equals(AdminActivity.status, "rejected"))
                navigateTo(JoinToBarberShopActivity.class);
            else
                Toast.makeText(this, "You have already joined the barbershop.", Toast.LENGTH_SHORT).show();
        });

    }

    public void RemoveBarberAccount(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to remove your account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser == null) {
                        return;
                    }

                    String uid = currentUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(uid);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("myWorkplaceName", null);
                    updates.put("myWorkplaceId", null);
                    updates.put("myIdAsBarber", null);
                    updates.put("status", null);

                    userRef.updateChildren(updates)
                            .addOnSuccessListener(aVoid -> Log.d("RemoveBarberAccount", "User fields cleared"))
                            .addOnFailureListener(e -> Log.e("RemoveBarberAccount", "Failed to update user fields", e));

                    String barberShopsId = String.valueOf(AdminActivity.barberShopsId);
                    String barberId = String.valueOf(AdminActivity.barberId);

                    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance()
                            .getReference("barberShops")
                            .child(barberShopsId)
                            .child("specialists")
                            .child(barberId);

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


    private void ReadBarberID() {
        profileImageView = findViewById(R.id.profileImageView);
        nameText = findViewById(R.id.nameText);
        phoneNumberText = findViewById(R.id.phoneNumberText);
        ratingText = findViewById(R.id.ratingText);
        reject_reason_Text = findViewById(R.id.reject_reason_Text);

        aboutLayout = findViewById(R.id.aboutLayout);
        settingLayout = findViewById(R.id.settingLayout);
        wait_for_confirmation = findViewById(R.id.wait_for_confirmation);
        rejected = findViewById(R.id.rejected);

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
                            Toast.makeText(BarberProfileActivity.this, "Insufficient data to load the profile", Toast.LENGTH_SHORT).show();
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
                        phoneNumberText.setText(phone);
                        ratingText.setText(barber.getRating() + "★");

                        String image = barber.getImage();
                        if (image != null && !image.isEmpty()) {
                            Glide.with(BarberProfileActivity.this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCorners(100))).into(profileImageView);
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

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.main), "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToAdminSettings(View view) {
        navigateTo(AdminSettingsActivity.class);
    }

    public void ToBooks(View view) {
        if (Objects.equals(AdminActivity.status, "pending")) {
            Toast.makeText(this, "Wait for confirmation", Toast.LENGTH_SHORT).show();
        } else if (Objects.equals(AdminActivity.status, "rejected")) {
            Toast.makeText(this, "Your request has been rejected, please join again.", Toast.LENGTH_SHORT).show();
        } else
            navigateTo(AdminBooksActivity.class);
    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }
}