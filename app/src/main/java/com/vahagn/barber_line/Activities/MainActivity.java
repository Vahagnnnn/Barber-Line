package com.vahagn.barber_line.Activities;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.TopBarberShopsAdapter;
import com.vahagn.barber_line.adapter.TopBarbersAdapter;
import com.vahagn.barber_line.adapter.TopHaircutsAdapter;
import com.vahagn.barber_line.model.TopBarbers;
import com.vahagn.barber_line.model.TopHaircuts;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static boolean isLogin = false;

    RecyclerView topbarbershopsRecycler, topbarbersRecycler, tophaircutsRecycler;
    TopBarberShopsAdapter topbarbershopsAdapter;
    TopBarbersAdapter topbarbersAdapter;
    TopHaircutsAdapter tophaircutsAdapter;

    public static List<BarberShops> TopBarberShopsList = new ArrayList<>();
    List<TopBarbers> TopBarbersList = new ArrayList<>();
    List<TopHaircuts> TopHaircutsList = new ArrayList<>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("barberShops");

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TopBarberShopsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    TopBarberShopsList.add(new BarberShops(shop.getName(), shop.getAddress(), shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(), shop.getServices(), shop.getSpecialists()));
                }
                setTopBarberShopsRecycler(TopBarberShopsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });


        TopBarbersList.add(new TopBarbers(R.drawable.img_sargis_paragon, "Sargis", "077-77-77-77"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_narine_paragon, "Narine", "099-99-99-99"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_narine_paragon, "Narine", "099-99-99-99"));
        setTopBarbersRecycler(TopBarbersList);

        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        setTopHaircutsRecycler(TopHaircutsList);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i("currentUser", "Log In " + isLogin);
            MainActivity.isLogin = true;
        } else {
            Log.i("currentUser", "Dont Log In " + isLogin);
        }

    }

    private void setTopBarberShopsRecycler(List<BarberShops> topBarberShopsList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        topbarbershopsRecycler = findViewById(R.id.top_barber_shops_Recycler);
        topbarbershopsRecycler.setLayoutManager(layoutManager);

        topbarbershopsAdapter = new TopBarberShopsAdapter(this, topBarberShopsList);
        topbarbershopsRecycler.setAdapter(topbarbershopsAdapter);
    }


    private void setTopBarbersRecycler(List<TopBarbers> topBarbersList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        topbarbersRecycler = findViewById(R.id.top_barbers_Recycler);
        topbarbersRecycler.setLayoutManager(layoutManager);

        topbarbersAdapter = new TopBarbersAdapter(this, topBarbersList);
        topbarbersRecycler.setAdapter(topbarbersAdapter);
    }

    private void setTopHaircutsRecycler(List<TopHaircuts> topHaircutsList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        tophaircutsRecycler = findViewById(R.id.top_haircuts_Recycler);
        tophaircutsRecycler.setLayoutManager(layoutManager);

        tophaircutsAdapter = new TopHaircutsAdapter(this, topHaircutsList);
        tophaircutsRecycler.setAdapter(tophaircutsAdapter);
    }


    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
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

    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }

    public void ToSetting(View view) {
        navigateTo(SettingsActivity.class);
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
}