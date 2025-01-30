package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BarberShopsAboutActivity extends AppCompatActivity {
    FrameLayout back_section;
    ImageView image;
    TextView name, rating, adress;
    LinearLayout specialistsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_shops_about);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        adress = findViewById(R.id.adress);
        back_section = findViewById(R.id.back_section);
        specialistsContainer = findViewById(R.id.specialists_container);

        String from_where = getIntent().getStringExtra("from_where");
        String imageText = getIntent().getStringExtra("image");
        String nameText = getIntent().getStringExtra("name");
        String ratingText = getIntent().getStringExtra("rating");
        String addressText = getIntent().getStringExtra("address");
        List<Barbers> specialists = (List<Barbers>) getIntent().getSerializableExtra("specialists");

        int imageResId = getResources().getIdentifier(imageText, "drawable", getPackageName());
        image.setImageResource(imageResId);
        name.setText(nameText);
        rating.setText(ratingText);
        adress.setText(addressText);

        if (specialists != null) {
            for (Barbers specialist : specialists) {
                addSpecialist(specialistsContainer, specialist);
                Log.i("addSpecialist","addSpecialist");
            }
        }

        Toast.makeText(this, from_where, Toast.LENGTH_SHORT).show();
        back_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(from_where, "BarbersActivity"))
                    ToBarbers(v);
                else
                    ToHome(v);
            }
        });
    }

    private void addSpecialist(LinearLayout container, Barbers specialist) {
        View specialistView = LayoutInflater.from(this).inflate(R.layout.specialists, container, false);

        ImageView specialistImage = specialistView.findViewById(R.id.image);
        TextView specialistName = specialistView.findViewById(R.id.name);
        TextView specialistRating = specialistView.findViewById(R.id.rating);

        int specialistImageResId = getResources().getIdentifier(specialist.getImage(), "drawable", getPackageName());
        specialistImage.setImageResource(specialistImageResId);
        specialistName.setText(specialist.getName());
        specialistRating.setText(String.valueOf(specialist.getRating()));

        container.addView(specialistView);
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