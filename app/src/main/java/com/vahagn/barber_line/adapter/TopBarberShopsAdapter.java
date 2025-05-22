package com.vahagn.barber_line.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.Classes.BarberShops;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopBarberShopsAdapter extends RecyclerView.Adapter<TopBarberShopsAdapter.TopBarberShopsViewHolder> {
    Context context;
    List<BarberShops> topBarberShopsList;

    public TopBarberShopsAdapter(Context context, List<BarberShops> topBarberShopsList) {
        this.context = context;
        this.topBarberShopsList = topBarberShopsList;
    }

    @NonNull
    @Override
    public TopBarberShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View topbarbershopsItems = LayoutInflater.from(context).inflate(R.layout.top_barber_shops, parent, false);
        return new TopBarberShopsViewHolder(topbarbershopsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TopBarberShopsAdapter.TopBarberShopsViewHolder holder, int position) {
        BarberShops item = topBarberShopsList.get(position);
        holder.title.setText(item.getName());
        holder.address.setText(item.getAddress());
//        int imageResId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
//        holder.imageView.setImageResource(imageResId);

        String imageUrl = item.getImage();
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context, BarberShopsAboutActivity.class);
//                intent.putExtra("from_where", "MainActivity");
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                        (Activity) context,
//                        holder.imageView,
//                        "sharedImageTransition"
//                );
//                context.startActivity(intent, options.toBundle());

                Intent intent = new Intent(context, BarberShopsAboutActivity.class);
                intent.putExtra("from_where", "MainActivity");

                BarbersActivity.barberShopsId = item.getBarberShopsId();
                BarbersActivity.imageUrl = item.getImage();
                BarbersActivity.name = item.getName();
                BarbersActivity.rating = String.valueOf(item.getRating());
                BarbersActivity.OwnerEmail = item.getOwnerEmail();
                BarbersActivity.address = item.getAddress();
                BarbersActivity.logo = item.getLogo();
                BarbersActivity.coordinates = item.getCoordinates();
                BarbersActivity.ListSpecialist = item.getSpecialists();
                BarbersActivity.ListReviews = item.getReviews();
                BarbersActivity.openingTimes = item.getOpeningTimes();

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return topBarberShopsList.size();
    }

    public static final class TopBarberShopsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, address;

        public TopBarberShopsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
        }
    }

}

