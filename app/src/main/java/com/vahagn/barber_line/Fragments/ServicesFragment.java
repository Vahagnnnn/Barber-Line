package com.vahagn.barber_line.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vahagn.barber_line.Admin.AddBarbersActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import java.util.List;


public class ServicesFragment extends Fragment {
    private LinearLayout servicesContainer;
    private List<Services> serviceslists;

    public ServicesFragment(List<Services> serviceslists) {
        this.serviceslists = serviceslists;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        servicesContainer = view.findViewById(R.id.services_container);
        if (serviceslists != null) {
                for (Services services : serviceslists) {
                addServices(servicesContainer, services);
            }
        }
        return view;
    }

    public void updateServicesList(List<Services> newServicesList) {
        this.serviceslists = newServicesList;
        servicesContainer.removeAllViews();
        if (serviceslists != null) {
            for (Services services : serviceslists) {
                addServices(servicesContainer, services);
            }
        }
    }

    private void addServices(LinearLayout container, Services services) {
        View servicesView = LayoutInflater.from(getContext()).inflate(R.layout.services, container, false);
        TextView serviceName = servicesView.findViewById(R.id.name);
        TextView serviceDuration= servicesView.findViewById(R.id.duration);
        TextView servicePrice= servicesView.findViewById(R.id.price);

        serviceName.setText(services.getName());
        serviceDuration.setText(services.getDuration());
        servicePrice.setText(services.getPrice());

        servicesView.setOnClickListener(v -> {
            Toast.makeText(getContext(), services.getName(), Toast.LENGTH_SHORT).show();
        });

//        servicesView.setOnLongClickListener(v -> {
//            showEditDeleteDialog(services);
//            return true;
//        });

        container.addView(servicesView);
    }

//    private void showEditDeleteDialog(Services services) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Edit/Delete")
//                .setMessage("What do you want to do?")
//                .setPositiveButton("Edit", (dialog, which) -> editSpecialist(services))
//                .setNegativeButton("Delete", (dialog, which) -> deleteSpecialist(services))
//                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
//                .show();
//    }

//    private void editSpecialist(Services services) {
//        Toast.makeText(getContext(), "Editing " + specialist.getName(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getContext(), AddBarbersActivity.class);
//        Edit = true;
//
//        AddBarbersActivity.name = specialist.getName();
////        AddBarbersActivity.phoneNumber = specialist.getPhone();    private String name;
////    private String price;
////    public String duration;
//        AddBarbersActivity.ListServiceEdit = specialist.getServices();
//
//        startActivity(intent);
//    }

//    private void deleteSpecialist(Services services) {
//        serviceslists.remove(services);
//        displayServices();
//        AddBarbersActivity.imageUrl = null;
//        AddBarbersActivity.name = null;
////        AddBarbersActivity.phoneNumber = null;
//        AddBarbersActivity.ListServiceEdit = null;
//        Toast.makeText(getContext(), "Deleted " + services.getName(), Toast.LENGTH_SHORT).show();
//    }

//    private void displayServices() {
//        container.removeAllViews();
//        for (Services services : serviceslists) {
//            addSpecialist(specialist);
//        }
//    }

}