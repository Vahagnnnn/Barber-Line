package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.vahagn.barber_line.Activities.EditProfile.EditProfileActivity;
import com.vahagn.barber_line.R;

public class SettingsActivity extends AppCompatActivity {
    FrameLayout logout_button, remove_account;
    TextView Firstname_LastnameText, emailText, phoneNumberText;
    ImageView profileImageView;

    private DatabaseReference databaseReference;

    public String name, phone,photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        logout_button = findViewById(R.id.logout_button);
        remove_account = findViewById(R.id.remove_account);

        profileImageView = findViewById(R.id.profileImageView);
        Firstname_LastnameText = findViewById(R.id.Firstname_LastnameText);
        emailText = findViewById(R.id.email);
        phoneNumberText = findViewById(R.id.phoneNumberText);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("Firstname_LastnameText").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        phone = snapshot.child("phoneNumber").getValue(String.class);
                        photoUrl = snapshot.child("photoUrl").getValue(String.class);

                        assert phone != null;
                        phone = phone.substring(0, 4) + " " + phone.substring(4, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8);

                        Firstname_LastnameText.setText(name);
                        emailText.setText(email);
                        phoneNumberText.setText(phone);

                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(SettingsActivity.this)
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
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, EditProfileActivity.class);
        String[] parts = name.split(" ");
        intent.putExtra("first_name", parts[0]);
        intent.putExtra("last_name", parts[1]);
        intent.putExtra("phone", phone);
        intent.putExtra("photoUrl", photoUrl);
        startActivity(intent, options.toBundle());
    }

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }
}
