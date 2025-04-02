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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DateTimeActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView BarberName, ServiceName;
    Button continue_button;

    Button selectedButton = null;

    //    public static Map<String, String> MapForConfirm = new HashMap<>();
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

//            button.setId(View.generateViewId()); // Генерируем уникальный ID
            rowLayout.addView(button);
        }


        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Создаем объект Calendar и устанавливаем выбранную дату
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                // Получаем день недели и название месяца
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault()); // День недели (например, Понедельник)
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault()); // Название месяца (например, Апрель)

                weekDay = dayFormat.format(calendar.getTime());
                monthName = monthFormat.format(calendar.getTime());
                dayOfMonth_str=String.valueOf(dayOfMonth);

                // Выводим в лог или в TextView
                Log.d("SelectedDate", "День недели: " + weekDay + ", Месяц: " + monthName + ", День: " + dayOfMonth);
                Toast.makeText(getApplicationContext(), "Выбрано: " + weekDay + ", " + dayOfMonth + " " + monthName, Toast.LENGTH_SHORT).show();
            }
        });


        continue_button.setOnClickListener(v -> {
            if (selectedButton != null) {
                Log.i("SelectedDate", (String) selectedButton.getText());
            }
            Intent intent = new Intent(this, ConfirmActivity.class);

//            MapForConfirm.put("BarberShopImage", "Text");
//            MapForConfirm.put("BarberShopName", "Text");

            intent.putExtra("weekDay_monthName_dayOfMonth", weekDay + " " + monthName + " " + dayOfMonth_str);
            intent.putExtra("Time", (String) selectedButton.getText());
            startActivity(intent);
        });
    }

    public void Back(View view) {
        SpecialistActivity.SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;
        onBackPressed();
    }
}