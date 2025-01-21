package com.vahagn.barber_line;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vahagn.barber_line.adapter.TopBarberShopsAdapter;
import com.vahagn.barber_line.adapter.TopBarbersAdapter;
import com.vahagn.barber_line.model.TopBarberShops;
import com.vahagn.barber_line.model.TopBarbers;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView topbarbershopsRecycler,topbarbersRecycler;
    TopBarberShopsAdapter topbarbershopsAdapter;
    TopBarbersAdapter topbarbersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<TopBarberShops> TopBarberShopsList = new ArrayList<>();
        TopBarberShopsList.add(new TopBarberShops(R.drawable.paragon, "Paragon", "9 Ghazar Parpetsi St, 8", "Open from 08:30 to 23:30"));
        TopBarberShopsList.add(new TopBarberShops(R.drawable.paragon, "Paragon", "9 Ghazar Parpetsi St, 8", "Open from 08:30 to 23:30"));
        TopBarberShopsList.add(new TopBarberShops(R.drawable.paragon, "Paragon", "9 Ghazar Parpetsi St, 8", "Open from 08:30 to 23:30"));
        TopBarberShopsList.add(new TopBarberShops(R.drawable.paragon, "Paragon", "9 Ghazar Parpetsi St, 8", "Open from 08:30 to 23:30"));

        setTopBarberShopsRecycler(TopBarberShopsList);


        List<TopBarbers> TopBarbersList = new ArrayList<>();
        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));
        TopBarbersList.add(new TopBarbers(R.drawable.img_barber, "Sargis", "077-77-77-77"));

        setTopBarbersRecycler(TopBarbersList);
    }

    private void setTopBarbersRecycler(List<TopBarbers> topBarbersList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        topbarbersRecycler = findViewById(R.id.top_barbers_Recycler);
        topbarbersRecycler.setLayoutManager(layoutManager);

        topbarbersAdapter = new TopBarbersAdapter(this, topBarbersList);
        topbarbersRecycler.setAdapter(topbarbersAdapter);
    }

    private void setTopBarberShopsRecycler(List<TopBarberShops> topBarberShopsList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        topbarbershopsRecycler = findViewById(R.id.top_barber_shops_Recycler);
        topbarbershopsRecycler.setLayoutManager(layoutManager);

        topbarbershopsAdapter = new TopBarberShopsAdapter(this, topBarberShopsList);
        topbarbershopsRecycler.setAdapter(topbarbershopsAdapter);
    }
}