package com.vahagn.barber_line;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BarberShops_Gray extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barbershops_gray);
        Toast.makeText(this, "barbershops_gray", Toast.LENGTH_SHORT).show();
    }
}
