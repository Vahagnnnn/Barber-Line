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
import com.vahagn.barber_line.model.TopBarberShops;

import java.util.List;


public class TopBarberShopsAdapter extends RecyclerView.Adapter<TopBarberShopsAdapter.TopBarberShopsViewHolder> {


    Context context;
    List<TopBarberShops> topbarbershops;

    public TopBarberShopsAdapter(Context context, List<TopBarberShops> topbarbershops) {
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
        TopBarberShops item = topbarbershops.get(position);
        holder.title.setText(item.getTitle());
        holder.address.setText(item.getAddress());
        holder.time.setText(item.getTime());
        holder.imageView.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return topbarbershops.size();
    }

    public static final class TopBarberShopsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, address, time;

        public TopBarberShopsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            time = itemView.findViewById(R.id.time);
        }
    }

}
