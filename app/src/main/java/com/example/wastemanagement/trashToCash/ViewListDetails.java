package com.example.wastemanagement.trashToCash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Admin.AdminShop;
import com.example.wastemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewListDetails extends AppCompatActivity {

    private ImageView Picture;
    private String houseID = "";
    private String theuserID = "";
    private Button messageBTN, callBTN;

    private TextView monthlyRentTXT, houseAddressTXT, houseDescriptionTXT, listersNameTXT, listersPhoneNumberTXT, postedTXT;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_details);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.Adminarrowback_Details);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewListDetails.this, ShopList.class);
                startActivity(intent);
            }
        });

        monthlyRentTXT = (TextView) findViewById(R.id.RentEditText);
        houseAddressTXT = (TextView) findViewById(R.id.AddressEditText);
        houseDescriptionTXT = (TextView) findViewById(R.id.DescriptionEditText);
        listersNameTXT = (TextView) findViewById(R.id.NameInfoEditText);
        listersPhoneNumberTXT = (TextView) findViewById(R.id.PhoneInfoEditText);
        postedTXT = (TextView) findViewById(R.id.dateandtimeposted);
        Picture = (ImageView) findViewById(R.id.ImageViews);

        houseID = getIntent().getStringExtra("lid");

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        getHouseDetails(houseID);

    }

    private void getHouseDetails(String houseID) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");
        databaseReference.child(houseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    AdminShop house = snapshot.getValue(AdminShop.class);
                    monthlyRentTXT.setText("Address: " +house.getMonthlyRent());
                    houseAddressTXT.setText("Name: "+house.getHouseAddress());
                    houseDescriptionTXT.setText("Description: "+house.getHouseDetails());
                    listersNameTXT.setText(house.getHouseOwner());
                    listersPhoneNumberTXT.setText(house.getHouseContactNumber());
                    postedTXT.setText("Date Listed: " + house.getDate() + " : " + house.getTime());

                    Picasso.get().load(house.getImage()).into(Picture);

                    if (house.getUserId().equals(User.getUid())){
                        messageBTN.setVisibility(View.INVISIBLE);
                    }

                    messageBTN = (Button) findViewById(R.id.messageButton);
                    messageBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri SendMessage = Uri.parse("smsto:" + house.getHouseContactNumber());
                            Intent intent = new Intent(Intent.ACTION_SENDTO, SendMessage);
                            startActivity(intent);
                        }
                    });

                    callBTN = (Button) findViewById(R.id.callButton);
                    callBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", house.getHouseContactNumber(), null));
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}