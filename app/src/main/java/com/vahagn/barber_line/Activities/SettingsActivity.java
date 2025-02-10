package com.vahagn.barber_line.Activities;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vahagn.barber_line.R;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SettingsActivity extends AppCompatActivity {
    FrameLayout logout_button;
    TextView Firstname_LastnameText, emailText;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        logout_button = findViewById(R.id.logout_button);

        Firstname_LastnameText = findViewById(R.id.Firstname_LastnameText);
        emailText = findViewById(R.id.email);
        profileImageView = findViewById(R.id.profileImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        String name = sharedPreferences.getString("firstname_lastnameText", " ");
        String email = sharedPreferences.getString("email", " ");
        String photoUrl = sharedPreferences.getString("photoUrl", " ");

        Firstname_LastnameText.setText(name);
        emailText.setText(email);
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.img_avatar)
                    .error(R.drawable.img_avatar)
                    .into(profileImageView);
        } else {
            profileImageView.setImageResource(R.drawable.img_avatar);
        }

        logout_button.setOnClickListener(view -> logOut());
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(SettingsActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();
        MainActivity.isLogin = false;
        Firstname_LastnameText.setText(" ");
        emailText.setText(" ");
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }

}