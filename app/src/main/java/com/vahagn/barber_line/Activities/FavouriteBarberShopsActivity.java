package com.vahagn.barber_line.Activities;

import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FavouriteBarberShopsActivity extends AppCompatActivity {

    public static LinearLayout secondActivityContainer;
    private List<BarberShops> ListBarberShops = new ArrayList<>();

    LinearLayout noFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_barber_shops);
        secondActivityContainer = findViewById(R.id.barbers_list);
        noFavorites = findViewById(R.id.noFavorites);

        ReadFavouritesBarberShops();

    }

    private void ReadFavouritesBarberShops() {
        if (!MainActivity.isLogin || MainActivity.userClass == null) {
            Toast.makeText(FavouriteBarberShopsActivity.this, "Please log in to view favorites", Toast.LENGTH_SHORT).show();
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
//                        userClassKeyId = String.valueOf(MainActivity.userClass.getFavouriteBarbershops().get(KeyId));
                        userClassKeyId = String.valueOf(MainActivity.userClass.getFavouriteBarbershops().contains(KeyId));
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
                    noFavorites.setVisibility(VISIBLE);
//                    Toast.makeText(FavouriteBarberShopsActivity.this, "No favorite barbershops", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
                Toast.makeText(FavouriteBarberShopsActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
            }
        });
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
            intent.putExtra("from_where", "Favourites");

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
                    findViewById(R.id.main),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        });


        container.addView(barbershopView);
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

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }
}