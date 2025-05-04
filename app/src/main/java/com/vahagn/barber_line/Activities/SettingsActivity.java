package com.vahagn.barber_line.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.Applicant_BarberActivity;
import com.vahagn.barber_line.EditProfile.EditProfileActivity;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.SuperAdmin.SuperAdminModerationActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;

    FrameLayout edit_profile, logout_button, remove_account, admin;
    TextView Firstname_LastnameText, emailText, phoneNumberText;
    ImageView profileImageView;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Applicant_BarberActivity.Is_Applicant_BarberActivity = false;


        edit_profile = findViewById(R.id.edit_profile);
        logout_button = findViewById(R.id.logout_button);
        remove_account = findViewById(R.id.remove_account);
        admin = findViewById(R.id.admin);
        admin.setVisibility(View.GONE);

        profileImageView = findViewById(R.id.profileImageView);
        Firstname_LastnameText = findViewById(R.id.Firstname_LastnameText);
        emailText = findViewById(R.id.email);
        phoneNumberText = findViewById(R.id.phoneNumberText);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if (MainActivity.userClass != null) {
            Firstname_LastnameText.setText(MainActivity.userClass.getFirst_name() + " " + MainActivity.userClass.getLast_name());
            emailText.setText(MainActivity.userClass.getEmail());

            String phone = MainActivity.userClass.getPhoneNumber().substring(0, 4) + " " +
                    MainActivity.userClass.getPhoneNumber().substring(4, 6) + " " +
                    MainActivity.userClass.getPhoneNumber().substring(6, 8) + " " +
                    MainActivity.userClass.getPhoneNumber().substring(8);
            phoneNumberText.setText(phone);

            if (MainActivity.userClass.getPhotoUrl() != null && !MainActivity.userClass.getPhotoUrl().isEmpty()) {
                Glide.with(SettingsActivity.this)
                        .load(MainActivity.userClass.getPhotoUrl())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
                        .into(profileImageView);
            }

            if (Objects.equals(MainActivity.userClass.getEmail(), "vahagn.makaryan.v@gmail.com")) {
                admin.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(this, "User data not available. Please login again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }


//        Firstname_LastnameText.setText(MainActivity.userClass.getFirst_name() + " " + MainActivity.userClass.getLast_name());
//        emailText.setText(MainActivity.userClass.getEmail());
//        String  phone = MainActivity.userClass.getPhoneNumber().substring(0, 4) + " " + MainActivity.userClass.getPhoneNumber().substring(4, 6) + " " + MainActivity.userClass.getPhoneNumber().substring(6, 8) + " " + MainActivity.userClass.getPhoneNumber().substring(8);
//        phoneNumberText.setText(phone);
//
//        if (MainActivity.userClass.getPhotoUrl() != null && !MainActivity.userClass.getPhotoUrl().isEmpty()) {
//            Glide.with(SettingsActivity.this)
//                    .load(MainActivity.userClass.getPhotoUrl())
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
//                    .into(profileImageView);
//        }
//
//        if (Objects.equals(MainActivity.userClass.getEmail(), "vahagn.makaryan.v@gmail.com")){
//            admin.setVisibility(View.VISIBLE);
//        }


//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String userId = user.getUid();
//            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        String first_name = snapshot.child("first_name").getValue(String.class);
//                        String last_name = snapshot.child("last_name").getValue(String.class);
//                        String email = snapshot.child("email").getValue(String.class);
//                        String phone = snapshot.child("phoneNumber").getValue(String.class);
//                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);
//
//                        assert phone != null;
//                        phone = phone.substring(0, 4) + " " + phone.substring(4, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8);
//
//                        Firstname_LastnameText.setText(first_name + " " + last_name);
//                        emailText.setText(email);
//                        phoneNumberText.setText(phone);
//
//                        if (photoUrl != null && !photoUrl.isEmpty()) {
//                            Glide.with(SettingsActivity.this)
//                                    .load(photoUrl)
//                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
//                                    .into(profileImageView);
//                        }
//
//                        if (Objects.equals(email, "vahagn.makaryan.v@gmail.com")){
//                            admin.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    Log.w("Firebase", "Failed to read value.", error.toException());
//                }
//            });
//        }

//        View.OnTouchListener touchEffect = (v, event) -> {
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(Color.parseColor("#C4C1C1"));
//            drawable.setCornerRadius(15);
//            v.setBackground(drawable);
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    v.postDelayed(() -> {
//                        drawable.setColor(Color.parseColor("#9e9e9e"));
//                        v.setBackground(drawable);
//                    }, 0);
//                    return false;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    v.postDelayed(() -> {
//                        drawable.setColor(Color.parseColor("#C4C1C1"));
//                        v.setBackground(drawable);
//                    }, 0);
//                    break;
//            }
//            return false;
//        };

//        edit_profile.setOnTouchListener(touchEffect);
//        logout_button.setOnTouchListener(touchEffect);
//        remove_account.setOnTouchListener(touchEffect);
//        admin.setOnTouchListener(touchEffect);

        logout_button.setOnClickListener(view -> logOut());
        remove_account.setOnClickListener(view -> remove_account());
    }

    private void remove_account() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            databaseReference.child(user.getUid()).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.delete()
                                    .addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.apply();

                                            FirebaseAuth.getInstance().signOut();

                                            Toast.makeText(SettingsActivity.this, "Account has been removed.", Toast.LENGTH_SHORT).show();
                                            MainActivity.isLogin = false;
                                            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Failed to delete account.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Failed to remove account data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(SettingsActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
        MainActivity.isLogin = false;
        MainActivity.userClass = null;
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }

    public void ToEdit(View view) {
        navigateTo(EditProfileActivity.class);
    }

    public void ToBarberShopOwner(View view) {
        navigateTo(AdminActivity.class);
    }

    public void ToSuperAdminModerationActivity(View view) {
        navigateTo(SuperAdminModerationActivity.class);
    }

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }

    public void ToAboutTheDeveloper(View view) {
        navigateTo(AboutDeveloperActivity.class);
    }

    public void ToFavourites(View view) {
        navigateTo(FavouriteBarberShopsActivity.class);
    }

    public void ToNews(View view) {
        navigateTo(NewsActivity.class);
    }
    public void ToReport(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("name",MainActivity.userClass.getFirst_name());
        intent.putExtra("email",MainActivity.userClass.getEmail());
        startActivity(intent, options.toBundle());

    }
}
