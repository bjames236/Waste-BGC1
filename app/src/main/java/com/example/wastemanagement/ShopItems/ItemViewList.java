package com.example.wastemanagement.ShopItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Awareness.AwarenesssUsers;
import com.example.wastemanagement.Awareness.NewsList;
import com.example.wastemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemViewList extends AppCompatActivity {

    private ImageView Picture;
    private String houseID = "";
    private String theuserID = "";
    private Button messageBTN, callBTN;

    private TextView titleTXT, headerTXT, descriptionTXT, listersNameTXT, listersPhoneNumberTXT, postedTXT;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view_list);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myAwareness);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ItemViewList.this, NewsList.class);
                startActivity(intent);
            }
        });

        titleTXT = (TextView) findViewById(R.id.titleview);
        headerTXT = (TextView) findViewById(R.id.headerview);
        Picture = (ImageView) findViewById(R.id.imagegholder);

        houseID = getIntent().getStringExtra("lid");

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        getHouseDetails(houseID);
    }
    private void getHouseDetails(String houseID) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Shop Items");
        databaseReference.child(houseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    AwarenesssUsers house = snapshot.getValue(AwarenesssUsers.class);
                    titleTXT.setText(house.getTitle());
                    headerTXT.setText(house.getHeader());
                    descriptionTXT.setText(house.getDescription());

                    Picasso.get().load(house.getImage()).into(Picture);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}