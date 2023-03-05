package com.example.wastemanagement.Call;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Home.Users;
import com.example.wastemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class CallMain extends AppCompatActivity {
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    EditText userIDEditText;
    Button startBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_main);

        userIDEditText = findViewById(R.id.user_id_edit_text);
        startBTN = findViewById(R.id.callStartBTN);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                userIDEditText.setText(users.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CallMain.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });
        startBTN.setOnClickListener((v)->{
            String userID = userIDEditText.getText().toString().trim();
            if(userID.isEmpty()){
                return;
            }
            //start the service
            startService(userID);
            Intent intent = new Intent(CallMain.this, CallActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);
        });
    }

    void startService(String userID){
        Application application = getApplication(); // Android's application context
        long appID = 1941288565 ;   // yourAppID
        String appSign = "f1009345ad4260fbd394b56e317692e713ad831ba203744cf9fb9aa44321c216";  // yourAppSign
        String userName = userID;

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}