package com.vahagn.barber_line;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vahagn.barber_line.database.BarberLineDatabaseHelper;

import java.util.ArrayList;

public class BarbersActivity extends AppCompatActivity {

    BarberLineDatabaseHelper myDB;
    ArrayList<String> shop_id, shop_name, shop_address, shop_rating, shop_specialists, shop_services, shop_reviews, shop_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);

        myDB = new BarberLineDatabaseHelper(this);
        shop_id = new ArrayList<>();
        shop_name = new ArrayList<>();
        shop_address = new ArrayList<>();
        shop_rating = new ArrayList<>();
        shop_specialists = new ArrayList<>();
        shop_services = new ArrayList<>();
        shop_reviews = new ArrayList<>();
        shop_images = new ArrayList<>();
        storeDataInArrays();

    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                shop_id.add(cursor.getString(0));
                shop_name.add(cursor.getString(1));
                shop_address.add(cursor.getString(2));
                shop_rating.add(cursor.getString(3));
                shop_specialists.add(cursor.getString(4));
                shop_services.add(cursor.getString(5));
                shop_reviews.add(cursor.getString(6));
                shop_images.add(cursor.getString(7));
            }
        }
        cursor.close();
        ShowBarberShops();
    }

    private void addBarbershop(LinearLayout container, String titleText, String addressText, int imageResId) {
        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);

        TextView title = barbershopView.findViewById(R.id.title);
        TextView address = barbershopView.findViewById(R.id.address);
        ImageView logo = barbershopView.findViewById(R.id.logo);

        title.setText(titleText);
        address.setText(addressText);
        logo.setImageResource(imageResId);

        container.addView(barbershopView);
    }

    private void ShowBarberShops() {
//        myDB.addBarberShop("Paragon", "9 Ghazar Parpetsi St, 8, Yerevan", 5f, "John, Mike, Sargis", "Haircuts, Shaving", "Great service!", "sad", "sda");

        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);

        for (int i = 0; i < shop_name.size(); i++) {
            String name = shop_name.get(i);
            String address = shop_address.get(i);
            String imageName = shop_images.get(i);

            int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            //Log.i("imagePath", String.valueOf(resourceId));

            addBarbershop(secondActivityContainer, name, address, resourceId);
        }
        //addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        //addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
    }
    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }
    public void ToLogin(View view) {
        navigateTo(LoginActivity.class);
    }
    public void ToSettings(View view) {
        navigateTo(SettingsActivity.class);
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