package com.example.wastemanagement.Admin;

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
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.wastemanagement.AwarenessMain;
import com.example.wastemanagement.MapsActivity;
import com.example.wastemanagement.R;
import com.example.wastemanagement.Schedule.CalendarMain;
import com.example.wastemanagement.UserAdminSelect;
import com.example.wastemanagement.navigationMenuUI.Navigation_Rate;
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

public class AdminDashboard_1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
                Intent intent = new Intent(AdminDashboard_1.this, AwarenessMain.class);
                startActivity(intent);
            }
        });

        Schedule = (CardView) findViewById(R.id.dash_schedule);
        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard_1.this, CalendarMain.class);
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
            case R.id.nav_settings:
                Intent intent1 = new Intent(AdminDashboard_1.this, MapsActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_rate_us:
                Intent intent2 = new Intent(AdminDashboard_1.this, Navigation_Rate.class);
                startActivity(intent2);
                break;
            case R.id.nav_log_out:
                Intent intent3 = new Intent(AdminDashboard_1.this, UserAdminSelect.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Auth.signOut();
                startActivity(intent3);
                finish();
                break;
        }

        return true;
    }
}
