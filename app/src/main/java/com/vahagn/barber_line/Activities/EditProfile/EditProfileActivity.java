package com.vahagn.barber_line.Activities.EditProfile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    public static HashMap<String, String> InfoArr = new HashMap<>();

    private DatabaseReference databaseReference;
    TextView FirstnameText, LastnameText, phoneNumberText;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profileImageView);
        FirstnameText = findViewById(R.id.FirstnameText);
        LastnameText = findViewById(R.id.LastnameText);
        phoneNumberText = findViewById(R.id.phoneNumberText);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String first_name = snapshot.child("first_name").getValue(String.class);
                        String last_name = snapshot.child("last_name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phoneNumber").getValue(String.class);
                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);

                        InfoArr.put("first_name", first_name);
                        InfoArr.put("last_name", last_name);
                        InfoArr.put("email", email);
                        InfoArr.put("phoneNumber", phone);
                        InfoArr.put("photoUrl", photoUrl);

                        assert phone != null;
                        phone = phone.substring(0, 4) + " " + phone.substring(4, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8);

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
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Firebase", "Failed to read value.", error.toException());
                }
            });
        }
    }

    public void ToSetting(View view) {
        navigateTo(SettingsActivity.class);
    }

    public void ToEditFirstName(View view) {
        navigateTo(EditFirstNameActivity.class);
    }

    public void ToEditLastName(View view) {
        navigateTo(EditLastNameActivity.class);
    }

    public void ToEditPhoneNumber(View view) {
        navigateTo(EditPhoneNumberActivity.class);
    }

    public void ToEditPassword(View view) {
        navigateTo(EditPasswordActivity.class);
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