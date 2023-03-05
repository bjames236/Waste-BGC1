package com.example.wastemanagement.Call;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class CallActivity extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    Button startBTN;
    EditText userIdEditText;
    TextView heyUserTextView;
    ZegoSendCallInvitationButton voiceCallBtn, videoCallBtn;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myAwareness);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (CallActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        userIdEditText = findViewById(R.id.user_id_edit_text_callactivity);
        heyUserTextView = findViewById(R.id.hey_user_text_view);
        voiceCallBtn = findViewById(R.id.voice_call_btn);
        videoCallBtn = findViewById(R.id.video_call_btn);

        startBTN = findViewById(R.id.callStartBTNActivity);


        startBTN.setOnClickListener((v)->{
            userID = heyUserTextView.getText().toString().trim();
            if(userID.isEmpty()){
                return;
            }
            //start the service
            startService(userID);
            Toast.makeText(CallActivity.this, "The service has started. Press only once.", Toast.LENGTH_SHORT).show();

        });

        heyUserTextView.setText("Hey " + userID);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                assert users != null;
                heyUserTextView.setText(users.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CallActivity.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });

        userIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String targetUserID = userIdEditText.getText().toString().trim();
                setVoiceCall(targetUserID);
                setVideoCall(targetUserID);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void setVoiceCall(String targetUserID){
        voiceCallBtn.setIsVideoCall(false);
        voiceCallBtn.setResourceID("zego_uikit_call");
        voiceCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserID)));
    }
    void setVideoCall(String targetUserID){
        videoCallBtn.setIsVideoCall(true);
        videoCallBtn.setResourceID("zego_uikit_call");
        videoCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID,targetUserID)));
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

}