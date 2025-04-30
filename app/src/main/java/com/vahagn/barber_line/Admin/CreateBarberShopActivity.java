package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateBarberShopActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri BarberShopImageUri, BarberShopLogoUri;
    ImageView BarberShopImage, BarberShopLogo;

    public static List<Barbers> ListSpecialist = new ArrayList<>();
    private EditText BarberShopName, BarberShopAddress;
    private EditText Address;

    private DatabaseReference barberShopsRef;
    private boolean isLogoSelected = false;

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String address;

    public static boolean isCreateBarberShopActivity;


    private final String[] timeOptions = {
            "Closed",
            "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
            "12:00 PM", "12:30 PM", "01:00 PM", "01:30 PM",
            "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM",
            "04:00 PM", "04:30 PM", "05:00 PM", "05:30 PM",
            "06:00 PM", "06:30 PM", "07:00 PM", "07:30 PM",
            "08:00 PM", "08:30 PM", "09:00 PM"
    };
    private final List<String> daysOfWeek = Arrays.asList(
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"
    );
    private Map<String, TimeRange> openingTimes = new HashMap<>();


    String BarberShopName_INTENT;
    Uri BarberShopImage_getImageURI_INTENT, BarberShopImageUri_getdataUrl_INTENT;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_barber_shop);

        if (AdminActivity.AdminActivity)
            ListSpecialist.clear();
        else {
            BarberShopName.setText(BarberShopName_INTENT);
            BarberShopImage.setImageURI(BarberShopImage_getImageURI_INTENT);
            BarberShopImageUri = BarberShopImageUri_getdataUrl_INTENT;
        }
        AdminActivity.AdminActivity = false;
        isCreateBarberShopActivity = true;

        barberShopsRef = FirebaseDatabase.getInstance().getReference("pending_barbershops");

        FrameLayout Send_for_moderation = findViewById(R.id.Send_for_moderation);
        BarberShopImage = findViewById(R.id.BarberShopImage);
        BarberShopLogo = findViewById(R.id.BarberShopLogo);
        BarberShopName = findViewById(R.id.BarberShopName);
        Address = findViewById(R.id.Address);

        View mapOverlay = findViewById(R.id.map_overlay);
        mapOverlay.setOnClickListener(v -> navigateTo(AddLocationActivity.class));

//        ListSpecialist.add(new Barbers(R.drawable.img_sargis_paragon, "Sargis", "77777777"));
//        ListSpecialist.add(new Barbers(R.drawable.img_sargis_paragon, "Sargis", "77777777"));
//        ListSpecialist.add(new Barbers(R.drawable.img_sargis_paragon, "Sargis", "77777777"));

        if (ListSpecialist != null) {
            SpecialistsFragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.info_container, specialistsFragment);
            transaction.commit();
        }

        BarberShopImage.setOnClickListener(v -> {
            isLogoSelected = false;
            openGallery();
        });
        BarberShopLogo.setOnClickListener(v -> {
            isLogoSelected = true;
            openGallery();
        });

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        address = intent.getStringExtra("address");
        Address.setText(address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

//        setupDay("Monday");
//        setupDay("Tuesday");
//        setupDay("Wednesday");
//        setupDay("Thursday");
//        setupDay("Friday");
//        setupDay("Saturday");
//        setupDay("Sunday");

        for (String day : daysOfWeek) {
            setupDay(day);
        }

        Send_for_moderation.setOnClickListener(v -> Send_for_moderation());

    }


//    private void setupDay(String day) {
//        int startId = getResources().getIdentifier(day + "_start_spinner", "id", getPackageName());
//        int endId = getResources().getIdentifier(day + "_end_spinner", "id", getPackageName());
//
//        Spinner openSpinner = findViewById(startId);
//        Spinner closeSpinner = findViewById(endId);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        openSpinner.setAdapter(adapter);
//        closeSpinner.setAdapter(adapter);
//
//
//        openSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectedTime = (String) parentView.getItemAtPosition(position);
//                if ("Closed".equals(selectedTime)) {
//                    closeSpinner.setSelection(0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//        });
//
//        closeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String selectedTime = (String) parentView.getItemAtPosition(position);
//                if ("Closed".equals(selectedTime)) {
//                    openSpinner.setSelection(0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//        });
//
//    }

    private void setupDay(String day) {
        int startId = getResources().getIdentifier(day + "_start_spinner", "id", getPackageName());
        int endId = getResources().getIdentifier(day + "_end_spinner", "id", getPackageName());

        Spinner openSpinner = findViewById(startId);
        Spinner closeSpinner = findViewById(endId);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        openSpinner.setAdapter(adapter);
        closeSpinner.setAdapter(adapter);

        openSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTime = (String) parentView.getItemAtPosition(position);
                if ("Closed".equals(selectedTime)) {
                    closeSpinner.setSelection(0);
                }

//                // Update the opening time for the day
//                TimeRange timeRange = openingTimes.get(day);
//                if (timeRange == null) {
//                    Log.i("timeRange","getOpen = "+timeRange.getOpen()+"getClose = "+timeRange.getClose());
//                    timeRange = new TimeRange();
//                    openingTimes.put(day, timeRange);
//                }
//                timeRange.setOpen(selectedTime);

                else {
                    TimeRange timeRange = openingTimes.get(day);
                    if (timeRange == null) {
                        timeRange = new TimeRange();
                        openingTimes.put(day, timeRange);
                    }
//                    TimeRange timeRange = new TimeRange();
                    timeRange.setOpen(selectedTime);
                    Log.i("timeRange", "getOpen = " + timeRange.getOpen());
                    Log.i("timeRange", "getClose = " + timeRange.getClose());

                    if (!Objects.equals(timeRange.getClose(), "Closed")) {
                        Log.i("timeRange", "getOpen = " + timeRange.getOpen() + " getClose = " + timeRange.getClose());
                        openingTimes.put(day, timeRange);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        closeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTime = (String) parentView.getItemAtPosition(position);
                if ("Closed".equals(selectedTime)) {
                    openSpinner.setSelection(0);
                }

                // Update the closing time for the day
//                TimeRange timeRange = openingTimes.get(day);
//                if (timeRange == null) {
//                    Log.i("timeRange", "getOpen = " + timeRange.getOpen() + "getClose = " + timeRange.getClose());
//                    timeRange = new TimeRange();
//                    openingTimes.put(day, timeRange);
//                }
//                timeRange.setClose(selectedTime);

                else {
                    TimeRange timeRange = openingTimes.get(day);
                    if (timeRange == null) {
                        timeRange = new TimeRange();
                        openingTimes.put(day, timeRange);
                    }
                    timeRange.setClose(selectedTime);
                    Log.i("timeRange", "getClose = " + timeRange.getClose());
                    Log.i("timeRange", "getOpen = " + timeRange.getOpen());

                    if (!Objects.equals(timeRange.getOpen(), "Closed")) {
                        Log.i("timeRange", "getOpen = " + timeRange.getOpen() + " getClose = " + timeRange.getClose());
                        openingTimes.put(day, timeRange);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (latitude != 0.0 && longitude != 0.0) {
            LatLng barberLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(barberLocation).title(address));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barberLocation, 15f));
        } else {
            LatLng barberLocation = new LatLng(40.17762323599215, 44.51250583988117);
            mMap.addMarker(new MarkerOptions().position(barberLocation).title("Default Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barberLocation, 15f));
            Toast.makeText(this, "Default Location Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void Send_for_moderation() {
        String BarberShopName_str = BarberShopName.getText().toString().trim();
//        String BarberShopAddress_str = BarberShopAddress.getText().toString().trim();

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
        if (ListSpecialist.isEmpty()) {
            Toast.makeText(this, "The Specialists list is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Barbers barber : ListSpecialist) {
            barber.setWorkPlace(BarberShopName_str);
        }

        AddBarbersActivity.ListServices = new ArrayList<>(new LinkedHashSet<>(AddBarbersActivity.ListServices));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String ownerEmail = "";

        if (currentUser != null) {
            ownerEmail = currentUser.getEmail();
            Log.d("FirebaseAuth", "Current user email: " + ownerEmail);
        } else {
            Log.e("FirebaseAuth", "No user is signed in");
        }

        Log.d("FirebaseAuth", "Current user email: " + ownerEmail);
        String coordinates = latitude + " " + longitude;
//        BarberShops BarberShop = new BarberShops(ownerEmail, BarberShopName_str, address, coordinates, String.valueOf(BarberShopImageUri), String.valueOf(BarberShopLogoUri), "pending", AddBarbersActivity.ListServices, ListSpecialist);

        for (String day : daysOfWeek) {
            if (!openingTimes.containsKey(day)) {
                openingTimes.put(day, new TimeRange());
            }
        }

        BarberShops BarberShop = new BarberShops(ownerEmail, BarberShopName_str, address, coordinates, String.valueOf(BarberShopImageUri), String.valueOf(BarberShopLogoUri), "pending", ListSpecialist, openingTimes);
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

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap compressedBitmap = compressBitmap(bitmap, 100);

                String base64Image = bitmapToBase64(compressedBitmap);
                String dataUrl = "data:image/jpeg;base64," + base64Image;

                if (isLogoSelected) {
                    BarberShopLogo.setImageURI(imageUri);
                    BarberShopLogoUri = Uri.parse(dataUrl);
                } else {
                    BarberShopImage.setImageURI(imageUri);
                    BarberShopImageUri = Uri.parse(dataUrl);
                }

            } catch (IOException e) {
                Toast.makeText(this, "Error processing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ImageError", "Failed to process image", e);
            }
        }
    }

    private Bitmap compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
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

    public void ToAddBarber(View view) {
        navigateTo(AddBarbersActivity.class);
        BarberShopName_INTENT
    }

    public void ToSetting(View view) {
        navigateTo(AdminSettingsActivity.class);
    }

}