package com.vahagn.barber_line.EditProfile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.R;

public class EditFirstNameActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    TextView save;
    EditText FirstnameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_first_name);

        FirstnameText = findViewById(R.id.FirstnameText);
        save = findViewById(R.id.save);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

//        FirstnameText.setText(EditProfileActivity.InfoArr.get("first_name"));
        FirstnameText.setText(MainActivity.userClass.getFirst_name());

        save.setOnClickListener(view -> UpdateUserFirstName(String.valueOf(FirstnameText.getText())));
    }

    private void UpdateUserFirstName(String newName) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = usersRef.child(userId);

            MainActivity.userClass.setFirst_name(newName);
            userRef.child("first_name").setValue(newName)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditFirstNameActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                            navigateTo(EditProfileActivity.class);
                        } else {
                            Toast.makeText(EditFirstNameActivity.this, "Failed to update name: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditFirstNameActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
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