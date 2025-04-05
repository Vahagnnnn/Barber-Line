package com.vahagn.barber_line.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vahagn.barber_line.Activities.AppointmentsAboutActivity;
import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.BooksActivity;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.R;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
   public static String BarbershopImageUrl;
    private List<Appointment> appointments;

    public AppointmentAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        Glide.with(holder.itemView.getContext())
                .load(appointment.getBarbershopImageUrl())
                .into(holder.BarberShopImage);
        holder.BarberShopName.setText(appointment.getBarbershopName());
        holder.BarberShopAddress.setText(appointment.getBarbershopAddress());

        Glide.with(holder.itemView.getContext())
                .load(appointment.getBarberImageUrl())
                .into(holder.BarberImage);
        holder.BarberName.setText(appointment.getBarberName());
        holder.ServiceName.setText(appointment.getServiceName());

        holder.weekDay_monthName_dayOfMonth.setText(appointment.getWeekDay_monthName_dayOfMonth());
        holder.Time.setText(appointment.getTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), AppointmentsAboutActivity.class);

                BarbershopImageUrl = appointment.getBarbershopImageUrl();
                intent.putExtra("BarberShopName", appointment.getBarbershopName());
                intent.putExtra("weekDay_monthName_dayOfMonth", appointment.getWeekDay_monthName_dayOfMonth());
                intent.putExtra("Time", appointment.getTime());
                intent.putExtra("ServiceDuration", appointment.getServiceDuration());
                intent.putExtra("BarberShopAddress", appointment.getBarbershopAddress());
                intent.putExtra("BarbershopCoordinates", appointment.getBarbershopCoordinates());
                intent.putExtra("ServiceName", appointment.getServiceName());

                intent.putExtra("BarbershopRating", appointment.getBarbershopRating());
                BooksActivity.uniqueID = appointment.getUniqueID();
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView BarberShopName, BarberShopAddress , BarberName,ServiceName, weekDay_monthName_dayOfMonth, Time;
        ImageView BarberShopImage, BarberImage;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            BarberShopImage = itemView.findViewById(R.id.BarberShopImage);
            BarberShopName = itemView.findViewById(R.id.BarberShopName);
            BarberShopAddress = itemView.findViewById(R.id.BarberShopAddress);

            BarberImage = itemView.findViewById(R.id.BarberImage);
            BarberName = itemView.findViewById(R.id.BarberName);
            ServiceName = itemView.findViewById(R.id.ServiceName);

            weekDay_monthName_dayOfMonth = itemView.findViewById(R.id.weekDay_monthName_dayOfMonth);
            Time = itemView.findViewById(R.id.Time);

        }
    }
}
