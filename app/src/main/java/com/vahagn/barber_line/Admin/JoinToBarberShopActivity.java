package com.vahagn.barber_line.Admin;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JoinToBarberShopActivity extends AppCompatActivity {

    public static boolean JoinToBarberShopActivity_REGISTER;
    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");

    private List<BarberShops> ListBarberShops = new ArrayList<>();
    public static String BarberWorkPlace, BarberEmail; //BarbershopName,
    public static int BarbershopKeyId;
    public static List<Barbers> ListSpecialistSendRequest = new ArrayList<>();

    private List<BarberShops> filtered_barbers_list = new ArrayList<>();
    LinearLayout barbers_list_Layout;

    private SearchView searchView;
    public static int BarberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_to_barber_shop);
        SelectBarberForSendRequestActivity.SelectBarberForSendRequestActivity = false;

        barbers_list_Layout = findViewById(R.id.barbers_list);

        barberShopsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    int barbershopKey = Integer.parseInt(Objects.requireNonNull(snapshot.getKey()));

                    List<Barbers> specialistsWithKeys = new ArrayList<>();

                    DataSnapshot specialistsSnapshot = snapshot.child("specialists");
                    for (DataSnapshot specialistSnap : specialistsSnapshot.getChildren()) {
                        Barbers specialist = specialistSnap.getValue(Barbers.class);
                        String specialistKey = specialistSnap.getKey();

                        if (specialist != null) {
                            assert specialistKey != null;
                            specialist.setBarberId(Integer.parseInt(specialistKey));
                            specialistsWithKeys.add(specialist);
                        }
                    }

                    if (shop != null) {
                        shop.setSpecialists(specialistsWithKeys);

                        addBarbershop(barbers_list_Layout, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getOwnerEmail(), shop.getCoordinates(), specialistsWithKeys, shop.getReviews(), shop.getOpeningTimes(), barbershopKey);

                        ListBarberShops.add(new BarberShops(
                                barbershopKey,
                                shop.getOwnerEmail(),
                                shop.getName(),
                                shop.getAddress(),
                                shop.getCoordinates(),
                                shop.getImage(),
                                shop.getLogo(),
                                shop.getRating(),
                                shop.getReviews(),
                                shop.getServices(),
                                specialistsWithKeys,
                                shop.getOpeningTimes()
                        ));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBarbershops(newText);
                return false;
            }
        });

    }


    private void filterBarbershops(String query) {
        filtered_barbers_list.clear();
        for (BarberShops barbershop : ListBarberShops) {
            if (barbershop.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered_barbers_list.add(barbershop);
            }
        }
        barbers_list_Layout.removeAllViews();
        for (BarberShops shop : filtered_barbers_list) {
            addBarbershop(barbers_list_Layout, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getOwnerEmail(), shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes(), shop.getKeyId());
        }
    }

    public void addBarbershop(LinearLayout container, String logo, String imageUrl, String name, double rating, String address, String OwnerEmail, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes, int KeyId) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Join To " + name);

            builder.setPositiveButton("Send Request", (dialog, which) -> {
                BarberWorkPlace = name;
                BarbershopKeyId = KeyId;
                ListSpecialistSendRequest = ListSpecialist;

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                assert currentUser != null;
                BarberEmail = currentUser.getEmail();

                navigateTo(SelectBarberForSendRequestActivity.class);
                Toast.makeText(this, "SelectBarberForSendRequestActivity", Toast.LENGTH_SHORT).show();

            });


            builder.setNeutralButton("Register", (dialog, which) -> {
                Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
                JoinToBarberShopActivity.JoinToBarberShopActivity_REGISTER = true;
                BarberWorkPlace = name;
//                BarbershopName = name;
                BarbershopKeyId = KeyId;

//                int maxBarberId = -1;

//                Log.d("MaxBarberId", "Size = " + ListSpecialist.size());
//
//                for (Barbers barber : ListSpecialist) {
//                    maxBarberId += 1;
//                    Log.d("MaxBarberId", String.valueOf(maxBarberId));
//                }

                BarberId = ListSpecialist.size();
                Log.d("MaxBarberId", "Last barberId is: " + BarberId);
                navigateTo(AddBarbersActivity.class);
            });


            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });


        container.addView(barbershopView);
    }


    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }

    public void ToSetting(View view) {
        navigateTo(AdminSettingsActivity.class);
    }

    public void ToBooks(View view) {
        navigateTo(AdminBooksActivity.class);
    }
}