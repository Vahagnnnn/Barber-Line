package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.OpeningTime;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.CategoryAdapter;
import com.vahagn.barber_line.model.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BarberShopsAboutActivity extends AppCompatActivity {
    FrameLayout back_section;
    ImageView image;
    @SuppressLint("StaticFieldLeak")
    public static TextView name;
    TextView rating, address;

    CategoryAdapter categoryAdapter;
    RecyclerView categoryRecycler;
    List<Category> categoryList = new ArrayList<>();
    public static List<Barbers> ListSpecialist = new ArrayList<>();
    List<Services> ListService = new ArrayList<>();
    List<Reviews> ListReviews = new ArrayList<>();
    Map<String, TimeRange> openingTimes;

    ImageView heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_shops_about);
        CreateBarberShopActivity.isCreateBarberShopActivity = false;
        SpecialistActivity.SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;

        Log.i("ASAA", "BarberShopsAboutActivity " + SpecialistActivity.SpecialistActivity);
        Log.i("ASAA", "BarberShopsAboutActivity " + ServicesActivity.ServicesActivity);


        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        address = findViewById(R.id.address);
        back_section = findViewById(R.id.back_section);
        heart = findViewById(R.id.heart);

        String from_where = getIntent().getStringExtra("from_where");

        Glide.with(this)
                .load(BarbersActivity.imageUrl)
                .into(image);
        name.setText(BarbersActivity.name);
        rating.setText(BarbersActivity.rating);
        address.setText(BarbersActivity.address);


        if (MainActivity.userClass.getFavourite_Barbershops() != null) {
            try {
                if (String.valueOf(MainActivity.userClass.getFavourite_Barbershops().get(BarbersActivity.KeyId)).equals("true")) {
                    heart.setImageResource(R.drawable.img_heart_red);
                    heart.setTag(R.drawable.img_heart_red);
                }
                else
                {
                    heart.setImageResource(R.drawable.img_heart);
                    heart.setTag(R.drawable.img_heart);
                }
            } catch (Exception ignored) {
                heart.setImageResource(R.drawable.img_heart);
                heart.setTag(R.drawable.img_heart);
                Log.i("heartCheck", "catch");
            }
        } else {
            heart.setImageResource(R.drawable.img_heart);
            heart.setTag(R.drawable.img_heart);
        }
        ListSpecialist = BarbersActivity.ListSpecialist;
        ListReviews = BarbersActivity.ListReviews;
        openingTimes = BarbersActivity.openingTimes;


        if (ListSpecialist != null) {
            Set<String> existingServiceNames = new HashSet<>();

            for (Services service : ListService) {
                existingServiceNames.add(service.getName());
            }

            for (Barbers barbers : ListSpecialist) {
                if (barbers.getServices() != null) {
                    for (Services service : barbers.getServices()) {
                        if (!existingServiceNames.contains(service.getName())) {
                            ListService.add(service);
                            existingServiceNames.add(service.getName());
                        }
                    }
                }
            }
        }

//        ListService = BarbersActivity.ListService;

        if (ListSpecialist != null) {
            SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.info_container, specialistsFragment);
            transaction.commit();
        }
        back_section.setOnClickListener(v -> {
            if (Objects.equals(from_where, "BarbersActivity"))
                ToBarbers(v);
            else if (Objects.equals(from_where, "MainActivity"))
                ToHome(v);
            else if (Objects.equals(from_where, "MapActivity"))
                ToMap(v);
            else if (Objects.equals(from_where, "AppointmentsAboutActivity"))
                ToBooks(v);
            else
                ToHome(v);
        });
        categoryList.add(new Category(1, "Specialists", R.drawable.specialists, "#EDEFFB"));
        categoryList.add(new Category(2, "Services", R.drawable.scissors, "#242C3B"));
        categoryList.add(new Category(3, "Reviews", R.drawable.star_xml, "#242C3B"));
        categoryList.add(new Category(4, "About", R.drawable.ic_about, "#242C3B"));
        setCategoryRecycler(categoryList);
    }

    private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.category);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList, ListSpecialist, ListService, ListReviews, openingTimes, getSupportFragmentManager());
        categoryRecycler.setAdapter(categoryAdapter);
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
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

    public void AddToFavourites(View view) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userFavRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("Favourite_Barbershops");

        Object tag = heart.getTag();

        if (tag instanceof Integer) {
            int tagValue = (int) tag;
            if (tagValue == R.drawable.img_heart) {
                heart.setImageResource(R.drawable.img_heart_red);
                heart.setTag(R.drawable.img_heart_red);
                userFavRef.child(String.valueOf(BarbersActivity.KeyId)).setValue(true);
//                Log.i("getTag", "Tag: img_heart_red");
            } else if (tagValue == R.drawable.img_heart_red) {
                heart.setImageResource(R.drawable.img_heart);
                heart.setTag(R.drawable.img_heart);
                userFavRef.child(String.valueOf(BarbersActivity.KeyId)).removeValue();
//                Log.i("getTag", "Tag: img_heart");
            }
        } else {
            Log.i("getTag", "Tag is null or not an Integer");
        }


    }
}