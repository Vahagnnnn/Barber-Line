package com.vahagn.barber_line.adapter;

import static androidx.core.content.ContextCompat.startActivity;

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

import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.Classes.BarberShops;

import java.io.Serializable;
import java.util.List;


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
        int imageResId = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
        holder.imageView.setImageResource(imageResId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BarberShopsAboutActivity.class);
                intent.putExtra("from_where", "MainActivity");
                intent.putExtra("image", item.getImage());
                intent.putExtra("name", item.getName());
                intent.putExtra("rating", String.valueOf(item.getRating()));
                intent.putExtra("address", item.getAddress());
                intent.putExtra("ListSpecialist", (Serializable) item.getSpecialists());
                intent.putExtra("ListService", (Serializable) item.getServices());
                context.startActivity(intent);

//                Log.d("IntentData", "from_where: BarbersActivity");
//                Log.d("IntentData", "image: " + item.getImage());
//                Log.d("IntentData", "name: " + item.getName());
//                Log.d("IntentData", "rating: " + item.getRating());
//                Log.d("IntentData", "address: " + item.getAddress());
//
//                if (item.getSpecialists() != null) {
//                    Log.d("IntentData", "ListSpecialist: " + item.getSpecialists().toString());
//                } else {
//                    Log.d("IntentData", "ListSpecialist: null");
//                }
//
//                if (item.getServices() != null) {
//                    Log.d("IntentData", "ListService: " + item.getServices().toString());
//                } else {
//                    Log.d("IntentData", "ListService: null");
//                }
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

