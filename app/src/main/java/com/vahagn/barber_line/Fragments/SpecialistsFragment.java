package com.vahagn.barber_line.Fragments;

import static com.vahagn.barber_line.Admin.SelectBarberForSendRequestActivity.SelectBarberForSendRequestActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vahagn.barber_line.Activities.DateTimeActivity;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.Activities.SpecialistActivity;
import com.vahagn.barber_line.Admin.AddBarbersActivity;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.Applicant_BarberActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Admin.JoinToBarberShopActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialistsFragment extends Fragment {
    private LinearLayout infoContainer;
    private List<Barbers> specialists;

    public static boolean Edit,CanBook;


    public static String imageUrl, name, rating;
    public static List<Services> ListServices = new ArrayList<>();

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

    public void addSpecialist(Barbers specialist) {
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

        if (ServicesActivity.ServicesActivity) {
            specialistView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DateTimeActivity.class);
                startActivity(intent);
                name = specialist.getName();
                rating = String.valueOf(specialist.getRating());
            });
//        } else if (Applicant_BarberActivity.Is_Applicant_BarberActivity) {
//            specialistView.setOnClickListener(v -> {
//                Intent intent = new Intent(getContext(), SpecialistActivity.class);
//
//                imageUrl = specialist.getImage();
//                name = specialist.getName();
//                rating = String.valueOf(specialist.getRating());
//                ListServices = specialist.getServices();
//
//                startActivity(intent);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle(specialist.getName());
//
//                builder.setPositiveButton("Confirm", (dialog, which) -> {
//                    Toast.makeText(getContext(), "Confirmed", Toast.LENGTH_SHORT).show();
//                });
//
//
//                builder.setNeutralButton("Cancel", (dialog, which) -> dialog.cancel());
//                Toast.makeText(getContext(), "Rejected", Toast.LENGTH_SHORT).show();
//
//                builder.setNegativeButton("Reject", (dialog, which) -> {
//                    Toast.makeText(getContext(), "Rejected", Toast.LENGTH_SHORT).show();
//                });
//
//                builder.show();
//            });


        } else if (SelectBarberForSendRequestActivity) {
            specialistView.setOnClickListener(v -> {
                SelectBarberForSendRequestActivity =false;
                Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();

                Barbers Applicant_Barber = new Barbers(String.valueOf(specialist.getImage()), specialist.getName(), specialist.getPhoneNumber(), specialist.getServices(),specialist.getWorkPlace());
                AddApplicant_BarberDB(Applicant_Barber);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(currentUser.getUid());



                    Map<String, Object> updates = new HashMap<>();
                    updates.put("myWorkplaceName", specialist.getWorkPlace());
                    updates.put("myWorkplaceId", JoinToBarberShopActivity.BarbershopKeyId);
                    updates.put("myIdAsBarber", specialist.getBarberId());
                    updates.put("status", "pending");
                    userRef.updateChildren(updates);

                }

            });
        } else if (CanBook){
            specialistView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SpecialistActivity.class);

                imageUrl = specialist.getImage();
                name = specialist.getName();
                rating = String.valueOf(specialist.getRating());
                ListServices = specialist.getServices();

                startActivity(intent);
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

    private void AddApplicant_BarberDB(Barbers Applicant_Barber){
        DatabaseReference applicant_barbersShopsRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");

        applicant_barbersShopsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long newId = task.getResult().getChildrenCount();
                applicant_barbersShopsRef.child(String.valueOf(newId)).setValue(Applicant_Barber)
                        .addOnCompleteListener(storeTask -> {
                            if (storeTask.isSuccessful()) {
                                Toast.makeText(getContext(), "Store successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), AdminActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "Failed to store user data", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
