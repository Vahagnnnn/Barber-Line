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

import java.util.List;

import com.vahagn.barber_line.model.TopBarbers;

public class TopBarbersAdapter extends RecyclerView.Adapter<TopBarbersAdapter.TopBarbersViewHolder> {

    Context context;
    List<TopBarbers> topbarbers;

    public TopBarbersAdapter(Context context, List<TopBarbers> topbarbers) {
        this.context = context;
        this.topbarbers = topbarbers;
    }

    @NonNull
    @Override
    public TopBarbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View topbarbersItems = LayoutInflater.from(context).inflate(R.layout.top_barbers, parent, false);
        return new TopBarbersViewHolder(topbarbersItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TopBarbersAdapter.TopBarbersViewHolder holder, int position) {
        TopBarbers item = topbarbers.get(position);
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());
        holder.barber_image.setImageResource(item.getImageView());
    }

    @Override
    public int getItemCount() {
        return topbarbers.size();
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


