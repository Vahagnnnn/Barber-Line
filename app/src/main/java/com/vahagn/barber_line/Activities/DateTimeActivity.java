package com.vahagn.barber_line.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateTimeActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView BarberName, ServiceName;
    Button continue_button;

    Button selectedButton = null;

    public String weekDay, monthName, dayOfMonth_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        calendarView = findViewById(R.id.calendarView);
        BarberName = findViewById(R.id.BarberName);
        BarberName.setText(SpecialistsFragment.name);
        ServiceName = findViewById(R.id.ServiceName);
        ServiceName.setText(ServicesFragment.name);

        Calendar calendar = Calendar.getInstance();
        updateDateVariables(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        LinearLayout timeContainer = findViewById(R.id.timeContainer);
        continue_button = findViewById(R.id.continue_button);

        List<String> times = Arrays.asList("13:45", "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45", "16:00",
                "16:15", "16:30", "16:45", "15:45", "16:00",
                "16:15", "16:30", "16:45", "15:45", "16:00",
                "16:15", "16:30", "16:45");

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
            });

            rowLayout.addView(button);
        }


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                updateDateVariables(year, month, dayOfMonth);
//                Toast.makeText(getApplicationContext(), "Выбрано: " + weekDay + ", " + dayOfMonth + " " + monthName, Toast.LENGTH_SHORT).show();
            }
        });


        continue_button.setOnClickListener(v -> {

            if (selectedButton != null) {

                Intent intent = new Intent(this, ConfirmActivity.class);

                intent.putExtra("weekDay_monthName_dayOfMonth", weekDay + " " + monthName + " " + dayOfMonth_str);
                intent.putExtra("Time", (String) selectedButton.getText());
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(), "No time selected", Toast.LENGTH_SHORT).show();
        });
    }


    private void updateDateVariables(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

        weekDay = dayFormat.format(calendar.getTime());
        monthName = monthFormat.format(calendar.getTime());
        dayOfMonth_str = String.valueOf(dayOfMonth);

        Log.d("DateTimeActivity", "Date updated: " + weekDay + ", " + dayOfMonth + " " + monthName);
    }

    public void Back(View view) {
        SpecialistActivity.SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;
        onBackPressed();
    }
}