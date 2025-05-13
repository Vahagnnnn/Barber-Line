package com.vahagn.barber_line.Fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.BooksActivity;
import com.vahagn.barber_line.Activities.ConfirmActivity;
import com.vahagn.barber_line.Activities.DateTimeActivity;
import com.vahagn.barber_line.Activities.LoginActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Activities.SpecialistActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.EditProfile.EditFirstNameActivity;
import com.vahagn.barber_line.EditProfile.EditProfileActivity;
import com.vahagn.barber_line.R;

import java.util.List;
import java.util.Objects;

public class ReviewsFragment extends Fragment {
    private LinearLayout infoContainer, review_section_container;
    private List<Reviews> ListReviews;
    private Boolean isAdmin;

    private RatingBar ratingBar;
    private TextView ratingDisplay;
    private EditText review_edittext;
    private FrameLayout submitButton;

    public ReviewsFragment(List<Reviews> ListReviews, Boolean isAdmin) {
        this.ListReviews = ListReviews;
        this.isAdmin = isAdmin;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        infoContainer = view.findViewById(R.id.info_container);
        if (ListReviews != null) {
            for (Reviews review : ListReviews) {
                addReviews(review);
            }
        }
        review_section_container = view.findViewById(R.id.review_section_container);
        Log.i("from_where", String.valueOf(isAdmin));
        if (isAdmin)
            review_section_container.setVisibility(GONE);
        else
            review_section_container.setVisibility(VISIBLE);

        ratingBar = view.findViewById(R.id.ratingBar);
        ratingDisplay = view.findViewById(R.id.ratingDisplay);
        review_edittext = view.findViewById(R.id.review_edittext);
        submitButton = view.findViewById(R.id.submitButton);

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            ratingDisplay.setText("Rating: " + rating);
        });

        submitButton.setOnClickListener(v -> sentReview());

        return view;
    }

    private void sentReview() {
        if (MainActivity.isLogin) {
            float rating = ratingBar.getRating();

            if (rating == 0)
                Toast.makeText(getContext(), "Please select a Rating", Toast.LENGTH_SHORT).show();
            else if (String.valueOf(review_edittext.getText()).isEmpty())
                Toast.makeText(getContext(), "The review is empty", Toast.LENGTH_SHORT).show();
            else {
                Reviews review = new Reviews(MainActivity.userClass.getPhotoUrl(), MainActivity.userClass.getFirst_name() + " " + MainActivity.userClass.getLast_name(), String.valueOf(review_edittext.getText()), rating);
                DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");

                barberShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            BarberShops shop = snapshot.getValue(BarberShops.class);
                            if (shop != null && Objects.equals(shop.getName(), BarbersActivity.name)) {
                                String shopId = snapshot.getKey();
                                assert shopId != null;
                                Log.i("shopId", shopId);

                                DatabaseReference reviewsRef = barberShopsRef.child(shopId).child("reviews");

                                reviewsRef.get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        long newId = task.getResult().getChildrenCount();
                                        reviewsRef.child(String.valueOf(newId)).setValue(review)
                                                .addOnSuccessListener(aVoid -> {
                                                    ratingBar.setRating(0);
                                                    review_edittext.setText("");
                                                    addReviews(review);
                                                    Toast.makeText(getContext(), "Review sent successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getContext(), "Failed to send review", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("Firebase", "Failed to read value.", databaseError.toException());
                    }
                });

            }
        } else
            ToLogin();
    }

    private void addReviews(Reviews review) {
        View reviewView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_item, infoContainer, false);
        ImageView CustomerImage = reviewView.findViewById(R.id.image);
        TextView CustomerName = reviewView.findViewById(R.id.name);
        TextView ReviewText = reviewView.findViewById(R.id.review_text);
        TextView Rating = reviewView.findViewById(R.id.rating);


        if (review.getCustomerImage() != null && !review.getCustomerImage().isEmpty()) {
            Glide.with(ReviewsFragment.this)
                    .load(review.getCustomerImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                    .into(CustomerImage);
        }

        CustomerName.setText(review.getCustomerName());
        ReviewText.setText(String.valueOf(review.getReviewText()));
        Rating.setText(String.valueOf(review.getRating()));

        infoContainer.addView(reviewView);
    }

    public void ToLogin() {
        navigateTo(LoginActivity.class);
    }

    private void navigateTo(Class<?> targetActivity) {
        View sharedView = getActivity().findViewById(R.id.bottom_navigation);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(), sharedView, "sharedImageTransition");

        Intent intent = new Intent(getActivity(), targetActivity);
        startActivity(intent, options.toBundle());
    }

}