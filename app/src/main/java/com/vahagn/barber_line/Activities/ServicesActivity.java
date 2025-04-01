package com.vahagn.barber_line.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {
    TextView ServiceNameTop,ServiceName,ServicePrice,ServiceDuration;
    public static List<Barbers> ListSpecialist= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        ServiceNameTop = findViewById(R.id.ServiceNameTop);
        ServiceName = findViewById(R.id.ServiceName);
        ServicePrice = findViewById(R.id.ServicePrice);
        ServiceDuration = findViewById(R.id.ServiceDuration);

        ServiceNameTop.setText(ServicesFragment.name);
        ServiceName.setText(ServicesFragment.name);
        ServicePrice.setText(ServicesFragment.price);
        ServiceDuration.setText(ServicesFragment.duration);

        List<Barbers> allBarbers = BarberShopsAboutActivity.ListSpecialist;
        ListSpecialist.clear();

        for (Barbers barber : allBarbers) {
            for (Services service : barber.getServices()) {
                if (service.getName().equals(ServicesFragment.name)) {
                    ListSpecialist.add(barber);
                    break;
                }
            }
        }
        SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, specialistsFragment);
        transaction.commit();
    }

    public void TobAboutBarberShop(View view) {
        onBackPressed();
    }
}