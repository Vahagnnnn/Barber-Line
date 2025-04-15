package com.vahagn.barber_line.Fragments;

import static com.vahagn.barber_line.Activities.MainActivity.TopBarberShopsList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vahagn.barber_line.Activities.BarberShopsAboutActivity;
import com.vahagn.barber_line.Activities.BarbersActivity;
import com.vahagn.barber_line.Activities.MapActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Classes.OpeningTime;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.Classes.TimeRange;
import com.vahagn.barber_line.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


public class AboutFragment extends Fragment  implements OnMapReadyCallback {
    private MaterialCardView mondayCircle, tuesdayCircle, wednesdayCircle, thursdayCircle, fridayCircle, saturdayCircle, sundayCircle;

    private TextView mondayText, tuesdayText, wednesdayText, thursdayText,
            fridayText, saturdayText, sundayText;

    Map<String, TimeRange> openingTimes;


    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentLocationMarker;
    private GoogleMap gMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public AboutFragment() {
    }

    public AboutFragment(Map<String, TimeRange> openingTimes) {
        this.openingTimes = openingTimes;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        findViewById_setText(view);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        return view;
    }


    private void findViewById_setText(View view){
        mondayCircle = view.findViewById(R.id.mondayCircle);
        tuesdayCircle = view.findViewById(R.id.tuesdayCircle);
        wednesdayCircle = view.findViewById(R.id.wednesdayCircle);
        thursdayCircle = view.findViewById(R.id.thursdayCircle);
        fridayCircle = view.findViewById(R.id.fridayCircle);
        saturdayCircle = view.findViewById(R.id.saturdayCircle);
        sundayCircle = view.findViewById(R.id.sundayCircle);


        mondayText = view.findViewById(R.id.mondayText);
        tuesdayText = view.findViewById(R.id.tuesdayText);
        wednesdayText = view.findViewById(R.id.wednesdayText);
        thursdayText = view.findViewById(R.id.thursdayText);
        fridayText = view.findViewById(R.id.fridayText);
        saturdayText = view.findViewById(R.id.saturdayText);
        sundayText = view.findViewById(R.id.sundayText);


        mondayText.setText(getTime("Monday"));
        tuesdayText.setText(getTime("Tuesday"));
        wednesdayText.setText(getTime("Wednesday"));
        thursdayText.setText(getTime("Thursday"));
        fridayText.setText(getTime("Friday"));
        saturdayText.setText(getTime("Saturday"));
        sundayText.setText(getTime("Sunday"));
    }
    private String getTime(String day) {
        String open = Objects.requireNonNull(openingTimes.get(day)).getOpen();
        String close = Objects.requireNonNull(openingTimes.get(day)).getClose();

        if (!open.isEmpty() && !close.isEmpty()) {
            return open + " - " + close;
        } else {
            switch (day) {
                case "Monday":
                    mondayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Tuesday":
                    tuesdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Wednesday":
                    wednesdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Thursday":
                    thursdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Friday":
                    fridayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Saturday":
                    saturdayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                case "Sunday":
                    sundayCircle.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
                default:
                    Log.i("circle", "Invalid");
                    break;
            }
            return "Closed";
        }
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateLocation();

            for (BarberShops barberShop : TopBarberShopsList) {
                String[] coords = barberShop.getCoordinates().split(" ");
                String latitudeString = coords[0].replace(",", "").trim();
                String longitudeString = coords[1].replace(",", "").trim();

                try {
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);

                    LatLng location = new LatLng(latitude, longitude);

                    Log.i("img" , barberShop.getLogo());
                    addCustomMarker(location, barberShop.getName(), barberShop.getLogo());
                } catch (NumberFormatException e) {
                    Log.i("coords", "Error parsing coordinates: " + e.getMessage());
                }
            }
        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        LatLng defaultLocation = new LatLng(40.17763162763801, 44.512459783931945);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 45));
    }

    private void updateLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Executor executor = ContextCompat.getMainExecutor(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(executor, new com.google.android.gms.tasks.OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                if (currentLocationMarker != null) {
                                    currentLocationMarker.setPosition(currentLatLng);
                                } else {
                                    currentLocationMarker = gMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                                }

                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                Toast.makeText(requireContext(), "Location permission is required to show your current location on the map.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void addCustomMarker(LatLng location, String Name, String logo) {
        new AboutFragment.LoadImageTask(location, Name).execute(logo);
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final LatLng location;
        private String Name;

        public LoadImageTask(LatLng location, String Name) {
            this.location = location;
            this.Name = Name;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            String logoData = params[0];
            Bitmap originalBitmap = null;

            try {
                if (logoData != null && logoData.startsWith("data:image")) {
                    String base64String = logoData.split(",")[1];
                    byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
                    originalBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                } else if (logoData != null && !logoData.isEmpty()) {
                    originalBitmap = BitmapFactory.decodeStream(new java.net.URL(logoData).openStream());
                }

                if (originalBitmap != null) {
                    int width = 130;
                    int height = 130;
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
                    return addRoundedCornersAndBorder(resizedBitmap, 15, 10);
                } else {
                    originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 130, 130, false);
                    return addRoundedCornersAndBorder(resizedBitmap, 15, 10);
                }
            } catch (Exception e) {
                Log.e("imageEror", "Error loading image: " + e.getMessage());
                originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 130, 130, false);
                return addRoundedCornersAndBorder(resizedBitmap, 15, 10);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            BitmapDescriptor icon;
            if (bitmap != null) {
                icon = BitmapDescriptorFactory.fromBitmap(bitmap);
            } else {
                icon = BitmapDescriptorFactory.defaultMarker();
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .icon(icon)
                    .title(bitmap != null ? Name : "Default Marker");

            gMap.addMarker(markerOptions);
            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.equals(marker)) {
                        Intent intent = new Intent(requireContext(), BarberShopsAboutActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                        List<BarberShops> paragonShops = TopBarberShopsList.stream()
                                .filter(shop -> marker.getTitle().equals(shop.getName()))
                                .collect(Collectors.toList());

                        paragonShops.forEach(shop -> {
                            intent.putExtra("from_where", "MapActivity");

                            BarbersActivity.imageUrl = shop.getImage();
                            BarbersActivity.name = shop.getName();
                            BarbersActivity.rating = String.valueOf(shop.getRating());

                            if (shop.getOwnerEmail()!=null)
                            {
                                Log.i("OwnerEmail",shop.getOwnerEmail());
                            }
                            else
                                Log.i("OwnerEmail","Null");
                            BarbersActivity.OwnerEmail = shop.getOwnerEmail();

                            BarbersActivity.address = shop.getAddress();
                            BarbersActivity.ListSpecialist = shop.getSpecialists();
                            BarbersActivity.ListService = shop.getServices();
                        });

                        requireContext().startActivity(intent);

                        return true;
                    }
                    return false;
                }

            });
        }

        private Bitmap addRoundedCornersAndBorder(Bitmap bitmap, int cornerRadius, int borderWidth) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Bitmap newBitmap = Bitmap.createBitmap(width + 2 * borderWidth, height + 2 * borderWidth, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);

            paint.setShader(new LinearGradient(0, 0, width + 2 * borderWidth, height + 2 * borderWidth,
                    Color.DKGRAY, Color.LTGRAY, Shader.TileMode.CLAMP));
            RectF rectF = new RectF(0, 0, width + 2 * borderWidth, height + 2 * borderWidth);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

            paint.setShader(null);
            Bitmap roundedBitmap = getRoundedBitmap(bitmap, cornerRadius);
            canvas.drawBitmap(roundedBitmap, borderWidth, borderWidth, null);
            return newBitmap;
        }

        private Bitmap getRoundedBitmap(Bitmap bitmap, int cornerRadius) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return output;
        }
    }

}