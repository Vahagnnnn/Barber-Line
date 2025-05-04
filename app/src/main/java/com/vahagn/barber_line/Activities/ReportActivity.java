package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Admin.JoinToBarberShopActivity;
import com.vahagn.barber_line.Classes.Problem;
import com.vahagn.barber_line.R;

public class ReportActivity extends AppCompatActivity {

    EditText name, email, problem;
    FrameLayout sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        problem = findViewById(R.id.problem);
        sendButton = findViewById(R.id.sendButton);

        Intent intent = getIntent();
         name.setText( intent.getStringExtra("name"));
        email.setText( intent.getStringExtra("email"));


        sendButton.setOnClickListener(view -> sendProblem());


    }

    private void sendProblem() {
        String name_str = name.getText().toString().trim();
        String email_str = email.getText().toString().trim();
        String problem_str = problem.getText().toString().trim();

        if (name_str.isEmpty()) {
            name.setError("Type your name here");
            name.requestFocus();
            return;
        }

        if (email_str.isEmpty()) {
            email.setError("Type your email here");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if (problem_str.isEmpty()) {
            problem.setError("Please enter your message");
            problem.requestFocus();
            return;
        }

        DatabaseReference problemsRef = FirebaseDatabase.getInstance().getReference("problems");
        Problem problem = new Problem(name_str, email_str, problem_str);

        problemsRef.setValue(problem)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReportActivity.this, "Problem sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ReportActivity.this, "Failed to send problem", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToSettings(View view) {
        navigateTo(SettingsActivity.class);
    }
}