package com.vahagn.barber_line.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
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
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

public class SpecialistActivity extends AppCompatActivity {
    TextView BarberNameTop,BarberName,BarberRating;
    ImageView BarberImage;

    public static boolean SpecialistActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist);

        SpecialistActivity = true;

        BarberNameTop = findViewById(R.id.BarberNameTop);
        BarberName = findViewById(R.id.BarberName);
        BarberImage = findViewById(R.id.BarberImage);
        BarberRating = findViewById(R.id.BarberRating);

        BarberNameTop.setText(SpecialistsFragment.name);
        BarberName.setText(SpecialistsFragment.name);
        BarberRating.setText(SpecialistsFragment.rating);

//        if (SpecialistsFragment.imageUrl != null) {
//            BarberImage.setImageResource();
//            setImageFromBase64(SpecialistsFragment.imageUrl, BarberImage);
//        }

//        if (SpecialistsFragment.imageUrl != null && !SpecialistsFragment.imageUrl.isEmpty()) {
//            Glide.with(SpecialistActivity.this)
//                    .load(SpecialistsFragment.imageUrl)
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
//                    .into(BarberImage);
//        }

        if (SpecialistsFragment.imageUrl != null && !SpecialistsFragment.imageUrl.isEmpty()) {
            Glide.with(SpecialistActivity.this)
                    .load(SpecialistsFragment.imageUrl)
                    .into(BarberImage);
        }

        ServicesFragment servicesFragment = new ServicesFragment(SpecialistsFragment.ListServices);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, servicesFragment);
        transaction.commit();
    }

//    public void setImageFromBase64(String base64String, ImageView imageView) {
//        if (base64String.contains("base64,")) {
//            base64String = base64String.split("base64,")[1];
//        }
//
//        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
//
//        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//        imageView.setImageBitmap(decodedBitmap);
//    }


    public void TobAboutBarberShop(View view) {
        SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;
        onBackPressed();
    }
}