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
import com.vahagn.barber_line.model.TopHaircuts;

import java.util.List;

public class TopHaircutsAdapter extends RecyclerView.Adapter<TopHaircutsAdapter.TopHaircutsViewHolder> {

    Context context;
    List<TopHaircuts> tophaircuts;

    public TopHaircutsAdapter(Context context, List<TopHaircuts> tophaircuts) {
        this.context = context;
        this.tophaircuts = tophaircuts;
    }

    @NonNull
    @Override
    public TopHaircutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tophaircutsItems = LayoutInflater.from(context).inflate(R.layout.top_haircuts, parent, false);
        return new TopHaircutsViewHolder(tophaircutsItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TopHaircutsAdapter.TopHaircutsViewHolder holder, int position) {
        TopHaircuts item = tophaircuts.get(position);
        holder.name.setText(item.getName());
        holder.haircut_image.setImageResource(item.getImageView());
    }

    @Override
    public int getItemCount() {
        return tophaircuts.size();
    }

    public static final class TopHaircutsViewHolder extends RecyclerView.ViewHolder {
        ImageView haircut_image;
        TextView name;

        public TopHaircutsViewHolder(@NonNull View itemView) {
            super(itemView);

            haircut_image = itemView.findViewById(R.id.haircut_image);
            name = itemView.findViewById(R.id.name);
        }
    }
}


