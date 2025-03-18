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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Activities.PhoneNumberActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateBarberShopActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri BarberShopImageUri, BarberShopLogoUri;
    ImageView BarberShopImage, BarberShopLogo;

    public static List<Barbers> ListSpecialist = new ArrayList<>();
    private EditText BarberShopName, BarberShopAddress;

    private DatabaseReference barberShopsRef;
    private boolean isLogoSelected = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_barber_shop);

        barberShopsRef = FirebaseDatabase.getInstance().getReference("pending_barbershops");

        FrameLayout Send_for_moderation = findViewById(R.id.Send_for_moderation);
        BarberShopImage = findViewById(R.id.BarberShopImage);
        BarberShopLogo = findViewById(R.id.BarberShopLogo);
        BarberShopName = findViewById(R.id.BarberShopName);
        BarberShopAddress = findViewById(R.id.BarberShopAddress);

//        ListSpecialist.add(new Barbers(R.drawable.img_sargis_paragon, "Sargis", "77777777"));
//        ListSpecialist.add(new Barbers(R.drawable.img_sargis_paragon, "Sargis", "77777777"));
//        ListSpecialist.add(new Barbers(R.drawable.img_sargis_paragon, "Sargis", "77777777"));

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

        View.OnTouchListener touchEffectForImages = (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().alpha(0.6f).setDuration(50).start();
                    return false;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().alpha(1.0f).setDuration(50).start();
                    break;
            }
            return false;
        };
        if (ListSpecialist != null) {
            SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.info_container, specialistsFragment);
            transaction.commit();
        }

        Send_for_moderation.setOnTouchListener(touchEffect);
        Send_for_moderation.setOnClickListener(v -> Send_for_moderation());

        BarberShopImage.setOnTouchListener(touchEffectForImages);
        BarberShopLogo.setOnTouchListener(touchEffectForImages);

        BarberShopImage.setOnClickListener(v -> {
            isLogoSelected = false;
            openGallery();
        });

        BarberShopLogo.setOnClickListener(v -> {
            isLogoSelected = true;
            openGallery();
        });
    }

    private void Send_for_moderation() {
        String BarberShopName_str = BarberShopName.getText().toString().trim();
        String BarberShopAddress_str = BarberShopAddress.getText().toString().trim();

        if (BarberShopImage.getDrawable().getConstantState().equals(getResources().getDrawable(android.R.drawable.ic_menu_gallery).getConstantState())) {
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (BarberShopLogo.getDrawable().getConstantState().equals(getResources().getDrawable(android.R.drawable.ic_menu_gallery).getConstantState())) {
            Toast.makeText(this, "Please upload the logo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (BarberShopName_str.isEmpty()) {
            Toast.makeText(this, "Please write Barber Shop's name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (BarberShopAddress_str.isEmpty()) {
            Toast.makeText(this, "Please write Barber Shop's address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ListSpecialist.isEmpty()) {
            Toast.makeText(this, "The Specialists list is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Barbers barber : ListSpecialist) {
            barber.setWorkPlace(BarberShopName_str);
        }

        AddBarbersActivity.ListServices = new ArrayList<>(new LinkedHashSet<>(AddBarbersActivity.ListServices));

        BarberShops BarberShop = new BarberShops(BarberShopName_str, BarberShopAddress_str, String.valueOf(BarberShopImageUri), String.valueOf(BarberShopLogoUri),"pending", AddBarbersActivity.ListServices, ListSpecialist);
        addBarberShop(BarberShop);
    }

    public void addBarberShop(BarberShops BarberShop) {
        barberShopsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long newId = task.getResult().getChildrenCount();
                barberShopsRef.child(String.valueOf(newId)).setValue(BarberShop)
                        .addOnCompleteListener(storeTask -> {
                            if (storeTask.isSuccessful()) {
                                Toast.makeText(CreateBarberShopActivity.this, "Store successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateBarberShopActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(CreateBarberShopActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(CreateBarberShopActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public void addBarberShop( BarberShops BarberShop) {
//        barberShopsRef.child(currentUser.getUid()).setValue(BarberShop)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(CreateBarberShopActivity.this, "Store successful", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(CreateBarberShopActivity.this, AdminActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(CreateBarberShopActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
//                    }
//                });

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> barberShop = new HashMap<>();
//        barberShop.put("name", name);
//        barberShop.put("owner", owner);
//        barberShop.put("location", location);
//        barberShop.put("phone", phone);
//        barberShop.put("status", "pending");
//
//        db.collection("pending_barbershops")
//                .add(barberShop)
//                .addOnSuccessListener(documentReference -> Log.d("Success", "Barber shop added for review"))
//                .addOnFailureListener(e -> Log.e("Error", "Failed to add barber shop", e));
//    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Log.i("imageUri", String.valueOf(imageUri));
            String photoUrl = String.valueOf(imageUri);

            if (isLogoSelected) {
                BarberShopLogo.setImageURI(imageUri);
                BarberShopLogoUri = imageUri;
            } else {
                BarberShopImage.setImageURI(imageUri);
                BarberShopImageUri = imageUri;
            }

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

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }

    public void ToAddLocation(View view) {
        navigateTo(AddLocationActivity.class);
    }

    public void ToAddBarber(View view) {
        navigateTo(AddBarbersActivity.class);
    }

    public void ToSetting(View view) {
        navigateTo(AdminActivity.class);
    }

}