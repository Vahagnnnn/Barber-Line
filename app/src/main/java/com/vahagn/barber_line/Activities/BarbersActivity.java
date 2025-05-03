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
import android.widget.Toast;

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

public class BarbersActivity extends AppCompatActivity {

    public static LinearLayout secondActivityContainer;

    public static Object ImageResource, tag;
    public static int KeyId;
    public static String logo, imageUrl, name, rating, OwnerEmail, address, coordinates;
    public static List<Barbers> ListSpecialist = new ArrayList<>();
    public static List<Reviews> ListReviews = new ArrayList<>();
    public static Map<String, TimeRange> openingTimes = new HashMap<>();

    private List<BarberShops> ListBarberShops = new ArrayList<>();
    private List<BarberShops> filteredList = new ArrayList<>();
    private SearchView searchView;

    String from_where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);
        SpecialistsFragment.CanBook = true;
        secondActivityContainer = findViewById(R.id.barbers_list);

        String To = getIntent().getStringExtra("To");

        if ("Favourites".equals(To)) {
//            ReadBarberShops();

//            ListBarberShops.clear();
//            secondActivityContainer.removeAllViews();

            Log.i("from_where", "ReadFavouritesBarberShops");
            ReadFavouritesBarberShops();
        } else
            ReadBarberShops();

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

        from_where = getIntent().getStringExtra("from_where");
        Log.i("from_where", "from_where Barbers = " + from_where);

    }

    private void ReadBarberShops() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("barberShops");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int KeyId = Integer.parseInt(Objects.requireNonNull(snapshot.getKey()));

                    BarberShops shop = snapshot.getValue(BarberShops.class);
                    assert shop != null;

                    addBarbershop(secondActivityContainer, KeyId, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getOwnerEmail(), shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
                    ListBarberShops.add(new BarberShops(shop.getOwnerEmail(), shop.getName(), shop.getAddress(), shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(), shop.getServices(), shop.getSpecialists(), shop.getOpeningTimes()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });

    }
//    private void ReadFavouritesBarberShops(){
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("barberShops");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    int KeyId = Integer.parseInt(Objects.requireNonNull(snapshot.getKey()));
//
//                    if (String.valueOf(MainActivity.userClass.getFavourite_Barbershops().get(KeyId)).equals("true")) {
//
//                        BarberShops shop = snapshot.getValue(BarberShops.class);
//                        assert shop != null;
//
//                        addBarbershop(secondActivityContainer, KeyId ,shop.getLogo(),  shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getOwnerEmail(), shop.getCoordinates(), shop.getSpecialists(),shop.getReviews(), shop.getOpeningTimes());
//                        ListBarberShops.add(new BarberShops(shop.getOwnerEmail(), shop.getName(), shop.getAddress(), shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(), shop.getServices(), shop.getSpecialists(), shop.getOpeningTimes()));
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//            }
//        });
//
//    }

    private void ReadFavouritesBarberShops() {
        if (!MainActivity.isLogin || MainActivity.userClass == null) {
            Toast.makeText(BarbersActivity.this, "Please log in to view favorites", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("barberShops");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListBarberShops.clear();
                secondActivityContainer.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key == null) {
                        Log.w("BarbersActivity", "Invalid snapshot key");
                        continue;
                    }
                    int KeyId = Integer.parseInt(key);

                    String userClassKeyId;
                    try {
                        userClassKeyId = String.valueOf(MainActivity.userClass.getFavouriteBarbershops().get(KeyId));
                    } catch (Exception e) {
                        userClassKeyId = "false";
                    }

                    if (userClassKeyId.equals("true")) {

                        BarberShops shop = snapshot.getValue(BarberShops.class);
                        assert shop != null;

                        addBarbershop(secondActivityContainer, KeyId, shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getOwnerEmail(), shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
                        ListBarberShops.add(new BarberShops(shop.getOwnerEmail(), shop.getName(), shop.getAddress(), shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(), shop.getServices(), shop.getSpecialists(), shop.getOpeningTimes()));
                    }
                }
                if (ListBarberShops.isEmpty()) {
                    Toast.makeText(BarbersActivity.this, "No favorite barbershops", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
                Toast.makeText(BarbersActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
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
            addBarbershop(secondActivityContainer, shop.getKeyId(), shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getOwnerEmail(), shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
        }
    }

    public void addBarbershop(LinearLayout container, int KeyId, String logo, String imageUrl, String name, double rating, String address, String OwnerEmail, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes) {
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

            if (from_where != null) {
                if (Objects.equals(from_where.trim(), "Favourites")) {
                    Log.i("from_where", "from_where IF = " + from_where);
                    intent.putExtra("from_where", "Favourites");
                }
            } else {
                Log.i("from_where", "from_where Else = " + from_where);
                intent.putExtra("from_where", "BarbersActivity");
            }


            Log.i("heartCheck", "heartCheckBarbers = " + KeyId);
            BarbersActivity.KeyId = KeyId;
            BarbersActivity.imageUrl = imageUrl;
            BarbersActivity.logo = logo;
            BarbersActivity.name = name;
            BarbersActivity.rating = String.valueOf(rating);
            BarbersActivity.OwnerEmail = OwnerEmail;
            BarbersActivity.address = address;
            BarbersActivity.coordinates = coordinates;
            BarbersActivity.ListSpecialist = ListSpecialist;
            BarbersActivity.ListReviews = ListReviews;
            BarbersActivity.openingTimes = openingTimes;

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