package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.AdminBarberShopsAboutActivity;
import com.vahagn.barber_line.Admin.AdminSettingsActivity;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.AppointmentAdapter;
import com.vahagn.barber_line.adapter.TopBarbersAdapter;
import com.vahagn.barber_line.model.TopBarbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BooksActivity extends AppCompatActivity {

    RecyclerView appointmentsRecyclerView;
    List<Appointment> AppointmentsList = new ArrayList<>();
    AppointmentAdapter appointmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);


        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        appointmentAdapter = new AppointmentAdapter(AppointmentsList);
        appointmentsRecyclerView.setAdapter(appointmentAdapter);

        loadAppointments();



//        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");

//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String userEmail = "";
//
//        if (currentUser != null) {
//            userEmail = currentUser.getEmail();
//            Log.d("FirebaseAuth", "Current user email: " + userEmail);
//        } else {
//            Log.e("FirebaseAuth", "No user is signed in");
//        }
//
//        String UserEmail = userEmail;
//        appointmentsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Appointment appointment = snapshot.getValue(Appointment.class);
//                    if (appointment != null && Objects.equals(appointment.getUserEmail(), UserEmail)) {
//                        AppointmentsList.add(new Appointment(snapshot.getName(), shop.getAddress(), shop.getCoordinates(), shop.getImage(), shop.getLogo(), shop.getRating(), shop.getReviews(), shop.getServices(), shop.getSpecialists()));
//
//
//                        //                        Appointment Appointment = new Appointment(UserEmail, UserName, BarberShopImageUrl_str, BarberShopName_str,
////                                BarberShopAddress_str, weekDay_monthName_dayOfMonth_str, Time_str, BarberImageUrl_str,
////                                BarberName_str, BarberRating_str, ServiceName_str, ServicePrice_str, ServiceDuration_str, "Active", message_or_requests_str);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//            }
//        });
//        AddAppointments(appointments_container,AppointmentsList);


    }


    private void loadAppointments() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e("FirebaseAuth", "No user is signed in");
            return;
        }

        String userEmail = currentUser.getEmail();
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppointmentsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null && appointment.getUserEmail().equals(userEmail)) {
                        AppointmentsList.add(appointment);
                    }
                }
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }
//    private void AddAppointments(LinearLayout container, List<Appointment> AppointmentsList) {
//        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, container, false);
//
//        appointmentAdapter = new AppointmentAdapter(this, AppointmentsList);
//
//        container.addView(barbershopView);
//
//    }

//    public void addBarbershop(LinearLayout container) {
//        View appointmentView = LayoutInflater.from(this).inflate(R.layout.book_item, container, false);
//

        //        ImageView logoImageView = appointmentView.findViewById(R.id.logo);

//        Glide.with(this)
//                .load(logo)
//                .into(logoImageView);
//
//        TextView nameTextView = barbershopView.findViewById(R.id.name);
//        TextView addressTextView = barbershopView.findViewById(R.id.address);
//
//        nameTextView.setText(name);
//        addressTextView.setText(address);

//
//        barbershopView.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//
//            AdminSettingsActivity.imageUrl = imageUrl;
//            AdminSettingsActivity.name = name;
//            AdminSettingsActivity.rating = String.valueOf(rating);
//            AdminSettingsActivity.address = address;
//            AdminSettingsActivity.ListSpecialist = ListSpecialist;
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                    this,
//                    findViewById(R.id.bottom_navigation),
//                    "sharedImageTransition");
//            startActivity(intent, options.toBundle());
//        });

//        container.addView(appointmentView);
//    }


    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }

    public void ToMap(View view) {
        navigateTo(MapActivity.class);
    }

    public void ToAdmin(View view) {
        if (isLogin)
            navigateTo(AdminActivity.class);
        else
            navigateTo(LoginActivity.class);
    }

    public void To(View view) {
        Log.i("isLogin", String.valueOf(isLogin));
        if (isLogin) {
            Log.i("isLogin", String.valueOf(isLogin));
            navigateTo(SettingsActivity.class);
        } else {
            Log.i("isLogin", String.valueOf(isLogin));
            navigateTo(LoginActivity.class);
        }

    }

}