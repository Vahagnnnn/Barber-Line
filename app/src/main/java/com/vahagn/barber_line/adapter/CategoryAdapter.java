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
import com.vahagn.barber_line.Activities.BooksActivity;
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.OpeningTime;
import com.vahagn.barber_line.Classes.Reviews;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.Fragments.AboutFragment;
import com.vahagn.barber_line.Fragments.ReviewsFragment;
import com.vahagn.barber_line.Fragments.ServicesFragment;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;
import com.vahagn.barber_line.model.Category;

import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories;
    private List<Barbers> ListSpecialist;
    private List<Services> ListService;
    private List<Reviews> ListReviews;
    private  Map<String, TimeRange> openingTimes;
    private  String coordinates,name,logo;
    private FragmentManager fragmentManager;
    private Boolean isAdmin;

    public CategoryAdapter(Context context, List<Category> categories, List<Barbers> ListSpecialist, List<Services> ListService, List<Reviews> ListReviews,Map<String, TimeRange> openingTimes, String coordinates, String name, String logo,FragmentManager fragmentManager,Boolean isAdmin) {
        this.context = context;
        this.categories = categories;
        this.ListSpecialist = ListSpecialist;
        this.ListService = ListService;
        this.ListReviews = ListReviews;
        this.openingTimes = openingTimes;
        this.coordinates = coordinates;
        this.name = name;
        this.logo = logo;
        this.fragmentManager = fragmentManager;
        this.isAdmin = isAdmin;
    }
//    public CategoryAdapter(Context context, List<Category> categories, List<Barbers> ListSpecialist, List<Services> ListService, FragmentManager fragmentManager) {
//        this.context = context;
//        this.categories = categories;
//        this.ListSpecialist = ListSpecialist;
//        this.ListService = ListService;
//        this.fragmentManager = fragmentManager;
//    }
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
                        Fragment specialistsFragment = new SpecialistsFragment(ListSpecialist);
                        transaction.replace(R.id.info_container, specialistsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case "Services":
                        Fragment servicesFragment = new ServicesFragment(ListService);
                        transaction.replace(R.id.info_container, servicesFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Log.i("getServices",ListService.toString());
                        break;
                    case "Reviews":
                        Fragment reviewsFragment = new ReviewsFragment(ListReviews,isAdmin);
                        transaction.replace(R.id.info_container, reviewsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case "About":
                        Fragment aboutFragment = new AboutFragment(openingTimes,coordinates,name,logo);
                        transaction.replace(R.id.info_container, aboutFragment);
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
