package com.vahagn.barber_line.Fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
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
    private MaterialCardView mondayCircle, tuesdayCircle, wednesdayCircle, thursdayCircle, fridayCircle, saturdayCircle, sundayCircle;

    private TextView mondayText, tuesdayText, wednesdayText, thursdayText,
            fridayText, saturdayText, sundayText;

    Map<String, TimeRange> openingTimes;

    public AboutFragment() {
    }

    public AboutFragment(Map<String, TimeRange> openingTimes) {
        this.openingTimes = openingTimes;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mondayCircle = view.findViewById(R.id.mondayCircle);
        tuesdayCircle = view.findViewById(R.id.tuesdayCircle);
        wednesdayCircle = view.findViewById(R.id.wednesdayCircle);
        thursdayCircle = view.findViewById(R.id.thursdayCircle);
        fridayCircle = view.findViewById(R.id.fridayCircle);
        saturdayCircle = view.findViewById(R.id.saturdayCircle);
        sundayCircle = view.findViewById(R.id.sundayCircle);


        mondayText = view.findViewById(R.id.mondayText);
        tuesdayText = view.findViewById(R.id.tuesdayText);
        wednesdayText = view.findViewById(R.id.wednesdayText);
        thursdayText = view.findViewById(R.id.thursdayText);
        fridayText = view.findViewById(R.id.fridayText);
        saturdayText = view.findViewById(R.id.saturdayText);
        sundayText = view.findViewById(R.id.sundayText);


        mondayText.setText(getTime("Monday"));
        tuesdayText.setText(getTime("Tuesday"));
        wednesdayText.setText(getTime("Wednesday"));
        thursdayText.setText(getTime("Thursday"));
        fridayText.setText(getTime("Friday"));
        saturdayText.setText(getTime("Saturday"));
        sundayText.setText(getTime("Sunday"));


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

        if (!open.isEmpty() && !close.isEmpty()) {
            return open + " - " + close;
        } else {
            switch (day) {
                case "Monday":
                    mondayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Tuesday":
                    tuesdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Wednesday":
                    tuesdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    wednesdayCircle.setCardBackgroundColor(Color.parseColor("#ff0000"));
                    Log.i("circle","wednesdayCircle");
                    break;
                case "Thursday":
                    thursdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Friday":
                    fridayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Saturday":
                    saturdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Sunday":
                    sundayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                default:
                    Log.i("circle","Invalid");
                    break;
            }
            return "Closed";
        }
    }
}