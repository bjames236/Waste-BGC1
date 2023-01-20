package com.example.wastemanagement.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.LoadingScreen;
import com.example.wastemanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminAdd extends AppCompatActivity {

    private TextView loginInstead;
    private EditText emailAdd, password, confirmPassword, phoneNumber, houseAddress,fullName;
    private Button registerBtn;
    private DatabaseReference databaseReference;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;
    FirebaseAuth Auth;
    FirebaseUser User, SellerAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        loginInstead = (TextView) findViewById(R.id.rgt_logininstead);
        loginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAdd.this, AdminLogin.class);
                startActivity(intent);
            }
        });

        //Initialize firebase
        Auth = FirebaseAuth.getInstance();

        //Progress Dialog
        loadBar = new ProgressDialog(this);

        emailAdd = (EditText) findViewById(R.id.rgt_emailAddress);
        password = (EditText) findViewById(R.id.rgt_password);
        confirmPassword = (EditText) findViewById(R.id.rgt_confirmpassword);
        phoneNumber = (EditText) findViewById(R.id.rgt_phoneNumber);
        fullName = (EditText) findViewById(R.id.rgt_fullname);

        //Button
        registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuthentication();
            }
        });

    }

    private void PerformAuthentication() {
        String email = emailAdd.getText().toString();
        String pass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();
        String phone = phoneNumber.getText().toString();
        String name = fullName.getText().toString();

        if (!email.matches(emailPattern)) {
            emailAdd.setError("Enter correct E-mail!");
            Toast.makeText(this, "Please input correct E-Mail...", Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            password.setError("Enter Password!");
            Toast.makeText(this, "Please input your password...", Toast.LENGTH_SHORT).show();
        } else if (! pass.equals(confirmPass)) {
            confirmPassword.setError("Password does not match!");
            Toast.makeText(this,"Password does not match...", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            fullName.setError("Enter your full name");
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        } else {
            register(email, pass, phone, name);
        }

    }

    private void register(String email, String pass, String phone, String name) {
        loadBar.setTitle("Creating Account");
        loadBar.setMessage("Please wait, we are currently checking your credentials.");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.show();

        Auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    SellerAcc = Auth.getCurrentUser();
                    assert SellerAcc != null;
                    String userId =SellerAcc.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Admins").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("email", email);
                    hashMap.put("password", pass);
                    hashMap.put("phoneNumber", phone);
                    hashMap.put("fullname", name);
                    hashMap.put("profileImage", "default");
                    hashMap.put("status", "offline");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadBar.dismiss();
                                Intent intent = new Intent(AdminAdd.this, AdminAddShop.class);
                                startActivity(intent);
                                Toast.makeText(AdminAdd.this, "Account Created Successfull!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                loadBar.dismiss();
                                Toast.makeText(AdminAdd.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    loadBar.dismiss();
                    Toast.makeText(AdminAdd.this, "Account Created Successfull!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}