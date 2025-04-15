package com.vahagn.barber_line.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.OpeningTime;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AboutFragment extends Fragment {
    private TextView mondayText, tuesdayText, wednesdayText, thursdayText,
            fridayText, saturdayText, sundayText;

    Map<String, TimeRange> openingTimes;

    public AboutFragment() {
    }

    public AboutFragment( Map<String, TimeRange> openingTimes) {
        this.openingTimes = openingTimes;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mondayText = view.findViewById(R.id.mondayText);
        tuesdayText = view.findViewById(R.id.tuesdayText);
        wednesdayText = view.findViewById(R.id.wednesdayText);
        thursdayText = view.findViewById(R.id.thursdayText);
        fridayText = view.findViewById(R.id.fridayText);
        saturdayText = view.findViewById(R.id.saturdayText);
        sundayText = view.findViewById(R.id.sundayText);


        mondayText.setText("Monday: " + getTime("Monday"));
        tuesdayText.setText("Tuesday: " + getTime("Tuesday"));
        wednesdayText.setText("Wednesday: " + getTime("Wednesday"));
        thursdayText.setText("Thursday: " + getTime("Thursday"));
        fridayText.setText("Friday: " + getTime("Friday"));
        saturdayText.setText("Saturday: " + getTime("Saturday"));
        sundayText.setText("Sunday: " + getTime("Sunday"));


//        DatabaseReference opening_timesRef = FirebaseDatabase.getInstance().getReference().child("opening_times");
//        DatabaseReference myRef =            FirebaseDatabase.getInstance().getReference().child("barberShops");

//        opening_timesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mondayText.setText("Monday: " + getTime(snapshot, "Monday"));
//                tuesdayText.setText("Tuesday: " + getTime(snapshot, "Tuesday"));
//                wednesdayText.setText("Wednesday: " + getTime(snapshot, "Wednesday"));
//                thursdayText.setText("Thursday: " + getTime(snapshot, "Thursday"));
//                fridayText.setText("Friday: " + getTime(snapshot, "Friday"));
//                saturdayText.setText("Saturday: " + getTime(snapshot, "Saturday"));
//                sundayText.setText("Sunday: " + getTime(snapshot, "Sunday"));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//
//            private String getTime(DataSnapshot snapshot, String day) {
//                DataSnapshot daySnapshot = snapshot.child(day);
//                String open = daySnapshot.child("open").getValue(String.class);
//                String close = daySnapshot.child("close").getValue(String.class);
//                Log.d("Monday", daySnapshot.toString());
//                assert open != null;
//                Log.d("Monday", open);
//                assert close != null;
//                Log.d("Monday", close);
//
//                if (open != null && close != null) {
//                    return open + " - " + close;
//                } else {
//                    return "Closed";
//                }
//            }
//
//        });
        return view;
    }


    private String getTime(String day) {
        String open = Objects.requireNonNull(openingTimes.get(day)).getOpen();
        String close = Objects.requireNonNull(openingTimes.get(day)).getClose();

//        if (openingTimes != null) {
//            Log.d("Mapi", "Map is: " + openingTimes.get(day));
//        } else {
//            Log.d("Mapi", "Map is: Null");
//
//        }


        if (open != null && close != null) {
            return open + " - " + close;
        } else {
            return "Closed";
        }
//        return "Closed";

    }
}