package com.example.wastemanagement.Notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationMain extends AppCompatActivity {

    private EditText mTitle, mMessage;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");

        ImageButton arrowBack = (ImageButton) findViewById(R.id.notificationArrowBack);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationMain.this, Dashboard.class);
                startActivity(intent);
            }
        });
        // Device id
       /* FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult();
                        System.out.println("TOKEN " + token);
                        // Log and toast
                    }
                });*/


        mTitle = findViewById(R.id.messageTitle);
        mMessage = findViewById(R.id.messageDescription);

        //Device 1 to Device 2
        findViewById(R.id.sendMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString().trim();
                String message = mMessage.getText().toString().trim();

                if(!title.equals("") && !message.equals("")){
                    FCMSend.pushNotification(
                        NotificationMain.this,
                        "cGmnJgCoTj2i9A8NGxgKrI:APA91bHJ6hrziRNwb4s5Hi7H31qf6vqSVVG7iGz23FSz6mgbi8tHmJ0tSaDRP_OGCP5Jq4d00e_AEnB019VLN1T9DW-C-FCKF5U8dPkG-O0dZCEnJjXZtJorp-XJJSaviRD6kVaAXcRO",
                        title,
                        message
                    );
                }
            }
        });
    }
}