package com.vahagn.barber_line.Activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DateTimeActivity extends AppCompatActivity {
    TextView weekDay_monthName_dayOfMonth, Time;

    CalendarView calendarView;
    TextView BarberName, ServiceName;
    Button continue_button,close;

    Button selectedButton = null;

    public String weekDay, monthName, dayOfMonth_str;
    boolean isClose = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        weekDay_monthName_dayOfMonth = findViewById(R.id.weekDay_monthName_dayOfMonth);
        Time = findViewById(R.id.Time);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        BarberName = findViewById(R.id.BarberName);
        BarberName.setText(SpecialistsFragment.name);
        ServiceName = findViewById(R.id.ServiceName);
        ServiceName.setText(ServicesFragment.name);

        Calendar calendar = Calendar.getInstance();
        updateDateVariables(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        LinearLayout timeContainer = findViewById(R.id.timeContainer);
        continue_button = findViewById(R.id.continue_button);

        List<String> times = Arrays.asList(
                "10:00", "10:30",
                "11:00", "11:30",
                "12:00", "12:30",
                "13:00", "13:30",
                "14:00", "14:30",
                "15:00", "15:30",
                "16:00", "16:30",
                "17:00", "17:30",
                "18:00", "18:30",
                "19:00", "19:30",
                "20:00", "20:30",
                "21:00"
        );

        int columns = 4;
        LinearLayout rowLayout = null;

        for (int i = 0; i < times.size(); i++) {
            if (i % columns == 0) {
                rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setGravity(Gravity.CENTER);
                timeContainer.addView(rowLayout);
            }

            Button button = new Button(this);
            button.setText(times.get(i));
            button.setTextSize(16);
            button.setPadding(20, 10, 20, 10);
            button.setBackgroundResource(R.drawable.time_button_selector);
            button.setTextColor(Color.WHITE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(8, 8, 8, 8);
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                if (selectedButton != null) {
                    selectedButton.setSelected(false);
                }

                button.setSelected(true);
                selectedButton = button;
                Time.setText(selectedButton.getText());
            });

            rowLayout.addView(button);
        }


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            updateDateVariables(year, month, dayOfMonth);
        });


        continue_button.setOnClickListener(v -> ContinueButton());

        close = findViewById(R.id.close);
        close.setOnClickListener(v -> closeButton());

    }

    private void closeButton() {
        if (isClose) {
            calendarView.setVisibility(VISIBLE);
            isClose = false;
        } else {
            calendarView.setVisibility(GONE);
            isClose = true;
        }


    }

    private void ContinueButton() {
        if (selectedButton != null) {
            if (ManageAppointmentActivity.is_reschedule_appointment) {
                DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("Appointments");

                appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Appointment appointment = snapshot.getValue(Appointment.class);
                            if (appointment != null && appointment.getUniqueID().equals(BooksActivity.uniqueID)) {
                                Log.i("uniqueID", BooksActivity.uniqueID);
                                appointment.setWeekDay_monthName_dayOfMonth(weekDay + " " + monthName + " " + dayOfMonth_str);
                                appointment.setTime((String) selectedButton.getText());

                                snapshot.getRef().setValue(appointment);

                                ManageAppointmentActivity.is_reschedule_appointment = false;

                                AppointmentsAboutActivity.weekDay_monthName_dayOfMonth_str = appointment.getWeekDay_monthName_dayOfMonth();
                                AppointmentsAboutActivity.Time_str = appointment.getTime();
                                ManageAppointmentActivity.appointmentDateAndTime.setText(
                                        AppointmentsAboutActivity.weekDay_monthName_dayOfMonth_str
                                                + " at " +
                                                AppointmentsAboutActivity.Time_str);

                                onBackPressed();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("Firebase", "Failed to read appointments.", databaseError.toException());
                    }
                });
            } else {
                Intent intent = new Intent(this, ConfirmActivity.class);
                intent.putExtra("weekDay_monthName_dayOfMonth", weekDay + " " + monthName + " " + dayOfMonth_str);
                intent.putExtra("Time", (String) selectedButton.getText());
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No time selected", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("SetTextI18n")
    private void updateDateVariables(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

        weekDay = dayFormat.format(calendar.getTime());
        monthName = monthFormat.format(calendar.getTime());
        dayOfMonth_str = String.valueOf(dayOfMonth);

        weekDay_monthName_dayOfMonth.setText(weekDay + " " + monthName + " " + dayOfMonth_str);
        Log.d("DateTimeActivity", "Date updated: " + weekDay + ", " + dayOfMonth + " " + monthName);
    }

    public void Back(View view) {
        SpecialistActivity.SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;
        onBackPressed();
    }
}