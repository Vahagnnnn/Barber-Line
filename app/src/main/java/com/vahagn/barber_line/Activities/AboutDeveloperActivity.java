package com.vahagn.barber_line.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vahagn.barber_line.R;

public class AboutDeveloperActivity extends AppCompatActivity {

    TextView textGithub, textLinkedIn, textCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        textGithub = findViewById(R.id.textGithub);
        textLinkedIn = findViewById(R.id.textLinkedIn);
        textCV = findViewById(R.id.textCV);

        // Set click listeners
        textGithub.setOnClickListener(view -> openUrl("https://github.com/yourusername"));
        textLinkedIn.setOnClickListener(view -> openUrl("https://linkedin.com/in/yourprofile"));
        textCV.setOnClickListener(view -> openUrl("https://yourwebsite.com/yourcv.pdf"));
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}