package com.vahagn.barber_line.Activities;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.vahagn.barber_line.Admin.Applicant_BarberActivity;
import com.vahagn.barber_line.Admin.SelectBarberForSendRequestActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Users;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.TopBarberShopsAdapter;
import com.vahagn.barber_line.adapter.TopBarbersAdapter;
import com.vahagn.barber_line.adapter.TopHaircutsAdapter;
import com.vahagn.barber_line.model.TopHaircuts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public static boolean isLogin = false;

    RecyclerView topbarbershopsRecycler, topbarbersRecycler, tophaircutsRecycler;
    TopBarberShopsAdapter topbarbershopsAdapter;
    TopBarbersAdapter topbarbersAdapter;
    TopHaircutsAdapter tophaircutsAdapter;

    public static List<BarberShops> originalTopBarberShopsList = new ArrayList<>(),  TopBarberShopsList = new ArrayList<>(), filteredList = new ArrayList<>();
    List<Barbers> TopBarbersList = new ArrayList<>();
    List<TopHaircuts> TopHaircutsList = new ArrayList<>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("barberShops");
    DatabaseReference TopBarbers = FirebaseDatabase.getInstance().getReference().child("TopBarbers");
    DatabaseReference TopHaircuts = FirebaseDatabase.getInstance().getReference().child("TopHaircuts");

    private FirebaseAuth mAuth;
    public static Users userClass;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SelectBarberForSendRequestActivity.SelectBarberForSendRequestActivity = false;
        Applicant_BarberActivity.Is_Applicant_BarberActivity = false;
        SpecialistsFragment.CanBook = true;


//        FirebaseAuth.getInstance().signOut();
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        Toast.makeText(MainActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
//        MainActivity.isLogin = false;
//        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();

        SetTopBarberShops();
        SetTopBarbers();
        SetTopHaircuts();
        ReadUserInfo();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            MainActivity.isLogin = true;
        }


//
//        Barber_Line_Text = findViewById(R.id.Barber_Line_Text);
//        Barber_Line_Text.setOnClickListener(v -> showNotification());


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
        if (query == null || query.trim().isEmpty()) {
            TopBarberShopsList.clear();
            TopBarberShopsList.addAll(originalTopBarberShopsList);
            setTopBarberShopsRecycler(TopBarberShopsList);
            return;
        }

        filteredList.clear();
        for (BarberShops barbershop : originalTopBarberShopsList) {
            if (barbershop.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(barbershop);
            }
        }

        TopBarberShopsList.clear();
        TopBarberShopsList.addAll(filteredList);
        setTopBarberShopsRecycler(TopBarberShopsList);
    }


    private void ReadUserInfo() {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            String userId = user.getUid();
            UsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String first_name = snapshot.child("first_name").getValue(String.class);
                        String last_name = snapshot.child("last_name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String password = snapshot.child("password").getValue(String.class);
                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);
//                        List<Integer> Favourite_Barbershops = (List<Integer>) snapshot.child("Favourite_Barbershops").getValue();


                        // Handling Favourite_Barbershops data
                        List<Integer> Favourite_Barbershops = new ArrayList<>();
                        Object favShops = snapshot.child("Favourite_Barbershops").getValue();

                        if (favShops instanceof HashMap) {
                            Log.w("UserInfo", "HashMap");
                            HashMap<String, Boolean> map = (HashMap<String, Boolean>) favShops;
                            for (String key : map.keySet()) {
                                if (Boolean.TRUE.equals(map.get(key))) {
                                    try {
                                        int shopId = Integer.parseInt(key);
                                        Favourite_Barbershops.add(shopId);
                                    } catch (NumberFormatException e) {
                                        Log.w("UserInfo", "Invalid shop ID format: " + key, e);
                                    }
                                }
                            }
                        } else if (favShops instanceof List) {
                            Log.w("UserInfo", "List");
                            List<Boolean> favList = (List<Boolean>) favShops;
                            for (int i = 0; i < favList.size(); i++) {
                                if (favList.get(i) != null && favList.get(i)) {
                                    // Index i corresponds to the shop ID
                                    Favourite_Barbershops.add(i);
                                }
                            }
                        } else {
                            Log.w("UserInfo", "Unexpected data type for Favourite_Barbershops: " + favShops);
                        }


                        // Log the Favourite_Barbershops to see the values
                        Log.d("UserInfo", "Favourite Barbershops: " + Favourite_Barbershops);

                        userClass = new Users(first_name, last_name, email, password, phoneNumber, photoUrl, Favourite_Barbershops);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });
        }
    }

    private void SetTopBarberShops() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TopBarberShopsList.clear();
                originalTopBarberShopsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    assert shop != null;

                    if (shop.getStatus() == null || !shop.getStatus().equalsIgnoreCase("deleted")) {
                        BarberShops newShop = new BarberShops(shop.getBarberShopsId(), shop.getOwnerEmail(), shop.getName(), shop.getAddress(),
                                shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(),
                                shop.getServices(), shop.getSpecialists(), shop.getOpeningTimes());
                        TopBarberShopsList.add(newShop);
                        originalTopBarberShopsList.add(newShop);
                    }

                }
                setTopBarberShopsRecycler(TopBarberShopsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void SetTopBarbers() {
        TopBarbers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TopBarbersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Barbers barbers = snapshot.getValue(Barbers.class);
                    TopBarbersList.add(barbers);
                }
                setTopBarbersRecycler(TopBarbersList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void SetTopHaircuts() {
        TopHaircuts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TopHaircutsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TopHaircuts haircuts = snapshot.getValue(TopHaircuts.class);
                    assert haircuts != null;
                    TopHaircutsList.add(haircuts);
                }
                setTopHaircutsRecycler(TopHaircutsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }


    private void setTopBarberShopsRecycler(List<BarberShops> topBarberShopsList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        topbarbershopsRecycler = findViewById(R.id.top_barber_shops_Recycler);
        topbarbershopsRecycler.setLayoutManager(layoutManager);

        topbarbershopsAdapter = new TopBarberShopsAdapter(this, topBarberShopsList);
        topbarbershopsRecycler.setAdapter(topbarbershopsAdapter);
    }


    private void setTopBarbersRecycler(List<Barbers> topBarbersList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        topbarbersRecycler = findViewById(R.id.top_barbers_Recycler);
        topbarbersRecycler.setLayoutManager(layoutManager);

//        for (Barbers barbers : topBarbersList) {
//            Log.i("getBarberById", "topBarbersListBarberId " + barbers.getBarberId());
//            Log.i("getBarberById", "topBarbersListBarberShopsId " + barbers.getBarberShopsId());
//        }

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
        if (isLogin) {
//            Log.i("userClass", userClass.getFirst_name());
//            Log.i("userClass", userClass.getLast_name());
//            Log.i("userClass", userClass.getEmail());
//            Log.i("userClass", userClass.getPassword());

            if (userClass == null) {
                Toast.makeText(this, "Wait a second and try again", Toast.LENGTH_SHORT).show();
            } else
                navigateTo(SettingsActivity.class);
        } else {
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

    public void ToBooks(View view) {
        if (isLogin)
            navigateTo(BooksActivity.class);
        else
            navigateTo(LoginActivity.class);
    }
}