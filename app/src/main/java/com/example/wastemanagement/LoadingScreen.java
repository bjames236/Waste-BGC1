package com.example.wastemanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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


public class LoadingScreen extends AppCompatActivity {

    private TextView createAccount, forgotPassword, guest;
    private EditText emailEditText, passwordEditText;
    private Button login;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;

    FirebaseAuth Auth;
    FirebaseUser User, Admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        createAccount= (TextView) findViewById(R.id.register);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoadingScreen.this,NewUser.class);
                startActivity(intent);
            }
        });

        forgotPassword = (TextView) findViewById(R.id.forget);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoadingScreen.this, ForgotPass.class);
                startActivity(intent);
            }
        });


        //Initialize Firebase
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        Admin = Auth.getCurrentUser();

        //Progress Dialog
        loadBar = new ProgressDialog(this);

        //EditText
        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailEditText.toString().equals("admin") && passwordEditText.getText().toString().equals("admin")){
                    Toast.makeText(LoadingScreen.this, "Admin LOGIN", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { loginUser();}
        });



    }

    private void loginUser(){
        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        if(!email.matches(emailPattern)){
            emailEditText.setError("Enter correct E-mail");
        }else if (pass.isEmpty()){
            passwordEditText.setError("Enter correct Password");
        }else{
            loadBar.setTitle("Logging In...");
            loadBar.setMessage("Please wait, we are currently checking your credentials");
            loadBar.setCanceledOnTouchOutside(false);
            loadBar.show();

            Auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User = FirebaseAuth.getInstance().getCurrentUser();
                        if(User.isEmailVerified()){
                            loadBar.dismiss();
                            Intent intent = new Intent(LoadingScreen.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }else{
                            User.sendEmailVerification();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoadingScreen.this);
                            builder.setTitle("E-mail Verification")
                                    .setMessage("Check your email / spam to verify your account before Logging in.")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            loadBar.dismiss();
                                            dialog.cancel();
                                        }
                                    }).show();
                        }
                    }else{
                        loadBar.dismiss();
                        Toast.makeText(LoadingScreen.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}