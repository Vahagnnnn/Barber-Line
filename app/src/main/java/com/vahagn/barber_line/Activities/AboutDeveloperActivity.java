package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vahagn.barber_line.R;

public class AboutDeveloperActivity extends AppCompatActivity {

    TextView Github, LinkedIn;
Button btnViewCV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        Github = findViewById(R.id.Github);
        LinkedIn = findViewById(R.id.LinkedIn);
        btnViewCV = findViewById(R.id.btnViewCV);

        Github.setOnClickListener(view -> openUrl("https://github.com/Vahagnnnn"));
        LinkedIn.setOnClickListener(view -> openUrl("https://www.linkedin.com/in/vahagn-makaryan-0a87a5230/?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"));
        btnViewCV.setOnClickListener(view -> openUrl("https://www.canva.com/design/DAGcF41f1FQ/CuB1_DvAKf-dzYGCzktpZg/view?utm_content=DAGcF41f1FQ&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h4cdf07dfd3"));
    }

//    private void openUrl(String url) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }

//    private void openUrl(String url) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(intent);
//    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Нет приложения для открытия ссылки", Toast.LENGTH_SHORT).show();
        }
    }


    public void ToSettings(View view) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent, options.toBundle());
    }
}