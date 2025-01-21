package com.vahagn.barber_line;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vahagn.barber_line.adapter.TopBarberShopsAdapter;
import com.vahagn.barber_line.model.TopBarberShops;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView topbarbershopsRecycler ;
    TopBarberShopsAdapter topbarbershopsAdapter;

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
    }

    private void setTopBarberShopsRecycler(List<TopBarberShops> topBarberShopsList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);

        topbarbershopsRecycler = findViewById(R.id.top_barber_shops_Recycler);
        topbarbershopsRecycler.setLayoutManager(layoutManager);

        topbarbershopsAdapter = new TopBarberShopsAdapter(this,topBarberShopsList);
        topbarbershopsRecycler.setAdapter(topbarbershopsAdapter);
    }
}