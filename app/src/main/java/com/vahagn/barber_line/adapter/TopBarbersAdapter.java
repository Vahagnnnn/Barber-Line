package com.vahagn.barber_line.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.util.ArrayList;
import java.util.List;

import com.vahagn.barber_line.model.TopBarbers;

public class TopBarbersAdapter extends RecyclerView.Adapter<TopBarbersAdapter.TopBarbersViewHolder> {

    Context context;
    List<Barbers> TopBarbers_WithID;
    List<Barbers> TopBarbers = new ArrayList<>();

    public TopBarbersAdapter(Context context, List<Barbers> TopBarbers_WithID) {
        this.context = context;
        this.TopBarbers_WithID = TopBarbers_WithID;

        for (Barbers barbers : TopBarbers_WithID) {
            Log.i("getBarberById", "BarberId " + barbers.getBarberId());
            Log.i("getBarberById", "BarberShopsId " + barbers.getBarberShopsId());
            getBarberById(barbers.getBarberId(), barbers.getBarberShopsId());
        }

        for (Barbers barbers : TopBarbers) {
            Log.i("getBarberById", "BarberName " + barbers.getName());
//            Log.i("getBarberById", "BarberPhoneNumber " + barbers.getPhone());
        }
    }

    public void getBarberById(int barberId, int barberShopsId) {
        DatabaseReference specialistsRef = FirebaseDatabase.getInstance()
                .getReference("barberShops")
                .child(String.valueOf(barberShopsId))
                .child("specialists");

        specialistsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.i("getBarberById", "onDataChange called");
                for (DataSnapshot child : snapshot.getChildren()) {
                    Barbers barber = child.getValue(Barbers.class);
//                    barber.setBarberId(2);
//                    Log.i("getBarberById", "BarberNameFOR = " + barber.getName()+" BarberShopsId = "+ barber.getBarberShopsId()+" BarberId = "+ barber.getBarberId());
//                    Log.i("getBarberById", "barberId = " + barberId);

//                    Log.i("getBarberById", "BarberPhoneNumberFOR " + barber.getPhone());
                    if (barber != null && barberId == barber.getBarberId()) {
//                        Log.i("getBarberById", "IF " );
                        Log.i("getBarberById", "BarberNameIF = " + barber.getName()+" BarberShopsId = "+ barber.getBarberShopsId()+" BarberId = "+ barber.getBarberId());
                        TopBarbers.add(barber);
//                        Log.i("getBarberById", "ADD " );
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }


    @NonNull
    @Override
    public TopBarbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View topbarbersItems = LayoutInflater.from(context).inflate(R.layout.top_barbers, parent, false);
        return new TopBarbersViewHolder(topbarbersItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TopBarbersAdapter.TopBarbersViewHolder holder, int position) {
        Log.i("getBarberById", "onBindViewHolder " );

        for (Barbers barbers : TopBarbers) {
            Log.i("getBarberById", "BarberName " + barbers.getName());
            Log.i("getBarberById", "BarberPhoneNumber " + barbers.getPhoneNumber());
        }
        Barbers item = TopBarbers.get(position);
//        Log.i("getBarberById", "position " );

//        Log.i("getBarberById", "getImage" + item.getBarberId());
//        Log.i("getBarberById", "BarberShopsId" + item.getBarberShopsId());

//        holder.name.setText("Name");
//        holder.phone.setText("Phone");

        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhoneNumber());
//        holder.barber_image.setImageResource(item.getImage());

        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .into(holder.barber_image);
    }
    @Override
    public int getItemCount() {
        return TopBarbers.size();
    }

    public static final class TopBarbersViewHolder extends RecyclerView.ViewHolder {
        ImageView barber_image;
        TextView name, phone;

        public TopBarbersViewHolder(@NonNull View itemView) {
            super(itemView);

            barber_image = itemView.findViewById(R.id.barber_image);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}


