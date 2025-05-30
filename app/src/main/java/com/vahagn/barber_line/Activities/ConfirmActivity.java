package com.vahagn.barber_line.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

public class ConfirmActivity extends AppCompatActivity {
    EditText message_or_requests;
    ImageView BarberShopImage, BarberImage;
    TextView BarberShopName, BarberShopAddress, weekDay_monthName_dayOfMonth, Time, BarberName, BarberRating, ServiceName, ServiceDuration, ServicePrice;

    String BarberShopImageUrl_str, BarberShopName_str, BarberShopAddress_str, BarbershopCoordinates_str, BarberShopRating_str, BarberShopOwnerEmail_str, weekDay_monthName_dayOfMonth_str, Time_str,
            BarberImageUrl_str, BarberName_str, BarberRating_str, ServiceName_str, ServicePrice_str, ServiceDuration_str;

    Button confirm_button;

    private DatabaseReference appointmentsRef;

    int BarberId;
    int BarberShopsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Intent intent = getIntent();
        BarberShopImageUrl_str = BarbersActivity.imageUrl;
        BarberShopName_str = BarbersActivity.name;
        BarberShopAddress_str = BarbersActivity.address;
        BarbershopCoordinates_str = BarbersActivity.coordinates;
        BarberShopRating_str = BarbersActivity.rating;
        BarberShopOwnerEmail_str = BarbersActivity.OwnerEmail;

        weekDay_monthName_dayOfMonth_str = intent.getStringExtra("weekDay_monthName_dayOfMonth");
        Time_str = intent.getStringExtra("Time");

        BarberImageUrl_str = SpecialistsFragment.imageUrl;
        BarberName_str = SpecialistsFragment.name;
        BarberRating_str = String.valueOf(SpecialistsFragment.rating);
        ServiceName_str = ServicesFragment.name;
        ServicePrice_str = ServicesFragment.price;
        ServiceDuration_str = ServicesFragment.duration;

        BarberId = BarbersActivity.barberId;
        BarberShopsId = BarbersActivity.barberShopsId;

        message_or_requests = findViewById(R.id.message_or_requests);
        BarberShopImage = findViewById(R.id.BarberShopImage);
        BarberShopName = findViewById(R.id.BarberShopName);
        BarberShopAddress = findViewById(R.id.BarberShopAddress);
        weekDay_monthName_dayOfMonth = findViewById(R.id.weekDay_monthName_dayOfMonth);
        Time = findViewById(R.id.Time);
        BarberImage = findViewById(R.id.BarberImage);
        BarberName = findViewById(R.id.BarberName);
        BarberRating = findViewById(R.id.BarberRating);
        ServiceName = findViewById(R.id.ServiceName);
        ServicePrice = findViewById(R.id.ServicePrice);
        ServiceDuration = findViewById(R.id.ServiceDuration);

        Glide.with(this)
                .load(BarberShopImageUrl_str)
                .into(BarberShopImage);
        Glide.with(this)
                .load(BarberImageUrl_str)
                .into(BarberImage);

        BarberShopName.setText(BarberShopName_str);
        BarberShopAddress.setText(BarberShopAddress_str);
        weekDay_monthName_dayOfMonth.setText(weekDay_monthName_dayOfMonth_str);
        Time.setText(Time_str);
        BarberName.setText(BarberName_str);
        BarberRating.setText(BarberRating_str);
        ServiceName.setText(ServiceName_str);
        ServicePrice.setText(ServicePrice_str);
        ServiceDuration.setText(ServiceDuration_str);


        appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");
        confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(v -> {
            ConfirmAppointment_InsertDatabase();
        });
    }

    public void ConfirmAppointment_InsertDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String UserEmail = "", UserName = "", UserPhoneNumber = "", UserImageUrl = "";

        if (currentUser != null) {
            UserEmail = MainActivity.userClass.getEmail();
            UserName = MainActivity.userClass.getFirst_name();
            UserPhoneNumber = MainActivity.userClass.getPhoneNumber();
            UserImageUrl = MainActivity.userClass.getPhotoUrl();
            Log.d("FirebaseAuth", "Current user name: " + UserName);
        } else {
            Log.e("FirebaseAuth", "No user is signed in");
        }

        String message_or_requests_str = message_or_requests.getText().toString().trim();
        String uniqueID = generateUniqueIDAndSave();

        if (BarbershopCoordinates_str != null) {
            Log.i("BarbershopCoordinates_str", BarbershopCoordinates_str);
        } else {
            Log.i("BarbershopCoordinates_str", "Message is null");
        }


//        if (BarberShopOwnerEmail_str!=null)
//        {
//            Log.i("BarberShopOwnerEmail_str",BarberShopOwnerEmail_str);
//        }
//        else
//            Log.i("BarberShopOwnerEmail_str","Null");

        Appointment Appointment = new Appointment(UserEmail, UserName, UserPhoneNumber, UserImageUrl, BarberShopImageUrl_str, BarberShopName_str,
                BarberShopAddress_str, BarbershopCoordinates_str, BarberShopRating_str, BarberShopOwnerEmail_str, weekDay_monthName_dayOfMonth_str, Time_str, BarberImageUrl_str,
                BarberName_str, BarberRating_str, ServiceName_str, ServicePrice_str, ServiceDuration_str, "Active", message_or_requests_str, uniqueID, BarberId, BarberShopsId);

        appointmentsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long newId = task.getResult().getChildrenCount();
                appointmentsRef.child(String.valueOf(newId)).setValue(Appointment)
                        .addOnCompleteListener(storeTask -> {
                            if (storeTask.isSuccessful()) {
                                Toast.makeText(ConfirmActivity.this, "Store successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmActivity.this, BooksActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ConfirmActivity.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ConfirmActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private String generate6CharID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder id = new StringBuilder();
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
    }


    public String generateUniqueIDAndSave() {
        String generatedID = generate6CharID();

        appointmentsRef.child(generatedID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    generateUniqueIDAndSave();
                }
            } else {
                Toast.makeText(ConfirmActivity.this, "Failed to check ID uniqueness", Toast.LENGTH_SHORT).show();
            }
        });
        return generatedID;
    }


    public void Back(View view) {
        onBackPressed();
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
}