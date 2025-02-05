package com.vahagn.barber_line.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Fragments.ReviewsFragment;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories;
    private List<Barbers> specialists;
    private FragmentManager fragmentManager;

    public CategoryAdapter(Context context, List<Category> categories, List<Barbers> specialists, FragmentManager fragmentManager) {
        this.context = context;
        this.categories = categories;
        this.specialists = specialists;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoriesItems = LayoutInflater.from(context).inflate(R.layout.categories, parent, false);
        return new CategoryViewHolder(categoriesItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category item = categories.get(position);
        holder.name.setText(item.getTitle());
        holder.image.setImageResource(item.getImage());
        holder.backgorund.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(item.getColor())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                String categoryName = item.getTitle();

                for (int i = 0; i < categories.size(); i++) {
                    categories.get(i).setColor("#242C3B");
                }
                item.setColor("#EDEFFB");

                notifyDataSetChanged();
                switch (categoryName) {
                    case "Specialists":
                        Fragment specialistsFragment = new SpecialistsFragment(specialists);
                        transaction.replace(R.id.specialists_container, specialistsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case "Services":
                        Fragment servicesFragment = new ServicesFragment();
                        transaction.replace(R.id.specialists_container, servicesFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case "Reviews":
                        Fragment reviewsFragment = new ReviewsFragment();
                        transaction.replace(R.id.specialists_container, reviewsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    default:
                        Toast.makeText(context, "Unknown category: " + categoryName, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        LinearLayout backgorund;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            backgorund = itemView.findViewById(R.id.backgorund);
        }
    }
}
