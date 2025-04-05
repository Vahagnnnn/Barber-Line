package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.AdminSettingsActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BarbersActivity extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("barberShops");
    LinearLayout secondActivityContainer;

    public static String imageUrl, name, rating, address, coordinates;
    public static List<Barbers> ListSpecialist = new ArrayList<>();
    public static List<Services> ListService = new ArrayList<>();

    private List<BarberShops> ListBarberShops = new ArrayList<>();
    private List<BarberShops> filteredList = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);
        secondActivityContainer = findViewById(R.id.barbers_list);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    assert shop != null;
                    addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getCoordinates(), shop.getSpecialists(), shop.getServices());
                    ListBarberShops.add(new BarberShops(shop.getName(), shop.getAddress(), shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(), shop.getServices(), shop.getSpecialists()));
//                    ListBarberShops.add(shop);
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
                // Not used here, but you can implement actions on submit if needed
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
        filteredList.clear();
        for (BarberShops barbershop : ListBarberShops) {
            if (barbershop.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(barbershop);
            }
        }
        secondActivityContainer.removeAllViews();
        for (BarberShops shop : filteredList) {
            addBarbershop(secondActivityContainer, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getCoordinates(), shop.getSpecialists(), shop.getServices());
        }
    }


    public void addBarbershop(LinearLayout container, String logo, String imageUrl, String name, double rating, String address, String coordinates, List<Barbers> ListSpecialist, List<Services> ListService) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);

        ImageView logoImageView = barbershopView.findViewById(R.id.logo);
//        int logoResId = getResources().getIdentifier(logo, "drawable", getPackageName());
//        logoImageView.setImageResource(logoResId);
//        Glide.with(this).load(logo).into(logoImageView);

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

            BarbersActivity.imageUrl = imageUrl;
            BarbersActivity.name = name;
            BarbersActivity.rating = String.valueOf(rating);
            BarbersActivity.address = address;

            if (coordinates != null) {
                Log.i("BarbershopCoordinates_str", "BarbersActivity = " + coordinates);
            } else {
                Log.i("BarbershopCoordinates_str", "BarbersActivity = " +"Message is null");
            }

            BarbersActivity.coordinates = coordinates;
            BarbersActivity.ListSpecialist = ListSpecialist;
//            BarbersActivity.ListService = ListService;

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });


        container.addView(barbershopView);
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }

    public void ToAdmin(View view) {
        if (isLogin)
            navigateTo(AdminActivity.class);
        else
            navigateTo(LoginActivity.class);
    }

    public void To(View view) {
        Log.i("isLogin", String.valueOf(isLogin));
        if (isLogin) {
            Log.i("isLogin", String.valueOf(isLogin));
            navigateTo(SettingsActivity.class);
        } else {
            Log.i("isLogin", String.valueOf(isLogin));
            navigateTo(LoginActivity.class);
        }

    }

    public void ToBooks(View view) {
        if (isLogin)
            navigateTo(BooksActivity.class);
        else
            navigateTo(LoginActivity.class);
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