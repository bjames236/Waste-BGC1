package com.example.wastemanagement.Awareness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Admin.AdminDashboard_1;
import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.R;

public class AwarenessFt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_ft);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_awareness);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AwarenessFt.this, Dashboard.class);
                startActivity(intent);
                Intent intent1 = new Intent (AwarenessFt.this, AdminDashboard_1.class);
                startActivity(intent1);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new recfragment()).commit();


    }
}