package com.vahagn.barber_line.Activities;

import static android.view.View.GONE;
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
import com.vahagn.barber_line.Admin.Applicant_BarberActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.OpeningTime;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.SuperAdmin.SuperAdminModerationActivity;
import com.vahagn.barber_line.adapter.CategoryAdapter;
import com.vahagn.barber_line.model.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BarberShopsAboutActivity extends AppCompatActivity {
    FrameLayout back_section, like_section;
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
    String coordinates, nameMark, logo;
    ImageView heart;

    public static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_shops_about);
        CreateBarberShopActivity.isCreateBarberShopActivity = false;
        SpecialistActivity.SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;
        Applicant_BarberActivity.Is_Applicant_BarberActivity = false;
        isAdmin = false;


        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        address = findViewById(R.id.address);
        back_section = findViewById(R.id.back_section);
        like_section = findViewById(R.id.like_section);
        heart = findViewById(R.id.heart);

        String from_where = getIntent().getStringExtra("from_where");
        if (Objects.equals(from_where, "SuperAdminModerationActivity")) {
            isAdmin = true;
            like_section.setVisibility(GONE);
        }
        Glide.with(this)
                .load(BarbersActivity.imageUrl)
                .into(image);
        name.setText(BarbersActivity.name);
        rating.setText(BarbersActivity.rating);
        address.setText(BarbersActivity.address);


//        if (MainActivity.userClass.getFavouriteBarbershops() != null) {
        if (MainActivity.userClass != null && MainActivity.userClass.getFavouriteBarbershops() != null) {
            try {
                Log.w("UserInfo", String.valueOf(BarbersActivity.KeyId));
                Log.w("UserInfo", String.valueOf(MainActivity.userClass.getFavouriteBarbershops().contains(BarbersActivity.KeyId)));
//                if (String.valueOf(MainActivity.userClass.getFavouriteBarbershops().get(BarbersActivity.KeyId)).equals("true")) {
                if (MainActivity.userClass.getFavouriteBarbershops().contains(BarbersActivity.KeyId)) {
                    heart.setImageResource(R.drawable.img_heart_red);
                    heart.setTag(R.drawable.img_heart_red);
                } else {
                    heart.setImageResource(R.drawable.img_heart);
                    heart.setTag(R.drawable.img_heart);
                }
            } catch (Exception ignored) {
                heart.setImageResource(R.drawable.img_heart);
                heart.setTag(R.drawable.img_heart);
                Log.i("UserInfo", "catch");
            }
        } else {
            heart.setImageResource(R.drawable.img_heart);
            heart.setTag(R.drawable.img_heart);
        }
        ListSpecialist = BarbersActivity.ListSpecialist;
        ListReviews = BarbersActivity.ListReviews;
        openingTimes = BarbersActivity.openingTimes;
        coordinates = BarbersActivity.coordinates;
        nameMark = BarbersActivity.name;
        logo = BarbersActivity.logo;


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
            else if (Objects.equals(from_where, "Favourites"))
                GoToFavourites(v);
            else if (Objects.equals(from_where, "SuperAdminModerationActivity"))
                navigateTo(SuperAdminModerationActivity.class);
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

        categoryAdapter = new CategoryAdapter(this, categoryList, ListSpecialist, ListService, ListReviews, openingTimes, coordinates, nameMark, logo, getSupportFragmentManager(), isAdmin);
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

//    public void AddToFavourites(View view) {
//        if (isLogin) {
//            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//            DatabaseReference userFavRef = FirebaseDatabase.getInstance()
//                    .getReference("Users")
//                    .child(userId)
//                    .child("Favourite_Barbershops");
//
//            Object tag = heart.getTag();
//
//            if (tag instanceof Integer) {
//                int tagValue = (int) tag;
//                if (tagValue == R.drawable.img_heart) {
//                    heart.setImageResource(R.drawable.img_heart_red);
//                    heart.setTag(R.drawable.img_heart_red);
//                    userFavRef.child(String.valueOf(BarbersActivity.KeyId)).setValue(true);
////                Log.i("getTag", "Tag: img_heart_red");
//                } else if (tagValue == R.drawable.img_heart_red) {
//                    Log.i("heartCheck", "heartCheckBarbers = " + BarbersActivity.KeyId);
//
//                    heart.setImageResource(R.drawable.img_heart);
//                    heart.setTag(R.drawable.img_heart);
//                    userFavRef.child(String.valueOf(BarbersActivity.KeyId)).removeValue();
//
//                    List<Integer> Favourite_Barbershops = MainActivity.userClass.getFavouriteBarbershops();
//                    Favourite_Barbershops.remove(BarbersActivity.KeyId);
//                    MainActivity.userClass.setFavouriteBarbershops(Favourite_Barbershops);
//
////                userClassKeyId = String.valueOf(MainActivity.userClass.getFavourite_Barbershops().get(BarbersActivity.KeyId));
////                BarbersActivity.ListBarberShops.clear();;
////                Log.i("getTag", "Tag: img_heart");
//                }
//            } else {
//                Log.i("getTag", "Tag is null or not an Integer");
//            }
//        } else
//            navigateTo(LoginActivity.class);
//    }


    public void AddToFavourites(View view) {
        if (!isLogin || FirebaseAuth.getInstance().getCurrentUser() == null) {
            navigateTo(LoginActivity.class);
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

                List<Integer> Favourite_Barbershops = MainActivity.userClass.getFavouriteBarbershops();

                Favourite_Barbershops.add(BarbersActivity.KeyId);
                Log.d("barbershopId", "Favourite Barbershops: " + Favourite_Barbershops);

                MainActivity.userClass.setFavouriteBarbershops(Favourite_Barbershops);
            } else if (tagValue == R.drawable.img_heart_red) {
                heart.setImageResource(R.drawable.img_heart);
                heart.setTag(R.drawable.img_heart);
                userFavRef.child(String.valueOf(BarbersActivity.KeyId)).removeValue();

                List<Integer> Favourite_Barbershops = MainActivity.userClass.getFavouriteBarbershops();



                Log.i("barbershopId", String.valueOf(BarbersActivity.KeyId));
                assert Favourite_Barbershops != null;
                Log.i("barbershopId", String.valueOf(Favourite_Barbershops.indexOf(BarbersActivity.KeyId)));

                Favourite_Barbershops.remove((Integer) BarbersActivity.KeyId);
                Log.d("barbershopId", "Favourite Barbershops: " + Favourite_Barbershops);

                MainActivity.userClass.setFavouriteBarbershops(Favourite_Barbershops);
            }
        } else {
            Log.i("getTag", "Tag is null or not an Integer");
        }
    }


    public void GoToFavourites(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, FavouriteBarberShopsActivity.class);
//        intent.putExtra("To", "Favourites");
//        intent.putExtra("from_where", "Favourites");
        startActivity(intent, options.toBundle());
    }
}