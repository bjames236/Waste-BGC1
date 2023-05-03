package com.example.wastemanagement.Admin;

import android.content.DialogInterface;
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
import com.example.wastemanagement.Call.CallActivityShop;
import com.example.wastemanagement.MapsActivity;
import com.example.wastemanagement.R;
import com.example.wastemanagement.ShopItems.AddItems;
import com.example.wastemanagement.ShopItems.ChangeItem;
import com.example.wastemanagement.UserAdminSelect;
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

public class AdminDashboard_1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private TextView name;

    private CardView TrashtoCash, AddItem, Awareness;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard1);

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
                Intent intent = new Intent(AdminDashboard_1.this, AdminViewShop.class);
                startActivity(intent);
            }
        });


        Awareness = (CardView) findViewById(R.id.dash_awareness);
        Awareness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard_1.this, ChangeItem.class);
                startActivity(intent);
            }
        });

        AddItem = (CardView) findViewById(R.id.addItems);
        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard_1.this, AddItems.class);
                startActivity(intent);
            }
        });





        navigationView.setNavigationItemSelectedListener(this);


        name = (TextView) findViewById(R.id.Admindashboard_ProfileName);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Admins").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminUsers users = snapshot.getValue(AdminUsers.class);
                assert users != null;
                name.setText("Welcome " + users.getFullname());
                profileName.setText(users.getFullname());
                profileName.setAllCaps(true);
                if(users.getProfileImage().equals("default")){
                    profileImageView.setImageResource(R.drawable.logo);
                }else{
                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profileImageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboard_1.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AdminDashboard_1.this, AdminProfile.class);
                startActivity(intent);
                break;
            case R.id.nav_change_password:
                Intent intent1 = new Intent(AdminDashboard_1.this, AdminChangePassword.class);
                startActivity(intent1);
                break;

            case R.id.nav_log_out:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminDashboard_1.this);
                builder.setTitle("")
                        .setMessage("Are you sure you want to LOG OUT? ")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent3 = new Intent(AdminDashboard_1.this, UserAdminSelect.class);
                                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Auth.signOut();
                                startActivity(intent3);
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                break;
            case R.id.nav_change_profile:
                Intent intent4 = new Intent(AdminDashboard_1.this, AdminChangeProfile.class);
                startActivity(intent4);
                break;
            case R.id.nav_maps:
                Intent intent2 = new Intent(AdminDashboard_1.this, MapsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_call:
                Intent intent5 = new Intent(AdminDashboard_1.this, CallActivityShop.class);
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
