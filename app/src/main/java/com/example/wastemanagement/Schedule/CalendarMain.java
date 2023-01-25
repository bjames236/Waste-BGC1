package com.example.wastemanagement.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Admin.AdminDashboard_1;
import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.R;

public class CalendarMain extends AppCompatActivity
{

    CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_schedule);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (CalendarMain.this, Dashboard.class);
                startActivity(intent);

            }
        });
        ImageButton arrowBack1 = (ImageButton) findViewById(R.id.arrowback_schedule);
        arrowBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarMain.this, AdminDashboard_1.class);
                startActivity(intent);
            }
        });

        customCalendarView = (CustomCalendarView) findViewById(R.id.custom_calendar_view);
    }

}
