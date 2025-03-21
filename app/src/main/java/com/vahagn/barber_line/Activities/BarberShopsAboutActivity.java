package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vahagn.barber_line.Activities.Admin.AdminActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.CategoryAdapter;
import com.vahagn.barber_line.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BarberShopsAboutActivity extends AppCompatActivity {
    FrameLayout back_section;
    ImageView image;
    TextView name, rating, adress;

    CategoryAdapter categoryAdapter;
    RecyclerView categoryRecycler;
    List<Category> categoryList = new ArrayList<>();
    List<Barbers> ListSpecialist = new ArrayList<>();
    List<Services> ListService = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_shops_about);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        adress = findViewById(R.id.adress);
        back_section = findViewById(R.id.back_section);

        String from_where = getIntent().getStringExtra("from_where");
        String imageUrl = getIntent().getStringExtra("image");
        String nameText = getIntent().getStringExtra("name");
        String ratingText = getIntent().getStringExtra("rating");
        String addressText = getIntent().getStringExtra("address");

//        image.setImageResource(imageUrl);
        Glide.with(this)
                .load(imageUrl)
                .into(image);
        name.setText(nameText);
        rating.setText(ratingText);
        adress.setText(addressText);

        ListSpecialist = (List<Barbers>) getIntent().getSerializableExtra("ListSpecialist");
        ListService = (List<Services>) getIntent().getSerializableExtra("ListService");

        if (ListSpecialist != null) {
            SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.info_container, specialistsFragment);
            transaction.commit();
        }
        back_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(from_where, "BarbersActivity"))
                    ToBarbers(v);
                else if (Objects.equals(from_where, "MainActivity"))
                    ToHome(v);
                else if (Objects.equals(from_where, "MapActivity"))
                    ToMap(v);
                else
                    ToHome(v);
            }
        });
        categoryList.add(new Category(1, "Specialists", R.drawable.specialists, "#EDEFFB"));
        categoryList.add(new Category(2, "Services", R.drawable.scissors, "#242C3B"));
        categoryList.add(new Category(3, "Reviews", R.drawable.star_xml, "#242C3B"));
        setCategoryRecycler(categoryList);
    }

    private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.category);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList, ListSpecialist, ListService, getSupportFragmentManager());
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
    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
}