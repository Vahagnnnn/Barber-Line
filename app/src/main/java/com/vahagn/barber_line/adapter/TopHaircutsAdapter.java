package com.vahagn.barber_line.adapter;

import android.content.Context;
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
import com.vahagn.barber_line.Fragments.ReviewsFragment;
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


        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                    .into(holder.haircut_image);
        }


//        holder.haircut_image.setImageResource(item.getImage());
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


