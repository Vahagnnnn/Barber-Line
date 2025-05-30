package com.vahagn.barber_line.Activities;

import static android.view.View.VISIBLE;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
        SpecialistActivity = getIntent().getBooleanExtra("SpecialistActivity",true);;
        SpecialistsFragment.CanBook = false;

        BarberNameTop = findViewById(R.id.BarberNameTop);
        BarberName = findViewById(R.id.BarberName);
        BarberImage = findViewById(R.id.BarberImage);
        BarberRating = findViewById(R.id.BarberRating);

        BarberNameTop.setText(SpecialistsFragment.name);
        BarberName.setText(SpecialistsFragment.name);
        BarberRating.setText(String.valueOf( SpecialistsFragment.rating));

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
            btnReject.setOnClickListener(v -> RejectBarber());

        }


    }


    private void ConfirmBarber() {
        Toast.makeText(this, "Confirmation...", Toast.LENGTH_LONG).show();
        DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
        DatabaseReference applicant_barbersShopsRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");
        Barbers newBarber = new Barbers(SpecialistsFragment.imageUrl, SpecialistsFragment.name, SpecialistsFragment.phoneNumber, SpecialistsFragment.ListServices, SpecialistsFragment.barberId, SpecialistsFragment.barberShopsId);

        barberShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot barbershopSnapshot : snapshot.getChildren()) {
                    String name = barbershopSnapshot.child("name").getValue(String.class);
                    if (name != null && name.equals(AdminActivity. myBarbershopName)) {
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

                            Log.e("specialists", newBarber.getName());

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


    private void RejectBarber() {
        EditText input = new EditText(this);
        input.setHint("Reason for rejection");

        new AlertDialog.Builder(this)
                .setTitle("Reject Barber")
                .setMessage("Please provide a reason for rejection:")
                .setView(input)
                .setPositiveButton("Reject", (dialog, which) -> {
                    String reason = input.getText().toString().trim();
                    if (!reason.isEmpty()) {
                        performRejection(reason);
                    } else {
                        Toast.makeText(this, "Rejection reason is required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void performRejection(String reason) {
        Toast.makeText(this, "Rejecting...", Toast.LENGTH_SHORT).show();
        DatabaseReference applicantRef = FirebaseDatabase.getInstance().getReference("applicant_barbers");
        DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");

        applicantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot applicantSnapshot : snapshot.getChildren()) {
                    String applicantName = applicantSnapshot.child("name").getValue(String.class);
                    String applicantEmail = applicantSnapshot.child("email").getValue(String.class);

                    if (applicantName != null && applicantName.equals(SpecialistsFragment.name) &&
                            applicantEmail != null && applicantEmail.equals(SpecialistsFragment.BarberEmail)) {

                        String joinType = SpecialistsFragment.joinType;

                        Log.e("specialists", SpecialistsFragment.joinType);
                        if (joinType != null && !joinType.equals("Send Request")) {
                            Log.e("specialists", SpecialistsFragment.joinType);
                            DatabaseReference rejectedBarberRef = rejectedRef.child(AdminActivity. myBarbershopName).push();
                            rejectedBarberRef.child("name").setValue(SpecialistsFragment.name);
                            rejectedBarberRef.child("imageUrl").setValue(SpecialistsFragment.imageUrl);
                            rejectedBarberRef.child("rating").setValue(SpecialistsFragment.rating);
                            rejectedBarberRef.child("services").setValue(SpecialistsFragment.ListServices);
                            rejectedBarberRef.child("reason").setValue(reason)
                                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Barber added to rejected_barbershops"))
                                    .addOnFailureListener(e -> Log.e("Firebase", "Failed to add to rejected_barbershops", e));
                        }

                        // Update user status + add reason
                        updateUserStatusWithReason(SpecialistsFragment.BarberEmail, "rejected", reason, () -> {
                            applicantSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SpecialistActivity.this, "Barber rejected and removed!", Toast.LENGTH_SHORT).show();
                                        Log.d("Firebase", "Barber fully rejected");
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



    private void updateUserStatusWithReason(String email, String status, String reason, Runnable onSuccess) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    if (userEmail != null && userEmail.equals(email)) {
                        userSnapshot.getRef().child("status").setValue(status);
                        userSnapshot.getRef().child("rejection_reason").setValue(reason)
//                        userSnapshot.getRef().child("myWorkplaceName").setValue(null)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firebase", "User status and reason updated");
                                    onSuccess.run();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(SpecialistActivity.this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("Firebase", "Error updating user", e);
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