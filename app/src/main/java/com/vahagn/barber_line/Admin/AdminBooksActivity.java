package com.vahagn.barber_line.Admin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.AppointmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminBooksActivity extends AppCompatActivity {

    RecyclerView appointmentsRecyclerView;
    List<Appointment> AppointmentsList = new ArrayList<>();
    AppointmentAdapter appointmentAdapter;

    public static String uniqueID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);

        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        appointmentAdapter = new AppointmentAdapter(AppointmentsList);
        appointmentsRecyclerView.setAdapter(appointmentAdapter);

        loadAppointments();

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
//                    if (appointment != null && appointment.getBarbershopOwnerEmail().equals(userEmail)) {
//                        AppointmentsList.add(appointment);
//                    }
                    if (appointment != null && AdminActivity.myBarbershopName != null) {
                        if (appointment.getBarbershopOwnerEmail().equals(userEmail)) {
                            AppointmentsList.add(appointment);
                        }
                    } else if (appointment != null && AdminActivity.myWorkplaceName != null) {
                        if (appointment.getBarbershopOwnerEmail().equals(userEmail)) {

                        }
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


    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToAdmin(View view) {
        navigateTo(AdminActivity.class);
    }

    public void ToSetting(View view) {
        if (AdminActivity.myBarbershopName == null && AdminActivity.myWorkplaceName == null) {
            Toast.makeText(this, "Create or join to barbershop", Toast.LENGTH_SHORT).show();
        } else if (AdminActivity.myBarbershopName != null) {
            navigateTo(AdminSettingsActivity.class);
        } else {
            navigateTo(BarberProfileActivity.class);
        }

//        navigateTo(AdminSettingsActivity.class);
    }

}