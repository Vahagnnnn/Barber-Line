package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddBarbersActivity extends AppCompatActivity {
    private final String prefix = "+374 ";
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView BarberImage;

    public static List<Services>  ListServices = new ArrayList<>();
    private TextInputEditText BarberPhoneNumber;
    private EditText BarberName, ServiceName, ServicePrice, ServiceDuration;

    Uri imageUri;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barbers);

        ServicesFragment servicesFragment = new ServicesFragment(ListServices);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, servicesFragment);
        transaction.commit();

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
        FrameLayout save = findViewById(R.id.Save);
        ServiceName = findViewById(R.id.ServiceName);
        ServicePrice = findViewById(R.id.ServicePrice);
        ServiceDuration = findViewById(R.id.ServiceDuration);
        FrameLayout addService = findViewById(R.id.AddService);

        save.setOnTouchListener(touchEffect);
        addService.setOnTouchListener(touchEffect);

        BarberImage.setOnClickListener(v -> openGallery());
        addService.setOnClickListener(v -> AddService());
        save.setOnClickListener(v -> SaveBarber());

        BarberPhoneNumber.setText(prefix);
        BarberPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || !s.toString().startsWith(prefix)) {
                    BarberPhoneNumber.setText(prefix);
                    BarberPhoneNumber.setSelection(prefix.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        BarberPhoneNumber.setSelection(prefix.length());
    }

    private void AddService() {
        String ServiceName_str = ServiceName.getText().toString().trim();
        String ServicePrice_str = ServicePrice.getText().toString().trim();
        String ServiceDuration_str = ServiceDuration.getText().toString().trim();

        if (ServiceName_str.isEmpty() || ServicePrice_str.isEmpty() || ServiceDuration_str.isEmpty()) {
            Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_SHORT).show();
            return;
        }

        ListServices.add(new Services(ServiceName_str, ServicePrice_str, ServiceDuration_str));

        Fragment servicesFragment = getSupportFragmentManager().findFragmentById(R.id.info_container);
        if (servicesFragment instanceof ServicesFragment) {
            ((ServicesFragment) servicesFragment).updateServicesList(ListServices);
        }

        Log.i("imageUri", "imageUriFromAddService " + String.valueOf(BarberImage.getDrawable()));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void SaveBarber() {
        String BarberName_str = BarberName.getText().toString().trim();
        String BarberPhoneNumber_str = Objects.requireNonNull(BarberPhoneNumber.getText()).toString().trim();

        Log.i("imageUri", "imageUriFromSaveBarber " + String.valueOf( BarberImage.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.img_avatar).getConstantState())));
        if ( BarberImage.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.img_avatar).getConstantState())) {
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BarberName_str.isEmpty()) {
            Toast.makeText(this, "Please write Barber's name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BarberPhoneNumber_str.length() == 13)
            BarberPhoneNumber_str = BarberPhoneNumber_str.substring(5);
        else {
            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (BarberPhoneNumber_str.length() > 5)
//            BarberPhoneNumber_str = BarberPhoneNumber_str.substring(0, 5);
//        else {
//            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
//            return;
//        }
        BarberPhoneNumber_str = "+374" + BarberPhoneNumber_str;
        String phoneNumberPattern = "^\\+374[0-9]{8}$";
        Log.i("phone", BarberPhoneNumber_str);
        if (!BarberPhoneNumber_str.matches(phoneNumberPattern)) {
            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ListServices.isEmpty()) {
            Toast.makeText(this, "The Services list is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateBarberShopActivity.ListSpecialist.add(new Barbers(String.valueOf(imageUri), BarberName_str, BarberPhoneNumber_str,ListServices));
        Log.i("getImage", String.valueOf(imageUri));
        Toast.makeText(this, "The Barber has been added", Toast.LENGTH_SHORT).show();
        navigateTo(CreateBarberShopActivity.class);
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
//        navigateTo(CreateBarberShopActivity.class);
        onBackPressed();
    }

}