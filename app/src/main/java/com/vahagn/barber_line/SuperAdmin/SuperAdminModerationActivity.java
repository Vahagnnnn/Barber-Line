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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.List;

public class SuperAdminModerationActivity extends AppCompatActivity {

    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
    DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("pending_barbershops");
    DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_moderation);
        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);
        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    Log.i("getServices", shop.getServices().toString());
                    addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getSpecialists(), shop.getServices(), snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }


    public void addBarbershop(LinearLayout container, String logo, String image, String name, double rating, String address, List<Barbers> ListSpecialist, List<Services> ListService, String key) {
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
            intent.putExtra("from_where", "BarbersActivity");
            intent.putExtra("image", image);
            intent.putExtra("name", name);
            intent.putExtra("rating", String.valueOf(rating));
            intent.putExtra("address", address);
            intent.putExtra("ListSpecialist", (Serializable) ListSpecialist);
            intent.putExtra("ListService", (Serializable) ListService);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });


        barbershopView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm BarberShop")
                    .setMessage("Are you sure you want to confirm this barbershop?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Confirm(barbershopView,container, key);
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
                                        Reject(barbershopView,container, key,reason);

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


    private void Confirm(View barbershopView,LinearLayout container,String key) {
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
    private void Reject(View barbershopView,LinearLayout container,String key,String reason) {
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

//                        rejectedRef.child(String.valueOf(nextId)).setValue(shop)
//                                .addOnSuccessListener(aVoid -> {
//                                    pendingRef.child(key).removeValue()
//                                            .addOnSuccessListener(aVoid1 -> {
//                                                container.removeView(barbershopView);
//
//                                            });
//                                });

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
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }
}