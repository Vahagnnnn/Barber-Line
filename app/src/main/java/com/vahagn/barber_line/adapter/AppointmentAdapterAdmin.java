package com.vahagn.barber_line.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.vahagn.barber_line.Activities.AppointmentsAboutActivity;
import com.vahagn.barber_line.Activities.BooksActivity;
import com.vahagn.barber_line.Activities.MainActivity;
import com.vahagn.barber_line.Activities.SettingsActivity;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.R;

import java.util.List;
import java.util.Objects;


public class AppointmentAdapterAdmin extends RecyclerView.Adapter<AppointmentAdapterAdmin.AppointmentViewHolder> {
    public static String UserImageUrl;
    private List<Appointment> appointments;

    public AppointmentAdapterAdmin(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_admin, parent, false);
        return new AppointmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

//        if (appointment.getUserImageUrl() != null && !appointment.getUserImageUrl().isEmpty()) {
//            Glide.with(holder.itemView.getContext())
//                    .load(appointment.getUserImageUrl())
//                    .into(holder.UserImage);
//        }

        Glide.with(holder.itemView.getContext())
                .load(appointment.getUserImageUrl())
                .into(holder.UserImage);
        holder.UserName_Service.setText(appointment.getUserName() + " / " + appointment.getServiceName());

        String phone = appointment.getUserPhoneNumber().substring(0, 4) + " " +
               appointment.getUserPhoneNumber().substring(4, 6) + " " +
               appointment.getUserPhoneNumber().substring(6, 8) + " " +
               appointment.getUserPhoneNumber().substring(8);
        
        holder.UserPhoneNumber.setText(phone);

//        Glide.with(holder.itemView.getContext())
//                .load(appointment.getBarberImageUrl())
//                .into(holder.BarberImage);
//        holder.BarberName.setText(appointment.getBarberName());
//        holder.ServiceName.setText(appointment.getServiceName());

        holder.weekDay_monthName_dayOfMonth.setText(appointment.getWeekDay_monthName_dayOfMonth());
        holder.Time.setText(appointment.getTime());

        holder.message_or_requests.setText(
                !Objects.equals(appointment.getMessage_or_requests(), "")
                        ? appointment.getMessage_or_requests()
                        : "No requests"
        );


//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(holder.itemView.getContext(), AppointmentsAboutActivity.class);
//
//            BarbershopImageUrl = appointment.getBarbershopImageUrl();
//            intent.putExtra("BarberShopName", appointment.getBarbershopName());
//            intent.putExtra("weekDay_monthName_dayOfMonth", appointment.getWeekDay_monthName_dayOfMonth());
//            intent.putExtra("Time", appointment.getTime());
//            intent.putExtra("ServiceDuration", appointment.getServiceDuration());
//            intent.putExtra("BarberShopAddress", appointment.getBarbershopAddress());
//            intent.putExtra("BarbershopCoordinates", appointment.getBarbershopCoordinates());
//            intent.putExtra("ServiceName", appointment.getServiceName());
//
//            intent.putExtra("BarbershopRating", appointment.getBarbershopRating());
//            BooksActivity.uniqueID = appointment.getUniqueID();
//            holder.itemView.getContext().startActivity(intent);
//
//        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView UserName_Service, UserPhoneNumber, weekDay_monthName_dayOfMonth, Time,message_or_requests;
        ImageView UserImage;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            UserImage = itemView.findViewById(R.id.UserImage);
            UserName_Service = itemView.findViewById(R.id.UserName_Service);
            UserPhoneNumber = itemView.findViewById(R.id.UserPhoneNumber);

//            BarberImage = itemView.findViewById(R.id.BarberImage);
//            BarberName = itemView.findViewById(R.id.BarberName);
//            ServiceName = itemView.findViewById(R.id.ServiceName);

            weekDay_monthName_dayOfMonth = itemView.findViewById(R.id.weekDay_monthName_dayOfMonth);
            Time = itemView.findViewById(R.id.Time);

            message_or_requests = itemView.findViewById(R.id.message_or_requests);
        }
    }
}
