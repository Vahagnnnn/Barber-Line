package com.vahagn.barber_line;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BarbersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);

        LinearLayout secondActivityContainer = findViewById(R.id.barbers_list);

        // Add barbershops dynamically
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
        addBarbershop(secondActivityContainer, "PARAGON", "9 Khazar Parpetsi St, 8", R.drawable.img_paragon_logo);
        addBarbershop(secondActivityContainer, "GENTS IMAGE CLUB", "Arabcir 27th Street 1/6 , Yerevan", R.drawable.img_gents_logo);
    }

    // Helper method to add a barbershop view with an image
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
}