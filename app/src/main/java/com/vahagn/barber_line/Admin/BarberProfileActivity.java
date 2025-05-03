package com.vahagn.barber_line.Admin;

import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.List;

public class BarberProfileActivity extends AppCompatActivity {
    public static String imageUrl,name, rating;
    public static List<Services> ListServices = new ArrayList<>();

    TextView BarberNameTop, BarberName, BarberRating ,Confirmed_Rejected;
    ImageView BarberImage;
    public static boolean SpecialistActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_profile);

        SpecialistActivity = true;

        BarberNameTop = findViewById(R.id.BarberNameTop);
        BarberName = findViewById(R.id.BarberName);
        BarberImage = findViewById(R.id.BarberImage);
        BarberRating = findViewById(R.id.BarberRating);
        Confirmed_Rejected = findViewById(R.id.Confirmed_Rejected);

        BarberNameTop.setText(name);
        BarberName.setText(name);
        BarberRating.setText(rating);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(BarberProfileActivity.this)
                    .load(imageUrl)
                    .into(BarberImage);
        }


        ServicesFragment servicesFragment = new ServicesFragment(ListServices);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, servicesFragment);
        transaction.commit();


    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToAdminSettings(View view) {
        navigateTo(AdminSettingsActivity.class);
    }
}