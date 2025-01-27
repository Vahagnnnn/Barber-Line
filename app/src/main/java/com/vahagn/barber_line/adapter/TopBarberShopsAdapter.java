package com.vahagn.barber_line.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vahagn.barber_line.R;
import com.vahagn.barber_line.Firebase.BarberShops;

import java.util.List;


public class TopBarberShopsAdapter extends RecyclerView.Adapter<TopBarberShopsAdapter.TopBarberShopsViewHolder> {


    Context context;
    List<BarberShops> topbarbershops;

    public TopBarberShopsAdapter(Context context, List<BarberShops> topbarbershops) {
        this.context = context;
        this.topbarbershops = topbarbershops;
    }

    @NonNull
    @Override
    public TopBarberShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View topbarbershopsItems = LayoutInflater.from(context).inflate(R.layout.top_barber_shops, parent, false);
       return new TopBarberShopsViewHolder(topbarbershopsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TopBarberShopsAdapter.TopBarberShopsViewHolder holder, int position) {
        BarberShops item = topbarbershops.get(position);
        holder.title.setText(item.getName());
        holder.address.setText(item.getAddress());
        holder.imageView.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return topbarbershops.size();
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

