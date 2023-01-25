package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.wastemanagement.Awareness.NewsList;
import com.example.wastemanagement.Recycle.RecylceMain;

public class AwarenessMain extends AppCompatActivity {

    CardView blog, recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_main2);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myAwareness);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AwarenessMain.this, Dashboard.class);
                startActivity(intent);
            }
        });


        blog = (CardView) findViewById(R.id.card1);
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AwarenessMain.this, NewsList.class);
                startActivity(intent);
            }
        });

        recycle = (CardView) findViewById(R.id.card2);
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AwarenessMain.this, RecylceMain.class);
                startActivity(intent);
            }
        });


    }
}