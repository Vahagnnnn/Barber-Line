package com.vahagn.barber_line.Activities.EditProfile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.R;

public class EditProfileActivity extends AppCompatActivity {
    TextView FirstnameText, LastnameText, phoneNumberText;
    ImageView profileImageView;

    public String first_name, last_name, phone, photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profileImageView);
        FirstnameText = findViewById(R.id.FirstnameText);
        LastnameText = findViewById(R.id.LastnameText);
        phoneNumberText = findViewById(R.id.phoneNumberText);

        Intent intent = getIntent();
        first_name = intent.getStringExtra("first_name");
        last_name = intent.getStringExtra("last_name");
        phone = intent.getStringExtra("phone");
        photoUrl = intent.getStringExtra("photoUrl");

        FirstnameText.setText(first_name);
        LastnameText.setText(last_name);
        phoneNumberText.setText(phone);

        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(EditProfileActivity.this)
                    .load(photoUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
                    .into(profileImageView);
        }

    }

    public void ToSetting(View view) {
        navigateTo(SettingsActivity.class);
    }

    public void ToEditName(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, EditNameActivity.class);
        intent.putExtra("first_name", first_name);
        startActivity(intent, options.toBundle());
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