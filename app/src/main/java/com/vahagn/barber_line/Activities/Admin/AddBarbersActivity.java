package com.vahagn.barber_line.Activities.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.vahagn.barber_line.Activities.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.model.TopHaircuts;

import java.util.ArrayList;
import java.util.List;

public class AddBarbersActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    ImageView BarberImage;

    private List<Services> Serviceslists = new ArrayList<>();
    private FrameLayout Save, AddService;
    private EditText BarberName, BarberPhoneNumber, ServiceName, ServicePrice, ServiceDuration;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barbers);

        View.OnTouchListener touchEffect = (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().alpha(0.8f).setDuration(50).start();
                    return false;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().alpha(1.0f).setDuration(50).start();
                    break;
            }
            return false;
        };


        BarberImage = findViewById(R.id.BarberImage);
        BarberName = findViewById(R.id.BarberName);
        BarberPhoneNumber = findViewById(R.id.BarberPhoneNumber);
        Save = findViewById(R.id.Save);

        ServiceName = findViewById(R.id.ServiceName);
        ServicePrice = findViewById(R.id.ServicePrice);
        ServiceDuration = findViewById(R.id.ServiceDuration);
        AddService = findViewById(R.id.AddService);

//        if (ListSpecialist != null) {
//            SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.info_container, specialistsFragment);
//            transaction.commit();
//        }

        Fragment servicesFragment = new ServicesFragment(Serviceslists);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, servicesFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Save.setOnTouchListener(touchEffect);
        AddService.setOnTouchListener(touchEffect);

        BarberImage.setOnClickListener(v -> openGallery());
        AddService.setOnClickListener(v -> AddService());
    }

    private void AddService() {
        String ServiceName_str = ServiceName.getText().toString().trim();
        String ServicePrice_str = ServicePrice.getText().toString().trim();
        String ServiceDuration_str = ServiceDuration.getText().toString().trim();

        Serviceslists.add(new Services(ServiceName_str, ServicePrice_str, ServiceDuration_str));

        Fragment servicesFragment = getSupportFragmentManager().findFragmentById(R.id.info_container);
        if (servicesFragment != null && servicesFragment instanceof ServicesFragment) {
            ((ServicesFragment) servicesFragment).updateServicesList(Serviceslists);
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.i("imageUri", String.valueOf(imageUri));
            String photoUrl = String.valueOf(imageUri);

            BarberImage.setImageURI(imageUri);
        }
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToCreateBarberShop(View view) {
        navigateTo(CreateBarberShopActivity.class);
    }

}