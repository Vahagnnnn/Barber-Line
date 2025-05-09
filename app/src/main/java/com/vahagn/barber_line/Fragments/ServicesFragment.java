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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vahagn.barber_line.Activities.DateTimeActivity;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Activities.SpecialistActivity;
import com.vahagn.barber_line.Admin.AddBarbersActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import java.util.List;


public class ServicesFragment extends Fragment {
    private LinearLayout servicesContainer;
    private List<Services> serviceslists;

    public static String name, price, duration;
    public static int barberShopsId;

    public ServicesFragment() {
    }
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
                addServices(services);
            }
        }
        return view;
    }

    public void updateServicesList(List<Services> newServicesList) {
        this.serviceslists = newServicesList;
        servicesContainer.removeAllViews();
        if (serviceslists != null) {
            for (Services services : serviceslists) {
                addServices(services);
            }
        }
    }

    private void addServices(Services services) {
        View servicesView = LayoutInflater.from(getContext()).inflate(R.layout.services, servicesContainer, false);
        TextView serviceName = servicesView.findViewById(R.id.name);
        TextView serviceDuration = servicesView.findViewById(R.id.duration);
        TextView servicePrice = servicesView.findViewById(R.id.price);

        serviceName.setText(services.getName());
        serviceDuration.setText(services.getDuration());
        servicePrice.setText(services.getPrice());

        Log.i("ASAA", "SpecialistActivity " + SpecialistActivity.SpecialistActivity);
        if (SpecialistActivity.SpecialistActivity) {
            servicesView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DateTimeActivity.class);
                startActivity(intent);
                name = services.getName();
                price = services.getPrice();
                duration = services.getDuration();
                barberShopsId = serviceDuration.getId();
            });
        } else if (SpecialistsFragment.CanBook){
            servicesView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ServicesActivity.class);

                name = services.getName();
                price = services.getPrice();
                duration = services.getDuration();

                startActivity(intent);

//                Toast.makeText(getContext(), services.getName(), Toast.LENGTH_SHORT).show();
            });
        }
        if (CreateBarberShopActivity.isCreateBarberShopActivity) {
            servicesView.setOnLongClickListener(v -> {
                showEditDeleteDialog(services);
                return true;
            });
        }
        servicesContainer.addView(servicesView);
    }

    private void showEditDeleteDialog(Services services) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit/Delete")
                .setMessage("What do you want to do?")
                .setPositiveButton("Edit", (dialog, which) -> editServices(services))
                .setNegativeButton("Delete", (dialog, which) -> deleteServices(services))
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void editServices(Services services) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText Service_Name_input = new EditText(getContext());
        Service_Name_input.setHint("Enter the name of the service");
        Service_Name_input.setText(services.getName());

        EditText Service_Price_input = new EditText(getContext());
        Service_Price_input.setHint("Enter the price of the service");
        Service_Price_input.setText(services.getPrice());

        EditText Service_Duration_input = new EditText(getContext());
        Service_Duration_input.setHint("Enter the duration of the service");
        Service_Duration_input.setText(services.getDuration());

        layout.addView(Service_Name_input);
        layout.addView(Service_Price_input);
        layout.addView(Service_Duration_input);

        builder.setTitle("Edit Service")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = Service_Name_input.getText().toString();
                    String newPrice = Service_Price_input.getText().toString();
                    String newDuration = Service_Duration_input.getText().toString();

                    services.setName(newName);
                    services.setPrice(newPrice);
                    services.setDuration(newDuration);

                    displayServices();

                    Toast.makeText(getContext(), "Service updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void deleteServices(Services services) {
        serviceslists.remove(services);
        displayServices();
        Toast.makeText(getContext(), "Deleted " + services.getName(), Toast.LENGTH_SHORT).show();
    }

    private void displayServices() {
        servicesContainer.removeAllViews();
        for (Services services : serviceslists) {
            addServices(services);
        }
    }
}