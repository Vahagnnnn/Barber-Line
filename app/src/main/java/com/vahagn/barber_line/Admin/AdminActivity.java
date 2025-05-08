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

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    public static boolean AdminActivity;
    public static  String myBarbershopName, myWorkplaceName,status;
    public static Integer barberId,barberShopsId;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        SpecialistsFragment.CanBook = false;

        FrameLayout createBarberShop = findViewById(R.id.createBarberShop);
        FrameLayout joinToBarberShop = findViewById(R.id.joinToBarberShop);

        ReadMyBarbershopName_MyWorkplaceName_status_barberId_barberShopsId();
//        ReadMyBarbershopName();
//        ReadMyWorkplaceName();

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
            } else if (myBarbershopName != null && !Objects.equals(status, "rejected"))
                Toast.makeText(this, "You already have your own barbershop", Toast.LENGTH_SHORT).show();
            else if (myWorkplaceName != null && Objects.equals(status, "rejected"))
                navigateTo(JoinToBarberShopActivity.class);
            else
                Toast.makeText(this, "You have already joined the barbershop.", Toast.LENGTH_SHORT).show();
        });
    }


    private void ReadMyBarbershopName_MyWorkplaceName_status_barberId_barberShopsId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(currentUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myBarbershopName = dataSnapshot.child("myBarbershopName").getValue(String.class);
                    myWorkplaceName = dataSnapshot.child("myWorkplaceName").getValue(String.class);
                    status = dataSnapshot.child("status").getValue(String.class);
                    barberId = dataSnapshot.child("myIdAsBarber").getValue(Integer.class);
                    barberShopsId = dataSnapshot.child("myWorkplaceId").getValue(Integer.class);
                } else {
                    myBarbershopName = null;
                    myWorkplaceName = null;
                    status = null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                myBarbershopName = null;
                myWorkplaceName = null;
                status = null;
                Log.e("ReadMyBarbershopName", "Error: " + databaseError.getMessage());
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
        if (myBarbershopName == null &&  myWorkplaceName == null) {
            Toast.makeText(this, "Create or join to barbershop", Toast.LENGTH_SHORT).show();
        } else
            navigateTo(AdminBooksActivity.class);
    }
}