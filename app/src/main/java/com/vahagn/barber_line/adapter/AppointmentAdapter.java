package com.vahagn.barber_line.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vahagn.barber_line.Classes.Appointment;
import com.vahagn.barber_line.R;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
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

        holder.shopName.setText(appointment.getBarbershopName());
//        holder.shopAddress.setText(appointment.getAddress());
//        holder.barberName.setText(appointment.getBarberName());
//        holder.service.setText(appointment.getService());
//        holder.date.setText(appointment.getDate());
//        holder.time.setText(appointment.getTime());
//
//        holder.shopImage.setImageResource(appointment.getShopImage());
//        holder.barberImage.setImageResource(appointment.getBarberImage());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress, barberName, service, date, time;
        ImageView shopImage, barberImage;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.BarberShopName);
//            shopAddress = itemView.findViewById(R.id.BarberShopAddress);
//            barberName = itemView.findViewById(R.id.txt_sargis);
//            service = itemView.findViewById(R.id.txt_address);
//            date = itemView.findViewById(R.id.txt_calendar_date);
//            time = itemView.findViewById(R.id.txt_time);
//            shopImage = itemView.findViewById(R.id.BarberShopImage);
//            barberImage = itemView.findViewById(R.id.img_barber);
        }
    }
}
