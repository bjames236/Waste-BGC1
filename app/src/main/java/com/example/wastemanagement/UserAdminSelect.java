package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.wastemanagement.Admin.AdminLogin;

public class UserAdminSelect extends AppCompatActivity {

    private CardView btn_seller, btn_buyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin_select);
        btn_seller = (CardView) findViewById(R.id.adminCard);
        btn_buyer = (CardView) findViewById(R.id.userCard) ;
        btn_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openbuyer();
            }
        });

        btn_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openseller();
            }
        });
    }
    public void openseller(){
        Intent intent = new Intent(this, AdminLogin.class);
        startActivity(intent);
    }
    public void openbuyer(){
        Intent intent = new Intent(this, LoadingScreen.class);
        startActivity(intent);
    }

}