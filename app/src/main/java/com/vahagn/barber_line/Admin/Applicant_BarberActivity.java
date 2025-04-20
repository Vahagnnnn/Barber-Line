package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.Objects;

public class Applicant_BarberActivity extends AppCompatActivity {

    public static boolean Is_Applicant_BarberActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_barber);
        Is_Applicant_BarberActivity = true;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment specialistsFragment = new SpecialistsFragment(AdminSettingsActivity.applicant_barber);
        transaction.replace(R.id.info_container, specialistsFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }
    public void ToBooks(View view) {
        navigateTo(AdminBooksActivity.class);
    }
    public void GoBack(View view) {
        Is_Applicant_BarberActivity = false;
        onBackPressed();
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
}