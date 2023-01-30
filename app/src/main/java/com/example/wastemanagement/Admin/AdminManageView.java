package com.example.wastemanagement.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminManageView extends AppCompatActivity {

    private ImageView Picture;
    private String houseID = "";

    private TextView monthlyRentTXT, houseAddressTXT, houseDescriptionTXT, postedTXT;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_view);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.AdminManagearrowback_Details);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageView.this, AdminManage.class);
                startActivity(intent);
            }
        });

        monthlyRentTXT = (TextView) findViewById(R.id.ListerRentEditText);
        houseAddressTXT = (TextView) findViewById(R.id.ListerAddressEditText);
        houseDescriptionTXT = (TextView) findViewById(R.id.ListerDescriptionEditText);
        postedTXT = (TextView) findViewById(R.id.listerdateandtimeposted);
        Picture = (ImageView) findViewById(R.id.ListerImageViews);

        houseID = getIntent().getStringExtra("hid");

        getHouseDetails(houseID);

    }

    private void getHouseDetails(String houseID) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");
        databaseReference.child(houseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    AdminShop house = snapshot.getValue(AdminShop.class);
                    monthlyRentTXT.setText(house.getMonthlyRent());
                    houseAddressTXT.setText(house.getHouseAddress());
                    houseDescriptionTXT.setText(house.getHouseDetails());
                    postedTXT.setText("Date Listed: " + house.getDate() + " : " + house.getTime());

                    Picasso.get().load(house.getImage()).into(Picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}