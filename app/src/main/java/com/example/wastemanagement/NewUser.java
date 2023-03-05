package com.example.wastemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewUser extends AppCompatActivity {

    private EditText userName, emailAdd, password, mobile;
    private Button registerBTN, loginInstead;
    private DatabaseReference databaseReference;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;
    FirebaseAuth Auth;
    FirebaseUser User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        TextView back = (TextView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent  = new Intent (NewUser.this,LoadingScreen.class);
                startActivity(Intent);
            }
        });
        //Initialize Firebase
        Auth = FirebaseAuth.getInstance();

        //Progress Dialog
        loadBar = new ProgressDialog(this);

        userName = (EditText) findViewById(R.id.rgt_username);
        emailAdd = (EditText) findViewById(R.id.rgt_email);
        password = (EditText) findViewById(R.id.rgt_password1);
        mobile = (EditText) findViewById(R.id.rgt_mobile);


        //Button
        registerBTN = (Button) findViewById(R.id.registerButton);
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { PerformAuthentication();}
        });


    }

    private void PerformAuthentication(){
        String username = userName.getText().toString();
        String email = emailAdd.getText().toString();
        String pass = password.getText().toString();
        String mobileNum = mobile.getText().toString();
        if(!email.matches(emailPattern)){
            emailAdd.setError("Enter valid E-mail!");
            Toast.makeText(this, "Please input correct E-Mail...", Toast.LENGTH_SHORT).show();
        }else if (pass.isEmpty()){
            password.setError("Enter Password");
            Toast.makeText(this, "Please input your password...", Toast.LENGTH_SHORT).show();
        }else if (username.isEmpty()){
            userName.setError("Enter username");
            Toast.makeText(this, "Please input your username...", Toast.LENGTH_SHORT).show();
        }else if (mobileNum.isEmpty()) {
            mobile.setError("Enter your 11 digit phone number");
            Toast.makeText(this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        }
        else{
            register(email, pass, username, mobileNum);
        }
    }

    private void register(String email, String pass, String username, String mobileNum) {
        loadBar.setTitle("Creating Account");
        loadBar.setMessage("Please wait");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.show();

        Auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User = Auth.getCurrentUser();
                    assert User != null;
                    String userId = User.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("username", username);
                    hashMap.put("email", email);
                    hashMap.put("password", pass);
                    hashMap.put("mobile", mobileNum);
                    hashMap.put("profileImage", "default");
                    hashMap.put("status", "offline");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                loadBar.dismiss();
                                Intent intent = new Intent(NewUser.this, LoadingScreen.class);
                                startActivity(intent);
                                Toast.makeText(NewUser.this, "Account Created Successfull!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                loadBar.dismiss();
                                Toast.makeText(NewUser.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    loadBar.dismiss();
                    Toast.makeText(NewUser.this, "Account Created Successfull!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}