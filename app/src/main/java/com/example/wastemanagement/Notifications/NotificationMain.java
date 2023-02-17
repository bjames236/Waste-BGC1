package com.example.wastemanagement.Notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.Home.Users;
import com.example.wastemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationMain extends AppCompatActivity {

    private EditText mMessage;
    private TextView mTitle;
    private Button next;
    private ProgressDialog loadingBar;

    private DatabaseReference databaseReference;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main);

        loadingBar = new ProgressDialog(this);



        databaseReference = FirebaseDatabase.getInstance().getReference().child("HouseListings");

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                mTitle.setText("FROM: " + users.getUserName() + " NO: " + users.getMobile());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotificationMain.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });
        //Device 1 to Device 2
        next = findViewById(R.id.sendMessage);
        next.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent = new Intent(NotificationMain.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                    loadingBar.dismiss();
                    Toast.makeText(NotificationMain.this, "Shop has been notified", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}