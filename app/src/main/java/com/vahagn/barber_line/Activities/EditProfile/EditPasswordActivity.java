package com.vahagn.barber_line.Activities.EditProfile;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.R;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private FirebaseUser user;
    EditText oldPasswordText, newPasswordText, confirmPasswordText;
    TextView save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        oldPasswordText = findViewById(R.id.OldPassword);
        newPasswordText = findViewById(R.id.NewPassword);
        confirmPasswordText = findViewById(R.id.ConfirmPassword);
        save = findViewById(R.id.save);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        user = mAuth.getCurrentUser();

        save.setOnClickListener(view -> updatePassword());
    }

    private void updatePassword() {
        String oldPassword = oldPasswordText.getText().toString().trim();
        String newPassword = newPasswordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty() || oldPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(EditPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            UpdateUserPasswordDatabase(newPassword);
                            navigateTo(EditProfileActivity.class);
                        } else {
                            Toast.makeText(EditPasswordActivity.this, "Failed to update password: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditPasswordActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Authentication",task.getException().getMessage());
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateUserPasswordDatabase(String newName) {
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = usersRef.child(userId);
            userRef.child("first_name").setValue(newName)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditPasswordActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                            navigateTo(EditProfileActivity.class);
                        } else {
                            Toast.makeText(EditPasswordActivity.this, "Failed to update name: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditPasswordActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
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