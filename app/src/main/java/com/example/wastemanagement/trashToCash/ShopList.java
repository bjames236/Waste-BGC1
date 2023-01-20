package com.example.wastemanagement.trashToCash;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wastemanagement.Admin.AdminShop;
import com.example.wastemanagement.Admin.AdminViewHolder;
import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.MapsActivity;
import com.example.wastemanagement.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopList extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;
    private RecyclerView myList;

    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myListing);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ShopList.this, Dashboard.class);
                startActivity(intent);
            }
        });
        ImageButton maps = (ImageButton) findViewById(R.id.mapsButton);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ShopList.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");
        FirebaseRecyclerOptions<AdminShop> options = new FirebaseRecyclerOptions.Builder<AdminShop>()
                .setQuery(databaseReference.orderByChild("status").equalTo("OPEN"), AdminShop.class).build();
        FirebaseRecyclerAdapter<AdminShop, AdminViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminShop, AdminViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminViewHolder houseViewHolder, int i, @NonNull AdminShop house) {
                        houseViewHolder.houseAddressLayout.setText(house.getHouseAddress());
                        houseViewHolder.monthRentPriceLayout.setText(house.getMonthlyRent());
                        houseViewHolder.houseContactLayout.setText(house.getHouseContactNumber());
                        houseViewHolder.housePostedDateLayout.setText(house.getDate());
                        houseViewHolder.listersNameLayout.setText("By: " + house.getHouseOwner());
                        houseViewHolder.statusLayout.setText(house.getStatus());
                        if (house.getStatus().equals("OPEN")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#008450"));
                        }else if (house.getStatus().equals("CLOSED")){
                            houseViewHolder.statusLayout.setTextColor(Color.parseColor("#EFB700"));
                        }
                        Glide.with(getApplicationContext()).load(house.getImage()).into(houseViewHolder.imageView);

                        houseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShopList.this, ViewListDetails.class);
                                intent.putExtra("lid", house.getLid());
                                startActivity(intent);
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}


