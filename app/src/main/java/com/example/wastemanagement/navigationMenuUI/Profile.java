package com.example.wastemanagement.navigationMenuUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.R;
import com.example.wastemanagement.Home.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private TextView name, email;
    private CircleImageView profileImage;

    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back = (ImageButton) findViewById(R.id.arrowback_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Dashboard.class);
                startActivity(intent);
            }
        });

        name = (TextView) findViewById(R.id.profileName);
        email = (TextView) findViewById(R.id.profileEmailAddress);

        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                name.setText(users.getUserName());
                email.setText(User.getEmail());

                if(users.getProfileImage().equals("default")){
                    profileImage.setImageResource(R.drawable.logo);
                }else{
                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}