package com.vahagn.barber_line.SuperAdmin;

//    public static String imageUrl, name, logo, rating, address, coordinates;
//    public static List<Barbers> ListSpecialist = new ArrayList<>();
//    public static List<Reviews> ListReviews = new ArrayList<>();
//    public static Map<String, TimeRange> openingTimes = new HashMap<>();


public class AdminSettingTest {

    //    DatabaseReference barberShopsRef = FirebaseDatabase.getInstance().getReference("barberShops");
//    DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("pending_barbershops");
//    DatabaseReference rejectedRef = FirebaseDatabase.getInstance().getReference("rejected_barbershops");


    //
//    LinearLayout wait_for_confirmation;


//    public static String workPlace;

    //
//    String ownerEmail;
//
//    TextView List_TextView;
//    LinearLayout barbershops_list;
//    private ProgressBar loadingProgressBar;
//
}


//    public void AddConfirm(View view) {
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        List_TextView.setText("Operating Barbershops");
//        barbershops_list.removeAllViews();
//        barberShopsRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                barbershops_list.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BarberShops shop = snapshot.getValue(BarberShops.class);
//                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(),shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
//                        workPlace = shop.getName();
//                    }
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }

//    public void AddWait(View view) {
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        List_TextView.setText("Pending Barbershops");
//        barbershops_list.removeAllViews();
//
//        pendingRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                barbershops_list.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BarberShops shop = snapshot.getValue(BarberShops.class);
//                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(),shop.getCoordinates(), shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes());
//                    }
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }

//    public void AddRejected(View view) {
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        List_TextView.setText("Rejected Barbershops");
//        barbershops_list.removeAllViews();
//
//        rejectedRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                barbershops_list.removeAllViews();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BarberShops shop = snapshot.getValue(BarberShops.class);
//                    if (shop != null && Objects.equals(shop.getOwnerEmail(), ownerEmail)) {
//                        addBarbershop(shop.getLogo(), shop.getImage(), shop.getName(), shop.getRating(), shop.getAddress(), shop.getCoordinates(),shop.getSpecialists(), shop.getReviews(), shop.getOpeningTimes(), shop.getReason());
//                        List_TextView.setText("Rejected Barbershops");
//                    }
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("Firebase", "Failed to read value.", databaseError.toException());
//                loadingProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }


//    public void addBarbershop(String logo, String imageUrl, String name, double rating, String address,String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes) {
//        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, barbershops_list, false);
//        ImageView logoImageView = barbershopView.findViewById(R.id.logo);
//
//        Glide.with(this)
//                .load(logo)
//                .into(logoImageView);
//
//        TextView nameTextView = barbershopView.findViewById(R.id.name);
//        TextView addressTextView = barbershopView.findViewById(R.id.address);
//
//        nameTextView.setText(name);
//        addressTextView.setText(address);
//
//
//        barbershopView.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//
//            AdminSettingsActivity.imageUrl = imageUrl;
//            AdminSettingsActivity.name = name;
//            AdminSettingsActivity.logo = logo;
//            AdminSettingsActivity.rating = String.valueOf(rating);
//            AdminSettingsActivity.address = address;
//            AdminSettingsActivity.coordinates = coordinates;
//            AdminSettingsActivity.ListSpecialist = ListSpecialist;
//            AdminSettingsActivity.ListReviews = ListReviews;
//            AdminSettingsActivity.openingTimes = openingTimes;
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                    this,
//                    findViewById(R.id.bottom_navigation),
//                    "sharedImageTransition");
//            startActivity(intent, options.toBundle());
//        });
//
////        barbershops_list.removeAllViews();
//        barbershops_list.addView(barbershopView);
//    }

//    public void addBarbershop(String logo, String imageUrl, String name, double rating, String address, String coordinates, List<Barbers> ListSpecialist, List<Reviews> ListReviews, Map<String, TimeRange> openingTimes, String reason) {
//        View barbershopView = LayoutInflater.from(this).inflate(R.layout.barbershops_gray, barbershops_list, false);
//        ImageView logoImageView = barbershopView.findViewById(R.id.logo);
//
//        Glide.with(this)
//                .load(logo)
//                .into(logoImageView);
//
//        TextView nameTextView = barbershopView.findViewById(R.id.name);
//        TextView addressTextView = barbershopView.findViewById(R.id.address);
//
//        nameTextView.setText(name);
////        nameTextView.setText(reason);
//        addressTextView.setText(address);
//
//
//        barbershopView.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AdminBarberShopsAboutActivity.class);
//
//            AdminSettingsActivity.imageUrl = imageUrl;
//            AdminSettingsActivity.name = name;
//            AdminSettingsActivity.logo = logo;
//            AdminSettingsActivity.rating = String.valueOf(rating);
//            AdminSettingsActivity.coordinates = coordinates;
//            AdminSettingsActivity.address = address;
//            AdminSettingsActivity.ListSpecialist = ListSpecialist;
//            AdminSettingsActivity.ListReviews = ListReviews;
//            AdminSettingsActivity.openingTimes = openingTimes;
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
//                    this,
//                    findViewById(R.id.bottom_navigation),
//                    "sharedImageTransition");
//            startActivity(intent, options.toBundle());
//        });
////        barbershops_list.removeAllViews();
//        barbershops_list.addView(barbershopView);
//    }
