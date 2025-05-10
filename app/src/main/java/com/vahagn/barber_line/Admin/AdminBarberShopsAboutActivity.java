package com.vahagn.barber_line.Admin;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.LoginActivity;
import com.vahagn.barber_line.Classes.Barbers;
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
import java.util.Set;

public class AdminBarberShopsAboutActivity extends AppCompatActivity {

    ImageView image;
    TextView name, rating, adress;

    CategoryAdapter categoryAdapter;
    RecyclerView categoryRecycler;
    List<Category> categoryList = new ArrayList<>();
    List<Barbers> ListSpecialist = new ArrayList<>();
    List<Services> ListService = new ArrayList<>();
    List<Reviews> ListReviews = new ArrayList<>();
    Map<String, TimeRange> openingTimes;
    String coordinates,nameMark,logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_barber_shops_about);
        CreateBarberShopActivity.isCreateBarberShopActivity = false;

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        adress = findViewById(R.id.adress);

//        String imageUrl = getIntent().getStringExtra("image");
//        String nameText = getIntent().getStringExtra("name");
//        String ratingText = getIntent().getStringExtra("rating");
//        String addressText = getIntent().getStringExtra("address");


        Glide.with(this)
                .load(AdminSettingsActivity.imageUrl)
                .into(image);
        name.setText(AdminSettingsActivity.name);
        rating.setText(AdminSettingsActivity.rating);
        adress.setText(AdminSettingsActivity.address);

        ListSpecialist = AdminSettingsActivity.ListSpecialist;
        ListReviews = AdminSettingsActivity.ListReviews;
        openingTimes = AdminSettingsActivity.openingTimes;
        coordinates = AdminSettingsActivity.coordinates;
        nameMark = AdminSettingsActivity.name;
        logo = AdminSettingsActivity.logo;

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



        if (ListSpecialist != null) {
            SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.info_container, specialistsFragment);
            transaction.commit();
        }

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

        categoryAdapter = new CategoryAdapter(this, categoryList, ListSpecialist, ListService, ListReviews, openingTimes, coordinates,nameMark,logo, getSupportFragmentManager(),true);
//        categoryAdapter = new CategoryAdapter(this, categoryList, ListSpecialist, ListService, getSupportFragmentManager());
        categoryRecycler.setAdapter(categoryAdapter);
    }


    public void ToSetting(View view) {
        navigateTo(
                "BarberProfileActivity".equals(getIntent().getStringExtra("from_where"))
                        ? BarberProfileActivity.class
                        : AdminSettingsActivity.class
        );

    }
    public void ToCreateBarberShop(View view) {
        navigateTo(CreateBarberShopActivity.class);
    }
    public void ToAdmin(View view) {
        if (isLogin)
            navigateTo(AdminActivity.class);
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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        ToSetting(null);
    }
}