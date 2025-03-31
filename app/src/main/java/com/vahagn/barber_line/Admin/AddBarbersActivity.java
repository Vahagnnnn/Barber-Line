package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
public class AddBarbersActivity extends AppCompatActivity {
    private final String prefix = "+374 ";
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView BarberImage;

    public static List<Services> ListServices;
    private TextInputEditText BarberPhoneNumber,BarberPhoneNumberPrefix;
    private EditText BarberName, ServiceName, ServicePrice, ServiceDuration;

    Uri imageUri;

    public static String imageUrl, name, phoneNumber;
    public static List<Services> ListServiceEdit = new ArrayList<>();


    private CountryCodePicker countryCodePicker;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barbers);
        ListServices = new ArrayList<>();


        countryCodePicker = findViewById(R.id.countryCodePicker);
        BarberPhoneNumber = findViewById(R.id.countryCodeBarberPhoneNumber);




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
//        BarberPhoneNumber = findViewById(R.id.BarberPhoneNumber);
        BarberPhoneNumberPrefix = findViewById(R.id.BarberPhoneNumberPrefix);
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

        BarberPhoneNumberPrefix.setText(prefix);
        BarberPhoneNumberPrefix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || !s.toString().startsWith(prefix)) {
                    BarberPhoneNumberPrefix.setText(prefix);
                    BarberPhoneNumberPrefix.setSelection(prefix.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        BarberPhoneNumberPrefix.setSelection(prefix.length());

        AddEditInfo();

//        updatePhoneHint();

        // Listen for country code change and update hint accordingly
        countryCodePicker.setOnCountryChangeListener(() -> {
            String selectedCountryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
            int phoneNumberLength = getPhoneNumberLength(selectedCountryCode);

            TextInputLayout textInputLayout = findViewById(R.id.phoneInputLayout);
            textInputLayout.setHint(selectedCountryCode + " " + "X".repeat(phoneNumberLength));
        });


    }

    private void AddEditInfo() {

        if (imageUrl != null) {
            setImageFromBase64(imageUrl,BarberImage);
        }

        if (name != null) {
            BarberName.setText(name);
        }
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.substring(5);
            Log.i("ASA", "Phone Number: " + phoneNumber);
            BarberPhoneNumber.setText(phoneNumber);
            Log.i("ASA", "Phone: " + BarberPhoneNumber.getText());
//            BarberPhoneNumber.setSelection(phoneNumber.length());
        }
//        Log.i("ASA", "Name: " + name);
//        Log.i("ASA", "Phone Number: " + phoneNumber);
        Log.i("AddBarbersActivity", "Services: " + ListServiceEdit.toString());
//        Log.i("ASA", "Phone: " + BarberPhoneNumber.getText());
//        Log.i("ASA", "Image URL: " + imageUrl);
        if (ListServiceEdit != null) {
            ListServices.addAll(ListServiceEdit);
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.info_container);
            if (currentFragment instanceof ServicesFragment) {
                ((ServicesFragment) currentFragment).updateServicesList(ListServices);
            }
        }
    }



    public void setImageFromBase64(String base64String, ImageView imageView) {
        // Remove the prefix if it exists (for example, "data:image/jpeg;base64,")
        if (base64String.contains("base64,")) {
            base64String = base64String.split("base64,")[1];
        }

        // Decode the Base64 string into a byte array
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

        // Convert the byte array to a Bitmap
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // Set the decoded Bitmap to the ImageView
        imageView.setImageBitmap(decodedBitmap);
    }


    private void AddService() {
        String ServiceName_str = ServiceName.getText().toString().trim();
        String ServicePrice_str = ServicePrice.getText().toString().trim();
        String ServiceDuration_str = ServiceDuration.getText().toString().trim();

        if (ServiceName_str.isEmpty() || ServicePrice_str.isEmpty() || ServiceDuration_str.isEmpty()) {
            Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Services service : ListServices) {
            if (service.getName().equalsIgnoreCase(ServiceName_str)) {
                Toast.makeText(this, "A service with this name already exists", Toast.LENGTH_SHORT).show();
                return;
            }
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

        String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
        String BarberPhoneNumber_str = Objects.requireNonNull(BarberPhoneNumber.getText()).toString().trim();

        if (BarberPhoneNumber_str.isEmpty()) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        BarberPhoneNumber_str = countryCode + BarberPhoneNumber_str;

        if (!BarberPhoneNumber_str.matches("^\\+\\d{10,15}$")) {
            Toast.makeText(this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
            return;
        }


        Log.i("imageUri", "imageUriFromSaveBarber " + String.valueOf(BarberImage.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.img_avatar).getConstantState())));
        if (BarberImage.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.img_avatar).getConstantState())) {
            Toast.makeText(this, "Please upload the image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BarberName_str.isEmpty()) {
            Toast.makeText(this, "Please write Barber's name", Toast.LENGTH_SHORT).show();
            return;
        }


//        if (BarberPhoneNumber_str.length() > 5)
//            BarberPhoneNumber_str = BarberPhoneNumber_str.substring(0, 5);
//        else {
//            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        BarberPhoneNumber_str = "+374" + BarberPhoneNumber_str;
//        String phoneNumberPattern = "^\\+374[0-9]{8}$";
//        Log.i("phone", BarberPhoneNumber_str);
//        if (!BarberPhoneNumber_str.matches(phoneNumberPattern)) {
//            Toast.makeText(this, "Please enter a valid Armenian phone number", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (ListServices.isEmpty()) {
            Toast.makeText(this, "The Services list is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateBarberShopActivity.ListSpecialist.add(new Barbers(String.valueOf(imageUri), BarberName_str, BarberPhoneNumber_str, ListServices));
        Log.i("getImage", String.valueOf(imageUri));
        Toast.makeText(this, "The Barber has been added", Toast.LENGTH_SHORT).show();
        navigateTo(CreateBarberShopActivity.class);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            String photoUrl = String.valueOf(imageUri);
//
//            BarberImage.setImageURI(imageUri);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.i("imageUri", String.valueOf(imageUri));

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap compressedBitmap = compressBitmap(bitmap, 100);

                String base64Image = bitmapToBase64(compressedBitmap);
                String dataUrl = "data:image/jpeg;base64," + base64Image;

                BarberImage.setImageURI(imageUri);

                imageUri = Uri.parse(dataUrl);

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

    public void ToCreateBarberShop(View view) {
//        navigateTo(CreateBarberShopActivity.class);
        onBackPressed();
    }



    private void updatePhoneHint() {
        String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
        int phoneNumberLength = getPhoneNumberLength(countryCode);

        // Generate hint dynamically
        String hint = countryCode + " " + "X".repeat(phoneNumberLength);
        BarberPhoneNumber.setHint(hint);
    }

    // Function to get phone number length based on country
    private int getPhoneNumberLength(String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber exampleNumber = phoneUtil.getExampleNumberForType(
                    phoneUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode.replace("+", ""))),
                    PhoneNumberUtil.PhoneNumberType.MOBILE
            );

            if (exampleNumber != null) {
                // Get the length of the national significant number (excluding country code)
                return phoneUtil.getNationalSignificantNumber(exampleNumber).length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 10; // Default length if unknown
    }


}