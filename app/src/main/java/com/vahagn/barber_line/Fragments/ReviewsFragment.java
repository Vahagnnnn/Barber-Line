package com.vahagn.barber_line.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vahagn.barber_line.Activities.DateTimeActivity;
import com.vahagn.barber_line.Activities.ServicesActivity;
import com.vahagn.barber_line.Activities.SpecialistActivity;
import com.vahagn.barber_line.Admin.CreateBarberShopActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.R;

import java.util.List;

public class ReviewsFragment extends Fragment {
    private LinearLayout infoContainer;
    private List<Reviews> ListReviews;


    public ReviewsFragment(List<Reviews> ListReviews) {
        this.ListReviews = ListReviews;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_reviews, container, false);

        infoContainer = view.findViewById(R.id.info_container);
        if (ListReviews != null) {
            for (Reviews review : ListReviews) {
                addReviews(review);
            }
        }
        return view;
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
}