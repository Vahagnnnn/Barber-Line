package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

public class AdminActivity extends AppCompatActivity {

    public static boolean AdminActivity;
    String myBarbershopName, myWorkplaceName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        SpecialistsFragment.CanBook = false;

        FrameLayout createBarberShop = findViewById(R.id.createBarberShop);
        FrameLayout joinToBarberShop = findViewById(R.id.joinToBarberShop);

        ReadMyBarbershopName();
        ReadMyWorkplaceName();

        createBarberShop.setOnClickListener(view -> {
            if (myBarbershopName == null && myWorkplaceName == null) {
                navigateTo(CreateBarberShopActivity.class);
                AdminActivity = true;
            } else if (myBarbershopName != null)
                Toast.makeText(this, "You already have your own barbershop", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "You have already joined the barbershop.", Toast.LENGTH_SHORT).show();
        });
        joinToBarberShop.setOnClickListener(view ->
        {
            if (myBarbershopName == null && myWorkplaceName == null) {
                navigateTo(JoinToBarberShopActivity.class);
            } else if (myBarbershopName != null)
                Toast.makeText(this, "You already have your own barbershop", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "You have already joined the barbershop.", Toast.LENGTH_SHORT).show();
        });
    }


    private void ReadMyBarbershopName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myBarbershopName = dataSnapshot.child("myBarbershopName").getValue(String.class);
                    Log.d("ReadMyBarbershopName", "Barber Shop Name: " + myBarbershopName);
                } else
                    myBarbershopName = null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                myBarbershopName = null;
                Log.e("ReadMyBarbershopName", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void ReadMyWorkplaceName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myWorkplaceName = dataSnapshot.child("myWorkplaceName").getValue(String.class);
                    Log.d("ReadMyWorkplaceName", "Workplace Name: " + myWorkplaceName);
                } else
                    myWorkplaceName = null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                myWorkplaceName = null;
                Log.e("ReadMyWorkplaceName", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToSetting(View view) {
        if (myBarbershopName == null && myWorkplaceName == null) {
            Toast.makeText(this, "Create or join to barbershop", Toast.LENGTH_SHORT).show();
        } else if (myBarbershopName != null) {
            navigateTo(AdminSettingsActivity.class);
        } else {
            navigateTo(BarberProfileActivity.class);
        }
    }

    public void ToBooks(View view) {
        if (myBarbershopName == null) {
            Toast.makeText(this, "Create or join to barbershop", Toast.LENGTH_SHORT).show();
        } else
            navigateTo(AdminBooksActivity.class);
    }

}