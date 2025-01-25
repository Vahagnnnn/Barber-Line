package com.vahagn.barber_line;


import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vahagn.barber_line.adapter.TopBarberShopsAdapter;
import com.vahagn.barber_line.adapter.TopBarbersAdapter;
import com.vahagn.barber_line.adapter.TopHaircutsAdapter;
import com.vahagn.barber_line.database.BarberLineDatabaseHelper;
import com.vahagn.barber_line.model.TopBarberShops;
import com.vahagn.barber_line.model.TopBarbers;
import com.vahagn.barber_line.model.TopHaircuts;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView topbarbershopsRecycler, topbarbersRecycler, tophaircutsRecycler;
    TopBarberShopsAdapter topbarbershopsAdapter;
    TopBarbersAdapter topbarbersAdapter;
    TopHaircutsAdapter tophaircutsAdapter;

    List<TopBarberShops> TopBarberShopsList = new ArrayList<>();
    List<TopBarbers> TopBarbersList = new ArrayList<>();
    List<TopHaircuts> TopHaircutsList = new ArrayList<>();

    BarberLineDatabaseHelper myDB;
    ArrayList<String> shop_name, shop_address, shop_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new BarberLineDatabaseHelper(this);
        shop_name = new ArrayList<>();
        shop_address = new ArrayList<>();
        shop_images = new ArrayList<>();

        storeDataInArrays();


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

    private void setTopBarberShopsRecycler(List<TopBarberShops> topBarberShopsList) {
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
    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                shop_name.add(cursor.getString(1));
                shop_address.add(cursor.getString(2));
                shop_images.add(cursor.getString(8));
            }
        }
        cursor.close();
        ShowBarberShops();
    }

    private void ShowBarberShops() {
        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);

        for (int i = 0; i < shop_name.size(); i++) {
            String name = shop_name.get(i);
            String address = shop_address.get(i);
            String imageName = shop_images.get(i);

            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            TopBarberShopsList.add(new TopBarberShops(resourceId, name, address));

        }
        setTopBarberShopsRecycler(TopBarberShopsList);

//        TopBarberShopsList.add(new TopBarberShops(R.drawable.img_paragon, "Paragon", "9 Ghazar Parpetsi St, 8"));
    }
}