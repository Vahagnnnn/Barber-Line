package com.vahagn.barber_line.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.vahagn.barber_line.adapter.AppointmentAdapterAdmin;

import java.util.ArrayList;
import java.util.List;

public class AdminBooksActivity extends AppCompatActivity {

    RecyclerView appointmentsRecyclerView;
    List<Appointment> AppointmentsList = new ArrayList<>();
    //    AppointmentAdapter appointmentAdapter;
    AppointmentAdapterAdmin appointmentAdapterAdmin;

    public static LinearLayout noAppointments;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);

        noAppointments = findViewById(R.id.noAppointments);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        loadingProgressBar.setVisibility(View.VISIBLE);

        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

//        appointmentAdapter = new AppointmentAdapter(AppointmentsList);
//        appointmentsRecyclerView.setAdapter(appointmentAdapter);

        appointmentAdapterAdmin = new AppointmentAdapterAdmin(AppointmentsList);
        appointmentsRecyclerView.setAdapter(appointmentAdapterAdmin);

        loadAppointments();

    }


//    @SuppressLint("NotifyDataSetChanged")
//    private void setTab(DatabaseReference dbRef, TextView selectedTab, TextView... otherTabs) {
//        animateTabChange(selectedTab, Color.WHITE);
//        animateTextBold(selectedTab, true);
//
//        for (TextView tab : otherTabs) {
//            animateTabChange(tab, Color.parseColor("#8E8E93"));
//            animateTextBold(tab, false);
//        }
//
//        noHistory.setVisibility(GONE);
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        AppointmentsList.clear();
//        appointmentAdapterAdmin.notifyDataSetChanged();
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null) return;
//
//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean hasAppointments = false;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Appointment appointment = snapshot.getValue(Appointment.class);
//                    if (appointment == null) continue;
//
//                    if (AdminActivity.myBarbershopName != null &&
//                            appointment.getBarbershopOwnerEmail().equals(currentUser.getEmail())) {
//                        AppointmentsList.add(appointment);
//                        hasAppointments = true;
//                    } else if (AdminActivity.myWorkplaceName != null &&
//                            appointment.getBarberShopsId() == AdminActivity.workplaceId &&
//                            appointment.getBarberId() == AdminActivity.barberId) {
//                        AppointmentsList.add(appointment);
//                        hasAppointments = true;
//                    }
//                }
//
//                noHistory.setVisibility(hasAppointments ? GONE : VISIBLE);
//                appointmentAdapterAdmin.notifyDataSetChanged();
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Firebase", "Error reading appointments", error.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppointmentsList.clear();
                boolean hasAppointments = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null && AdminActivity.myBarbershopName != null) {
                        if (appointment.getBarbershopOwnerEmail().equals(userEmail)) {
                            AppointmentsList.add(appointment);
                            hasAppointments = true;
                        }
                    } else if (appointment != null && AdminActivity.myWorkplaceName != null) {
                        if (appointment.getBarberShopsId() == AdminActivity.workplaceId && appointment.getBarberId() == AdminActivity.barberId) {
                            AppointmentsList.add(appointment);
                            hasAppointments = true;
                        }
                    }
                }

                if (hasAppointments) {
                    noAppointments.setVisibility(GONE);
                } else {
                    noAppointments.setVisibility(View.VISIBLE);
                }
                loadingProgressBar.setVisibility(View.GONE);
//                appointmentAdapter.notifyDataSetChanged();
                appointmentAdapterAdmin.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingProgressBar.setVisibility(View.GONE);
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