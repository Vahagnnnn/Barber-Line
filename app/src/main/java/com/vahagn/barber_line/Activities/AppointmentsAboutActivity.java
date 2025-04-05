package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.adapter.AppointmentAdapter;

import java.util.Calendar;
import java.util.Objects;

public class AppointmentsAboutActivity extends AppCompatActivity {
    DatabaseReference barberShops = FirebaseDatabase.getInstance().getReference("barberShops");

    ImageView BarberShopImage;
    TextView BarberShopName, weekDay_monthName_dayOfMonth, ServiceDuration, BarberShopAddress, ServiceName, ServiceDuration1;
    public static String weekDay_monthName_dayOfMonth_str, Time_str, ServiceName_str;
    LinearLayout add_to_calendar,getting_there, venue_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_about);
        String[] coords = Objects.requireNonNull(getIntent().getStringExtra("BarbershopCoordinates")).split(" ");

        if (getIntent().getStringExtra("BarbershopCoordinates") != null) {
            Log.i("BarbershopCoordinates_str","AppointmentsAboutActivity = " + getIntent().getStringExtra("BarbershopCoordinates"));
        } else {
            Log.i("BarbershopCoordinates_str", "AppointmentsAboutActivity = " +"Message is null");
        }

        BarberShopImage = findViewById(R.id.BarberShopImage);
        BarberShopName = findViewById(R.id.BarberShopName);
        weekDay_monthName_dayOfMonth = findViewById(R.id.weekDay_monthName_dayOfMonth);
        ServiceDuration = findViewById(R.id.ServiceDuration);
        BarberShopAddress = findViewById(R.id.BarberShopAddress);
        ServiceName = findViewById(R.id.ServiceName);
        ServiceDuration1 = findViewById(R.id.ServiceDuration1);

        Glide.with(this)
                .load(AppointmentAdapter.BarbershopImageUrl)
                .into(BarberShopImage);

        BarberShopName.setText(getIntent().getStringExtra("BarberShopName"));
        weekDay_monthName_dayOfMonth_str = getIntent().getStringExtra("weekDay_monthName_dayOfMonth");
        weekDay_monthName_dayOfMonth.setText(weekDay_monthName_dayOfMonth_str);
        Time_str = getIntent().getStringExtra("Time");
        ServiceDuration.setText(getIntent().getStringExtra("ServiceDuration"));
        BarberShopAddress.setText(getIntent().getStringExtra("BarberShopAddress"));
        ServiceName_str = getIntent().getStringExtra("ServiceName");
        ServiceName.setText(ServiceName_str);
        ServiceDuration1.setText(getIntent().getStringExtra("ServiceDuration"));

        add_to_calendar = findViewById(R.id.add_to_calendar);
        add_to_calendar.setOnClickListener(v -> add_to_calendar());

        getting_there = findViewById(R.id.getting_there);
        getting_there.setOnClickListener(v -> getting_there(coords));

        venue_details = findViewById(R.id.venue_details);
        venue_details.setOnClickListener(v -> venue_details());
    }

    private void add_to_calendar() {
        // Check for both read and write permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request the permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
        } else {
            // Permissions granted, proceed with showing the calendar app
            addEventToCalendar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if both permissions are granted
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, proceed with adding event
                addEventToCalendar();
            } else {
                // Handle the case where permissions are denied
                Log.e("Permissions", "Permissions not granted");
            }
        }
    }

    private void addEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        intent.putExtra(CalendarContract.Events.TITLE, "Haircut at " + BarberShopName.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Service: " + ServiceName_str + "\nDuration: " + ServiceDuration.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, BarberShopAddress.getText().toString());

        String[] Date =weekDay_monthName_dayOfMonth_str.split(" ");
        int DateDay = Integer.parseInt(Date[2].trim());

        String[] Time =Time_str.split(":");
        int Hour = Integer.parseInt(Time[0].trim());
        int Minute = Integer.parseInt(Time[1].trim());

        Log.i("DateDay", String.valueOf(DateDay));
        Log.i("DateDay", String.valueOf(Hour));
        Log.i("DateDay", String.valueOf(Minute));
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2025, Calendar.APRIL, DateDay, Hour, Minute);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());

        Calendar endTime = Calendar.getInstance();
        endTime.set(2025, Calendar.APRIL, DateDay, Hour+1, Minute);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());

        intent.putExtra(CalendarContract.Reminders.MINUTES, 15);

        try {
            startActivity(intent);
            Log.d("Calendar", "Calendar intent started successfully!");
        } catch (Exception e) {
            Log.e("Calendar", "Error starting calendar intent: " + e.getMessage());
        }
    }

//    private void addEventToCalendar() {
//        // Dynamically fetch available calendar
//        Cursor cursor = getContentResolver().query(
//                CalendarContract.Calendars.CONTENT_URI,
//                new String[]{CalendarContract.Calendars._ID},
//                null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            @SuppressLint("Range") int calendarId = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
//
//            // Create calendar instance for event start time
//            Calendar beginTime = Calendar.getInstance();
//            beginTime.set(2025, Calendar.APRIL, 10, 10, 30); // Set start time (Year, Month, Day, Hour, Minute)
//            long startTime = beginTime.getTimeInMillis();
//
//            // Create calendar instance for event end time
//            Calendar endTime = Calendar.getInstance();
//            endTime.set(2025, Calendar.APRIL, 10, 11, 30); // Set end time
//            long endTimeInMillis = endTime.getTimeInMillis();
//
//            // Create content values to insert the event
//            ContentValues values = new ContentValues();
//            values.put(CalendarContract.Events.DTSTART, startTime); // Event start time
//            values.put(CalendarContract.Events.DTEND, endTimeInMillis); // Event end time
//            values.put(CalendarContract.Events.TITLE, "Sample Event"); // Event title
//            values.put(CalendarContract.Events.DESCRIPTION, "This is a sample event description."); // Event description
//            values.put(CalendarContract.Events.CALENDAR_ID, calendarId); // Use the fetched calendar ID
//            values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC"); // Time zone of the event
//
//            // Insert event into the calendar
//            try {
//                getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
//                Log.d("Calendar", "Event added to calendar successfully!");
//            } catch (Exception e) {
//                Log.e("Calendar", "Error adding event: " + e.getMessage());
//            }
//
//            cursor.close();
//        } else {
//            Log.e("Calendar", "No calendar found.");
//        }
//    }





    private void getting_there(String[] coords) {
        String latitudeString = coords[0].replace(",", "").trim();
        String longitudeString = coords[1].replace(",", "").trim();

        double latitude = Double.parseDouble(latitudeString);
        double longitude = Double.parseDouble(longitudeString);

        finish();

        openGoogleMapsForNavigation(latitude, longitude);
    }

    public void openGoogleMapsForNavigation(double latitude, double longitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
    }

    private void venue_details() {
        {
            Intent intent = new Intent(this, BarberShopsAboutActivity.class);
            intent.putExtra("from_where", "AppointmentsAboutActivity");

            BarbersActivity.imageUrl = AppointmentAdapter.BarbershopImageUrl;
            BarbersActivity.name = getIntent().getStringExtra("BarberShopName");
            BarbersActivity.rating = getIntent().getStringExtra("BarbershopRating");
            BarbersActivity.address = getIntent().getStringExtra("BarberShopAddress");

            barberShops.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BarberShops shop = snapshot.getValue(BarberShops.class);
                        if (shop != null && shop.getName().equals(BarbersActivity.name)) {
                            BarbersActivity.ListSpecialist = shop.getSpecialists();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Firebase", "Failed to read value.", databaseError.toException());
                }
            });


            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.bottom_navigation),
                    "sharedImageTransition");
            startActivity(intent, options.toBundle());
        }
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

    public void ToManageAppointment(View view) {
        navigateTo(ManageAppointmentActivity.class);
    }

    public void ToBooks(View view) {
        if (isLogin)
            navigateTo(BooksActivity.class);
        else
            navigateTo(LoginActivity.class);
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