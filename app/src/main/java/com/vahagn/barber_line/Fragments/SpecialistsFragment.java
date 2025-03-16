package com.vahagn.barber_line.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.vahagn.barber_line.R;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vahagn.barber_line.Classes.Barbers;

import java.util.List;

public class SpecialistsFragment extends Fragment {
    private LinearLayout infoContainer;
    private List<Barbers> specialists;

    public SpecialistsFragment() {
    }
    public SpecialistsFragment(List<Barbers> specialists) {
        this.specialists = specialists;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specialists, container, false);
        infoContainer = view.findViewById(R.id.info_container);
        if (specialists != null) {
            for (Barbers specialist : specialists) {
                addSpecialist(specialist);
            }
        }
        return view;
    }

    private void addSpecialist(Barbers specialist) {
        View specialistView = LayoutInflater.from(getContext()).inflate(R.layout.specialists, infoContainer, false);
        ImageView specialistImage = specialistView.findViewById(R.id.image);
        TextView specialistName = specialistView.findViewById(R.id.name);
        TextView specialistRating = specialistView.findViewById(R.id.rating);

//        int specialistImageResId = getResources().getIdentifier(specialist.getImage(), "drawable", getContext().getPackageName());
//        specialistImage.setImageResource(specialistImageResId);

        Glide.with(this)
                .load(specialist.getImage())
                .into(specialistImage);

        specialistName.setText(specialist.getName());
        specialistRating.setText(String.valueOf(specialist.getRating()));

        specialistView.setOnClickListener(v -> {
            Toast.makeText(getContext(), specialist.getName(), Toast.LENGTH_SHORT).show();
        });

        infoContainer.addView(specialistView);
    }
}
