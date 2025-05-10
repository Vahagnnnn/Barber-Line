package com.vahagn.barber_line.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.vahagn.barber_line.Activities.BooksActivity;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.AppointmentAdapter;
import com.vahagn.barber_line.adapter.AppointmentAdapterAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    private TextView tabActive, tabPast, tabCanceled;

    List<Appointment> AppointmentsList = new ArrayList<>();
    AppointmentAdapterAdmin appointmentAdapterAdmin;
    RecyclerView appointmentsRecyclerView;


    public static LinearLayout noHistory;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tabActive = findViewById(R.id.tabActive);
        tabPast = findViewById(R.id.tabPast);
        tabCanceled = findViewById(R.id.tabCanceled);
        noHistory = findViewById(R.id.noHistory);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);


        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        appointmentAdapterAdmin = new AppointmentAdapterAdmin(AppointmentsList);
        appointmentsRecyclerView.setAdapter(appointmentAdapterAdmin);

        setActiveTab();

        tabActive.setOnClickListener(v -> setActiveTab());
        tabPast.setOnClickListener(v -> setPastTab());
        tabCanceled.setOnClickListener(v -> setCanceledTab());
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setTab(DatabaseReference dbRef, TextView selectedTab, TextView... otherTabs) {
        animateTabChange(selectedTab, Color.WHITE);
        animateTextBold(selectedTab, true);

        for (TextView tab : otherTabs) {
            animateTabChange(tab, Color.parseColor("#8E8E93"));
            animateTextBold(tab, false);
        }

        noHistory.setVisibility(GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        AppointmentsList.clear();
        appointmentAdapterAdmin.notifyDataSetChanged();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean hasAppointments = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment == null) continue;

                    if (AdminActivity.myBarbershopName != null &&
                            appointment.getBarbershopOwnerEmail().equals(currentUser.getEmail())) {
                        AppointmentsList.add(appointment);
                        hasAppointments = true;
                    } else if (AdminActivity.myWorkplaceName != null &&
                            appointment.getBarberShopsId() == AdminActivity.barberShopsId &&
                            appointment.getBarberId() == AdminActivity.barberId) {
                        AppointmentsList.add(appointment);
                        hasAppointments = true;
                    }
                }

                noHistory.setVisibility(hasAppointments ? GONE : VISIBLE);
                appointmentAdapterAdmin.notifyDataSetChanged();
                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error reading appointments", error.toException());
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setActiveTab() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
        setTab(ref, tabActive, tabPast, tabCanceled);
    }

    private void setPastTab() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Past_Appointments");
        setTab(ref, tabPast, tabActive, tabCanceled);
    }

    private void setCanceledTab() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Canceled_Appointments");
        setTab(ref, tabCanceled, tabActive, tabPast);
    }


//    @SuppressLint("NotifyDataSetChanged")
//    private void setActiveTab() {
//        animateTabChange(tabActive, Color.WHITE);
//        animateTextBold(tabActive, true);
//
//        animateTabChange(tabPast, Color.parseColor("#8E8E93"));
//        animateTextBold(tabPast, false);
//
//        animateTabChange(tabCanceled, Color.parseColor("#8E8E93"));
//        animateTextBold(tabCanceled, false);
//
//        noHistory.setVisibility(GONE);
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        AppointmentsList.clear();
//        appointmentAdapterAdmin.notifyDataSetChanged();
//
//        DatabaseReference activeAppointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        activeAppointmentsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean hasAppointments = false;
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Appointment appointment = snapshot.getValue(Appointment.class);
//
//                    if (appointment != null && AdminActivity.myBarbershopName != null) {
//                        assert currentUser != null;
//                        if (appointment.getBarbershopOwnerEmail().equals(currentUser.getEmail())) {
//                            AppointmentsList.add(appointment);
//                            hasAppointments = true;
//                        }
//                    } else if (appointment != null && AdminActivity.myWorkplaceName != null) {
//                        if (appointment.getBarberShopsId() == AdminActivity.barberShopsId && appointment.getBarberId() == AdminActivity.barberId) {
//                            AppointmentsList.add(appointment);
//                            hasAppointments = true;
//                        }
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
//                Log.e("Firebase", "Error reading active appointments", error.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void setPastTab() {
//        animateTabChange(tabPast, Color.WHITE);
//        animateTextBold(tabPast, true);
//
//        animateTabChange(tabActive, Color.parseColor("#8E8E93"));
//        animateTextBold(tabActive, false);
//
//        animateTabChange(tabCanceled, Color.parseColor("#8E8E93"));
//        animateTextBold(tabCanceled, false);
//
//        noHistory.setVisibility(GONE);
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        AppointmentsList.clear();
//        appointmentAdapterAdmin.notifyDataSetChanged();
//
//
//        DatabaseReference pastAppointmentsRef = FirebaseDatabase.getInstance().getReference("Past_Appointments");
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        pastAppointmentsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean hasAppointments = false;
//
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Appointment appointment = snapshot.getValue(Appointment.class);
//
//                        if (appointment != null && AdminActivity.myBarbershopName != null) {
//                            assert currentUser != null;
//                            if (appointment.getBarbershopOwnerEmail().equals(currentUser.getEmail())) {
//                                AppointmentsList.add(appointment);
//                                hasAppointments = true;
//                            }
//                        } else if (appointment != null && AdminActivity.myWorkplaceName != null) {
//                            if (appointment.getBarberShopsId() == AdminActivity.barberShopsId && appointment.getBarberId() == AdminActivity.barberId) {
//                                AppointmentsList.add(appointment);
//                                hasAppointments = true;
//                            }
//                        }
//                    }
//                }
//
//                noHistory.setVisibility(hasAppointments ? GONE : VISIBLE);
//                appointmentAdapterAdmin.notifyDataSetChanged();
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("Firebase", "Error reading past appointments", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void setCanceledTab() {
//        animateTabChange(tabCanceled, Color.WHITE);
//        animateTextBold(tabCanceled, true);
//
//        animateTabChange(tabActive, Color.parseColor("#8E8E93"));
//        animateTextBold(tabActive, false);
//
//        animateTabChange(tabPast, Color.parseColor("#8E8E93"));
//        animateTextBold(tabPast, false);
//
//        noHistory.setVisibility(GONE);
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        AppointmentsList.clear();
//        appointmentAdapterAdmin.notifyDataSetChanged();
//
//        DatabaseReference canceled_appointmentsRef = FirebaseDatabase.getInstance().getReference("Canceled_Appointments");
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        canceled_appointmentsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean hasAppointments = false;
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Appointment appointment = snapshot.getValue(Appointment.class);
//
//                    if (appointment != null && AdminActivity.myBarbershopName != null) {
//                        assert currentUser != null;
//                        if (appointment.getBarbershopOwnerEmail().equals(currentUser.getEmail())) {
//                            AppointmentsList.add(appointment);
//                            hasAppointments = true;
//                        }
//                    } else if (appointment != null && AdminActivity.myWorkplaceName != null) {
//                        if (appointment.getBarberShopsId() == AdminActivity.barberShopsId && appointment.getBarberId() == AdminActivity.barberId) {
//                            AppointmentsList.add(appointment);
//                            hasAppointments = true;
//                        }
//                    }
//
//                    noHistory.setVisibility(hasAppointments ? GONE : VISIBLE);
//                    appointmentAdapterAdmin.notifyDataSetChanged();
//                    loadingProgressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("Firebase", "Error reading canceled appointments", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }

    private void animateTabChange(TextView tab, int color) {
        ObjectAnimator colorAnim = ObjectAnimator.ofArgb(tab, "textColor", tab.getCurrentTextColor(), color);
        colorAnim.setDuration(300);
        colorAnim.start();
    }

    private void animateTextBold(TextView tab, boolean isBold) {
        float targetSize = isBold ? 1.2f : 1.0f;

        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(tab, "scaleX", 1.0f, targetSize);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(tab, "scaleY", 1.0f, targetSize);

        scaleXAnim.setDuration(300);
        scaleYAnim.setDuration(300);

        scaleXAnim.start();
        scaleYAnim.start();
    }


    public void GoBack(View view) {
        onBackPressed();
    }
}