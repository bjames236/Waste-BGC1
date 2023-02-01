package com.example.wastemanagement.Admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wastemanagement.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminViewShop extends AppCompatActivity {

    private ImageButton ArrowBack;
    private TextView soldItems;
    private RecyclerView myList;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private String productID = "";
    private String theuserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_shop);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.Adminarrowback_myListing);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminViewShop.this, AdminDashboard_1.class);
                startActivity(intent);
            }
        });

        myList = (RecyclerView) findViewById(R.id.myListingsRecyclerView);
        myList.setLayoutManager(new LinearLayoutManager(AdminViewShop.this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");

        FirebaseRecyclerOptions<AdminShop> options = new FirebaseRecyclerOptions.Builder<AdminShop>()
                .setQuery(databaseReference.orderByChild("userId").equalTo(User.getUid()), AdminShop.class).build();

        FirebaseRecyclerAdapter<AdminShop, AdminViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminShop, AdminViewHolder>(options) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull AdminViewHolder houseViewHolder, int i, @NonNull AdminShop house) {
                        houseViewHolder.houseAddressLayout.setText(house.getHouseAddress());
                        houseViewHolder.monthRentPriceLayout.setText(house.getMonthlyRent());
                        houseViewHolder.houseContactLayout.setText(house.getHouseContactNumber());
                        houseViewHolder.housePostedDateLayout.setText(house.getDate());
                        houseViewHolder.listersNameLayout.setText(house.getHouseOwner());
                        houseViewHolder.statusLayout.setText(house.getStatus());
                        if (house.getStatus().equals("AVAILABLE")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#008450"));
                        }else if (house.getStatus().equals("RESERVED")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#EFB700"));
                        }else if (house.getStatus().equals("RENTED")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#B81D13"));
                        }

                        Glide.with(getApplicationContext()).load(house.getImage()).into(houseViewHolder.imageView);

                        houseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String [] sortOptions = {"View", "Mark: CLOSED", "Mark: OPEN"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewShop.this);
                                builder.setTitle(house.getHouseAddress());
                                builder.setIcon(R.drawable.logo);
                                builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which==0){
                                            dialog.dismiss();
                                            Intent intent = new Intent(AdminViewShop.this, AdminViewShopDetails.class);
                                            intent.putExtra("userId", house.getUserId());
                                            intent.putExtra("hid", house.getLid());
                                            startActivity(intent);

                                        }else if (which==1){
                                            dialog.dismiss();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminViewShop.this);
                                            builder.setTitle("")
                                                    .setMessage("Do you want to mark this Shop as CLOSED?")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            HashMap houseMap = new HashMap();
                                                            houseMap.put("status", "CLOSED");
                                                            Auth = FirebaseAuth.getInstance();
                                                            User = Auth.getCurrentUser();

                                                            databaseReference = FirebaseDatabase.getInstance().getReference("HouseListings");
                                                            databaseReference.child(house.getLid()).updateChildren(houseMap);

                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();

                                        }else if (which==2){
                                            dialog.dismiss();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminViewShop.this);
                                            builder.setTitle("")
                                                    .setMessage("Do you want to mark this listing as OPEN again?")
                                                    .setCancelable(true)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Calendar calendar = Calendar.getInstance();

                                                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                            String saveCurrentDate = currentDate.format(calendar.getTime());

                                                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                                            String saveCurrentTime = currentTime.format(calendar.getTime());

                                                            HashMap houseMap = new HashMap();
                                                            houseMap.put("status", "OPEN");
                                                            houseMap.put("date", saveCurrentDate);
                                                            houseMap.put("time", saveCurrentTime);
                                                            Auth = FirebaseAuth.getInstance();
                                                            User = Auth.getCurrentUser();

                                                            databaseReference = FirebaseDatabase.getInstance().getReference("HouseListings");
                                                            databaseReference.child(house.getLid()).updateChildren(houseMap);

                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }
                                    }
                                });
                                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoplayout, parent, false);
                        AdminViewHolder holder = new AdminViewHolder(view);
                        return holder;
                    }
                };
        myList.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myList.setLayoutManager(linearLayoutManager);

    }

}