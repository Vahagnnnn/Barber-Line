package com.vahagn.barber_line.Activities;

import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.AdminSettingsActivity;
import com.vahagn.barber_line.Admin.Applicant_BarberActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialistActivity extends AppCompatActivity {
    TextView BarberNameTop, BarberName, BarberRating;
    ImageView BarberImage;

    public static boolean SpecialistActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist);

        SpecialistActivity = true;

        BarberNameTop = findViewById(R.id.BarberNameTop);
        BarberName = findViewById(R.id.BarberName);
        BarberImage = findViewById(R.id.BarberImage);
        BarberRating = findViewById(R.id.BarberRating);

        BarberNameTop.setText(SpecialistsFragment.name);
        BarberName.setText(SpecialistsFragment.name);
        BarberRating.setText(SpecialistsFragment.rating);

//        if (SpecialistsFragment.imageUrl != null) {
//            BarberImage.setImageResource();
//            setImageFromBase64(SpecialistsFragment.imageUrl, BarberImage);
//        }

//        if (SpecialistsFragment.imageUrl != null && !SpecialistsFragment.imageUrl.isEmpty()) {
//            Glide.with(SpecialistActivity.this)
//                    .load(SpecialistsFragment.imageUrl)
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
//                    .into(BarberImage);
//        }

        if (SpecialistsFragment.imageUrl != null && !SpecialistsFragment.imageUrl.isEmpty()) {
            Glide.with(SpecialistActivity.this)
                    .load(SpecialistsFragment.imageUrl)
                    .into(BarberImage);
        }

        ServicesFragment servicesFragment = new ServicesFragment(SpecialistsFragment.ListServices);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.info_container, servicesFragment);
        transaction.commit();


        if (Applicant_BarberActivity.Is_Applicant_BarberActivity) {
            LinearLayout confirm_reject_layout = findViewById(R.id.confirm_reject_layout);
            MaterialButton btnConfirm, btnReject;
            btnConfirm = findViewById(R.id.btn_confirm);
            btnReject = findViewById(R.id.btn_reject);
            confirm_reject_layout.setVisibility(VISIBLE);

            btnConfirm.setOnClickListener(v -> ConfirmBarber());

        }


    }

//    private void ConfirmBarber() {
//        Toast.makeText(this, "Wait...", Toast.LENGTH_LONG).show();
//        DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
//        DatabaseReference applicant_barbersShopsRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");
//        Barbers newBarber = new Barbers(SpecialistsFragment.imageUrl, SpecialistsFragment.name, SpecialistsFragment.rating, SpecialistsFragment.ListServices);
//
//        barberShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot barbershopSnapshot : snapshot.getChildren()) {
//                    String name = barbershopSnapshot.child("name").getValue(String.class);
//                    if (name != null && name.equals(AdminSettingsActivity.workPlace)) {
//                        List<Barbers> specialists = new ArrayList<>();
//                        DataSnapshot specialistsSnapshot = barbershopSnapshot.child("specialists");
//                        for (DataSnapshot specialistSnapshot : specialistsSnapshot.getChildren()) {
//                            Barbers specialist = specialistSnapshot.getValue(Barbers.class);
//                            if (specialist != null) {
//                                specialists.add(specialist);
//                            }
//                        }
//
//                        specialists.add(newBarber);
//
//                        barbershopSnapshot.getRef().child("specialists").setValue(specialists)
//                                .addOnSuccessListener(aVoid -> {
//                                    applicant_barbersShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            for (DataSnapshot applicantSnapshot : snapshot.getChildren()) {
//                                                String applicantName = applicantSnapshot.child("name").getValue(String.class);
//                                                if (applicantName != null && applicantName.equals(newBarber.getName())) {
//                                                    applicantSnapshot.getRef().removeValue()
//                                                            .addOnSuccessListener(aVoid2 -> {
//                                                                Toast.makeText(SpecialistActivity.this, "Barber added and removed from applicants successfully!", Toast.LENGTH_SHORT).show();
//                                                                Log.d("Firebase", "Barber added to " + AdminSettingsActivity.workPlace + " and removed from applicants");
//                                                                ToAdminSettings();
//                                                            })
//                                                            .addOnFailureListener(e -> {
//                                                                Toast.makeText(SpecialistActivity.this, "Failed to remove barber from applicants: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                Log.e("Firebase", "Error removing barber from applicants", e);
//                                                            });
//                                                    break;
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                            Toast.makeText(SpecialistActivity.this, "Database error while removing applicant: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                                            Log.e("Firebase", "Database error", error.toException());
//                                        }
//                                    });
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(SpecialistActivity.this, "Failed to add barber: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    Log.e("Firebase", "Error adding barber", e);
//                                });
//
//
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(SpecialistActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("Firebase", "Database error", error.toException());
//            }
//        });
//    }


    private void ConfirmBarber() {
        Toast.makeText(this, "Wait...", Toast.LENGTH_LONG).show();
        DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
        DatabaseReference applicant_barbersShopsRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");
        Barbers newBarber = new Barbers(SpecialistsFragment.imageUrl, SpecialistsFragment.name, SpecialistsFragment.rating, SpecialistsFragment.ListServices);

        barberShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot barbershopSnapshot : snapshot.getChildren()) {
                    String name = barbershopSnapshot.child("name").getValue(String.class);
                    if (name != null && name.equals(AdminSettingsActivity.workPlace)) {
                        String joinType = SpecialistsFragment.joinType;

                        if (joinType != null && !joinType.equals("Send Request")) {
                            List<Barbers> specialists = new ArrayList<>();
                            DataSnapshot specialistsSnapshot = barbershopSnapshot.child("specialists");
                            for (DataSnapshot specialistSnapshot : specialistsSnapshot.getChildren()) {
                                Barbers specialist = specialistSnapshot.getValue(Barbers.class);
                                if (specialist != null) {
                                    specialists.add(specialist);
                                }
                            }

                            specialists.add(newBarber);

                            // Обновляем список specialists
                            barbershopSnapshot.getRef().child("specialists").setValue(specialists)
                                    .addOnSuccessListener(aVoid -> removeFromApplicants(applicant_barbersShopsRef, newBarber))
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SpecialistActivity.this, "Failed to add barber: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("Firebase", "Error adding barber", e);
                                    });
                        } else {
                            // Только удаляем из applicants
                            removeFromApplicants(applicant_barbersShopsRef, newBarber);
                        }

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecialistActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Database error", error.toException());
            }
        });
    }

    private void removeFromApplicants(DatabaseReference applicantRef, Barbers barber) {
        applicantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot applicantSnapshot : snapshot.getChildren()) {
                    String applicantName = applicantSnapshot.child("name").getValue(String.class);
                    String applicantEmail = applicantSnapshot.child("email").getValue(String.class);

                    if (applicantName != null && applicantName.equals(barber.getName()) &&
                            applicantEmail != null && applicantEmail.equals(SpecialistsFragment.BarberEmail)) {

                        // Сначала обновляем статус в Users
                        updateUserStatus(SpecialistsFragment.BarberEmail, "confirmed", () -> {
                            // После успешного обновления — удаляем из applicant_barbers
                            applicantSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SpecialistActivity.this, "Barber confirmed and removed from applicants!", Toast.LENGTH_SHORT).show();
                                        Log.d("Firebase", "Barber status updated in Users and removed from applicants");
                                        ToAdminSettings();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SpecialistActivity.this, "Failed to remove barber: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("Firebase", "Error removing barber", e);
                                    });
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecialistActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Database error", error.toException());
            }
        });
    }

    private void updateUserStatus(String email, String status, Runnable onSuccess) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    if (userEmail != null && userEmail.equals(email)) {
                        userSnapshot.getRef().child("status").setValue(status)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firebase", "User status updated successfully");
                                    onSuccess.run();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SpecialistActivity.this, "Failed to update user status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("Firebase", "Error updating user status", e);
                                });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecialistActivity.this, "Database error while updating user: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Database error", error.toException());
            }
        });
    }



    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToAdminSettings() {
        navigateTo(AdminSettingsActivity.class);
    }

    public void TobAboutBarberShop(View view) {
        SpecialistActivity = false;
        ServicesActivity.ServicesActivity = false;
        onBackPressed();
    }
}