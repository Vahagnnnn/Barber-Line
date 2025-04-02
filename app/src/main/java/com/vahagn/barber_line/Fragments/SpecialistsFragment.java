package com.vahagn.barber_line.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vahagn.barber_line.Activities.DateTimeActivity;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.Activities.SpecialistActivity;
import com.vahagn.barber_line.Admin.AddBarbersActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vahagn.barber_line.Classes.Barbers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpecialistsFragment extends Fragment {
    private LinearLayout infoContainer;
    private List<Barbers> specialists;

    public static boolean Edit;


    public static String imageUrl ,name;
    public static List<Services> ListServices= new ArrayList<>();

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


        if (specialist.getImage() != null && !specialist.getImage().isEmpty()) {
            Glide.with(SpecialistsFragment.this)
                    .load(specialist.getImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                    .into(specialistImage);
        }

        specialistName.setText(specialist.getName());
        specialistRating.setText(String.valueOf(specialist.getRating()));

        Log.i("ASAA", "ServicesActivity " + ServicesActivity.ServicesActivity);
        if (ServicesActivity.ServicesActivity) {
            specialistView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DateTimeActivity.class);
                startActivity(intent);
                name = specialist.getName();

//                Toast.makeText(getContext(), specialist.getName(), Toast.LENGTH_SHORT).show();
            });
        }
        else {
            specialistView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SpecialistActivity.class);

                imageUrl = specialist.getImage();
                name = specialist.getName();
                ListServices = specialist.getServices();

                startActivity(intent);

//                Toast.makeText(getContext(), specialist.getName(), Toast.LENGTH_SHORT).show();

            });
        }
        if (CreateBarberShopActivity.isCreateBarberShopActivity) {
            specialistView.setOnLongClickListener(v -> {
                showEditDeleteDialog(specialist);
                return true;
            });
        }

        infoContainer.addView(specialistView);
    }

    private void showEditDeleteDialog(Barbers specialist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit/Delete")
                .setMessage("What do you want to do?")
                .setPositiveButton("Edit", (dialog, which) -> editSpecialist(specialist))
                .setNegativeButton("Delete", (dialog, which) -> deleteSpecialist(specialist))
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void editSpecialist(Barbers specialist) {
        Toast.makeText(getContext(), "Editing " + specialist.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), AddBarbersActivity.class);
        Edit = true;

        AddBarbersActivity.imageUrl = specialist.getImage();
        AddBarbersActivity.name = specialist.getName();
//        AddBarbersActivity.phoneNumber = specialist.getPhone();
        AddBarbersActivity.ListServiceEdit = specialist.getServices();

        startActivity(intent);
    }

    private void deleteSpecialist(Barbers specialist) {
        specialists.remove(specialist);
        displaySpecialists();
        AddBarbersActivity.imageUrl = null;
        AddBarbersActivity.name = null;
//        AddBarbersActivity.phoneNumber = null;
        AddBarbersActivity.ListServiceEdit = null;
        Toast.makeText(getContext(), "Deleted " + specialist.getName(), Toast.LENGTH_SHORT).show();
    }

    private void displaySpecialists() {
        infoContainer.removeAllViews();
        for (Barbers specialist : specialists) {
            addSpecialist(specialist);
        }
    }
}
