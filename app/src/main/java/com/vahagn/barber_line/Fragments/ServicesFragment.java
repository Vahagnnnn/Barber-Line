package com.vahagn.barber_line.Fragments;

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

        container.addView(servicesView);
    }
}