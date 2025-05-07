package com.vahagn.barber_line.Admin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

public class SelectBarberForSendRequestActivity extends AppCompatActivity {

    TextView BarbershopName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_barber_for_send_request);


        BarbershopName = findViewById(R.id.BarbershopName);
        BarbershopName.setText(JoinToBarberShopActivity.BarbershopName);

        SpecialistsFragment specialistsFragment = new SpecialistsFragment(JoinToBarberShopActivity.ListSpecialistSendRequest);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, specialistsFragment);
        transaction.commit();

    }


    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToJoinToBarberShop(View view) {
        navigateTo(JoinToBarberShopActivity.class);
    }
}