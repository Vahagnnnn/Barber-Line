package com.vahagn.barber_line.Activities.EditProfile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class EditPhoneNumberActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    TextView save;
    EditText PhoneNumber;
    private final String prefix = "+374 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_number);

        PhoneNumber = findViewById(R.id.PhoneNumber);
        save = findViewById(R.id.save);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Set the prefix and phone number
        String phoneNumberFromInfoArr = EditProfileActivity.InfoArr.get("phoneNumber");
        PhoneNumber.setText(prefix + phoneNumberFromInfoArr.substring(4));

        // Ensure the prefix cannot be edited
        PhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || !s.toString().startsWith(prefix)) {
                    // If the prefix is removed or modified, reset it
                    PhoneNumber.setText(prefix);
                    PhoneNumber.setSelection(prefix.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set the cursor to the end of the prefix initially
        PhoneNumber.setSelection(prefix.length());

        save.setOnClickListener(view -> {
            String fullPhoneNumber = PhoneNumber.getText().toString();
            if (fullPhoneNumber.startsWith(prefix)) {
                String phoneNumberWithoutPrefix = fullPhoneNumber.substring(prefix.length());
                UpdateUserName(phoneNumberWithoutPrefix);
            } else {
                Toast.makeText(EditPhoneNumberActivity.this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateUserName(String PhoneNumber) {
        PhoneNumber = "+374" + PhoneNumber;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = usersRef.child(userId);

            userRef.child("phoneNumber").setValue(PhoneNumber)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditPhoneNumberActivity.this, "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                            navigateTo(EditProfileActivity.class);
                        } else {
                            Toast.makeText(EditPhoneNumberActivity.this, "Failed to update Phone number: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditPhoneNumberActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
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