package com.vahagn.barber_line.Activities.EditProfile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.R;

public class EditLastNameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    TextView save;
    EditText LastnameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_last_name);

        LastnameText = findViewById(R.id.LastnameText);
        save = findViewById(R.id.save);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        LastnameText.setText(EditProfileActivity.InfoArr.get("last_name"));

        save.setOnClickListener(view -> UpdateLastName(String.valueOf(LastnameText.getText())));
    }

    private void UpdateLastName(String LastName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = usersRef.child(userId);

            userRef.child("last_name").setValue(LastName)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditLastNameActivity.this, "Last name updated successfully", Toast.LENGTH_SHORT).show();
                            navigateTo(EditProfileActivity.class);
                        } else {
                            Toast.makeText(EditLastNameActivity.this, "Failed to update last name: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditLastNameActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    public void ToEdit(View view) {
        navigateTo(EditProfileActivity.class);
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