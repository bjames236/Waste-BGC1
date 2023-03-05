package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.wastemanagement.Call.CallActivity;
import com.example.wastemanagement.Home.Users;
import com.example.wastemanagement.Schedule.CalendarMain;
import com.example.wastemanagement.navigationMenuUI.Navigation_Change_Password;
import com.example.wastemanagement.navigationMenuUI.Navigation_Change_Profile;
import com.example.wastemanagement.navigationMenuUI.Profile;
import com.example.wastemanagement.trashToCash.ShopList;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private TextView name;

    private CardView TrashtoCash, Schedule, Awareness;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("WASTE-BGC");
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);
        TextView profileName = (TextView) headerView.findViewById(R.id.profileIDname);


        TrashtoCash = (CardView) findViewById(R.id.trashToCash);
        TrashtoCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ShopList.class);
                startActivity(intent);
            }
        });


        Awareness = (CardView) findViewById(R.id.dash_awareness);
        Awareness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, AwarenessMain.class);
                startActivity(intent);
            }
        });

        Schedule = (CardView) findViewById(R.id.dash_schedule);
        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, CalendarMain.class);
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(this);


        name = (TextView) findViewById(R.id.dashboard_ProfileName);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                name.setText("Welcome " + users.getUserName());
                profileName.setText(users.getUserName());
                profileName.setAllCaps(true);
                if(users.getProfileImage().equals("default")){
                    profileImageView.setImageResource(R.drawable.logo);
                }else{
                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profileImageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });




        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.date_today);
        textViewDate.setText(currentDate);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(Dashboard.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_change_profile:
                Intent intent4 = new Intent(Dashboard.this, Navigation_Change_Profile.class);
                startActivity(intent4);
                break;
            case R.id.nav_change_password:
                Intent intent1 = new Intent(Dashboard.this, Navigation_Change_Password.class);
                startActivity(intent1);
                break;

            case R.id.nav_log_out:
                Intent intent3 = new Intent(Dashboard.this, UserAdminSelect.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Auth.signOut();
                startActivity(intent3);
                finish();
                break;
            case R.id.nav_maps:
                Intent intent2 = new Intent(Dashboard.this, MapsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_call:
                Intent intent5 = new Intent(Dashboard.this, CallActivity.class);
                startActivity(intent5);
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
