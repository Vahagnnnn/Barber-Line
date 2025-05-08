package com.vahagn.barber_line.SuperAdmin;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SuperAdminModerationActivity extends AppCompatActivity {

    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
    DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("pending_barbershops");
    DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");

    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_moderation);
        SpecialistsFragment.CanBook = false;


        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        loadingProgressBar.setVisibility(View.VISIBLE);

        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                secondActivityContainer.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes(), snapshot.getKey());
                }
                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }


    public void addBarbershop(LinearLayout container, String logo, String imageUrl, String name, double rating, String address, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes, String key) {
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
            Intent intent = new Intent(this, BarberShopsAboutActivity.class);
            intent.putExtra("from_where", "SuperAdminModerationActivity");
//            intent.putExtra("image", image);
//            intent.putExtra("name", name);
//            intent.putExtra("rating", String.valueOf(rating));
//            intent.putExtra("address", address);
//            intent.putExtra("ListSpecialist", (Serializable) ListSpecialist);
//            intent.putExtra("ListService", (Serializable) ListService);

            BarbersActivity.imageUrl = imageUrl;
            BarbersActivity.logo = logo;
            BarbersActivity.name = name;
            BarbersActivity.rating = String.valueOf(rating);
            BarbersActivity.address = address;
            BarbersActivity.coordinates = coordinates;
            BarbersActivity.ListSpecialist = ListSpecialist;
            BarbersActivity.ListReviews = ListReviews;
            BarbersActivity.openingTimes = openingTimes;
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.main),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });


        barbershopView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm BarberShop")
                    .setMessage("Are you sure you want to confirm this barbershop?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Confirm(barbershopView, container, key);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        EditText input = new EditText(this);
                        input.setHint("Enter the reason for the refusal");

                        new AlertDialog.Builder(this)
                                .setTitle("Reason for refusal")
                                .setView(input)
                                .setPositiveButton("Send", (dialog1, which1) -> {
                                    String reason = input.getText().toString().trim();
                                    if (!reason.isEmpty()) {
                                        Reject(barbershopView, container, key, reason);

                                        // Можно сохранить причину в БД или логах
//                                        pendingRef.child(key).removeValue();
//                                        container.removeView(barbershopView);
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    })
                    .show();

            return true;
        });


        container.addView(barbershopView);
    }


    private void Confirm(View barbershopView, LinearLayout container, String key) {
        pendingRef.child(key).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                BarberShops shop = dataSnapshot.getValue(BarberShops.class);
                if (shop != null) {
                    barberShopsRef.get().addOnSuccessListener(snapshot -> {
                        long nextId = 0;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                long id = Long.parseLong(child.getKey());
                                if (id >= nextId) {
                                    nextId = id + 1;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }

                        shop.setBarberShopsId((int) nextId);
                        Log.i("newId", String.valueOf(shop.getBarberShopsId()));

                        barberShopsRef.child(String.valueOf(nextId)).setValue(shop)
                                .addOnSuccessListener(aVoid -> {
                                    pendingRef.child(key).removeValue()
                                            .addOnSuccessListener(aVoid1 -> {
                                                container.removeView(barbershopView);
                                            });
                                });
                    });
                }
            }
        });
    }

    private void Reject(View barbershopView, LinearLayout container, String key, String reason) {
        pendingRef.child(key).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                BarberShops shop = dataSnapshot.getValue(BarberShops.class);
                if (shop != null) {
                    rejectedRef.get().addOnSuccessListener(snapshot -> {
                        long nextId = 0;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            try {
                                long id = Long.parseLong(child.getKey());
                                if (id >= nextId) {
                                    nextId = id + 1;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }

                        long finalNextId = nextId;
                        rejectedRef.child(String.valueOf(nextId)).setValue(shop)
                                .addOnSuccessListener(aVoid -> {
                                    rejectedRef.child(String.valueOf(finalNextId)).child("reason").setValue(reason);
                                    pendingRef.child(key).removeValue()
                                            .addOnSuccessListener(aVoid1 -> {
                                                container.removeView(barbershopView);
                                            });
                                });
                    });
                }
            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToSettings(View view) {
        navigateTo(SettingsActivity.class);
    }
}