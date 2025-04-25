package com.vahagn.barber_line.Activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.R;

import java.util.Objects;

public class ManageAppointmentActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static TextView appointmentDateAndTime;
    TextView ServiceName;

    public static boolean is_reschedule_appointment;
    LinearLayout reschedule_appointment, cancel_appointment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_appointment);

        appointmentDateAndTime = findViewById(R.id.appointmentDateAndTime);
        ServiceName = findViewById(R.id.ServiceName);
        appointmentDateAndTime.setText(AppointmentsAboutActivity.weekDay_monthName_dayOfMonth_str + " at " + AppointmentsAboutActivity.Time_str);
        ServiceName.setText(AppointmentsAboutActivity.ServiceName_str);

        reschedule_appointment = findViewById(R.id.reschedule_appointment);
        reschedule_appointment.setOnClickListener(v -> reschedule_appointment());

        cancel_appointment = findViewById(R.id.cancel_appointment);
        cancel_appointment.setOnClickListener(v -> cancel_appointment());
    }

    private void reschedule_appointment() {
        is_reschedule_appointment = true;
        navigateTo(DateTimeActivity.class);
    }

    private void cancel_appointment() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");
        DatabaseReference canceled_appointmentsRef = FirebaseDatabase.getInstance().getReference("Canceled_Appointments");

        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null && appointment.getUniqueID().equals(BooksActivity.uniqueID)) {
                        Log.i("uniqueID", BooksActivity.uniqueID);

                        canceled_appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot canceledSnapshot) {
                                long nextId = 0;

                                for (DataSnapshot child : canceledSnapshot.getChildren()) {
                                    try {
                                        long id = Long.parseLong(Objects.requireNonNull(child.getKey()));
                                        if (id >= nextId) {
                                            nextId = id + 1;
                                        }
                                    } catch (NumberFormatException ignored) {
                                    }
                                }

                                appointment.setStatus("Canceled");
                                canceled_appointmentsRef.child(String.valueOf(nextId)).setValue(appointment)
                                        .addOnSuccessListener(aVoid -> {
                                            snapshot.getRef().removeValue();
                                            BooksActivity.NewAppointmentsLayout.setVisibility(VISIBLE);
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("Firebase", "Failed to read canceled appointments.", error.toException());
                            }
                        });

                        break; // Можно выйти из цикла, если найден нужный appointment
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read appointments.", databaseError.toException());
            }
        });

        navigateTo(BooksActivity.class);
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

    public void Back(View view) {
        onBackPressed();
    }

    public void ToAdmin(View view) {
        if (isLogin)
            navigateTo(AdminActivity.class);
        else
            navigateTo(LoginActivity.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

}