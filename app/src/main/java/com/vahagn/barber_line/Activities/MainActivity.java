package com.vahagn.barber_line.Activities;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Firebase.BarberShopsDetail;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.TopBarberShopsAdapter;
import com.vahagn.barber_line.adapter.TopBarbersAdapter;
import com.vahagn.barber_line.adapter.TopHaircutsAdapter;
import com.vahagn.barber_line.Firebase.BarberShops;
import com.vahagn.barber_line.model.TopBarbers;
import com.vahagn.barber_line.model.TopHaircuts;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView topbarbershopsRecycler, topbarbersRecycler, tophaircutsRecycler;
    TopBarberShopsAdapter topbarbershopsAdapter;
    TopBarbersAdapter topbarbersAdapter;
    TopHaircutsAdapter tophaircutsAdapter;

    List<BarberShops> TopBarberShopsList = new ArrayList<>();
    List<TopBarbers> TopBarbersList = new ArrayList<>();
    List<TopHaircuts> TopHaircutsList = new ArrayList<>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("topBarberShops");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        myRef.push().setValue(new TopBarberShops(R.drawable.img_paragon, "Paragon", "9 Ghazar Parpetsi St, 8"));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TopBarberShopsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShopsDetail shop = snapshot.getValue(BarberShopsDetail.class);
                    int imageResId = getResources().getIdentifier(shop.getImage(), "drawable", getPackageName());
                    TopBarberShopsList.add(new BarberShops(imageResId, shop.getName(), shop.getAddress()));
//                    Log.d("Firebase", "Barber Shop: " + shop.getName() + ", Address: " + shop.getAddress()+ ", imageResId: " + imageResId);
                }
                setTopBarberShopsRecycler(TopBarberShopsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });


//        TopBarberShopsList.add(new TopBarberShops(R.drawable.img_paragon, "Paragon", "9 Ghazar Parpetsi St, 8"));
//        TopBarberShopsList.add(new TopBarberShops(R.drawable.img_paragon, "Paragon", "9 Ghazar Parpetsi St, 8"));
//        TopBarberShopsList.add(new TopBarberShops(R.drawable.img_paragon, "Paragon", "9 Ghazar Parpetsi St, 8"));
//        TopBarberShopsList.add(new TopBarberShops(R.drawable.img_paragon, "Paragon", "9 Ghazar Parpetsi St, 8"));
//        setTopBarberShopsRecycler(TopBarberShopsList);

        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));
        setTopBarbersRecycler(TopBarbersList);

        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        TopHaircutsList.add(new TopHaircuts(R.drawable.img_haircut, "The Textured Crop"));
        setTopHaircutsRecycler(TopHaircutsList);


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
        navigateTo(BarberShopsAboutActivity.class);
    }

    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }

    public void ToSettings(View view) {
        navigateTo(SettingsActivity.class);
    }


}