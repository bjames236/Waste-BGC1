package com.example.wastemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.wastemanagement.Admin.AdminLogin;
import com.example.wastemanagement.Admin.AdminManageLogin;

public class UserAdminSelect extends AppCompatActivity {

    private CardView btn_seller, btn_buyer;

    private ImageView admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin_select);
        btn_seller = (CardView) findViewById(R.id.adminCard);
        btn_buyer = (CardView) findViewById(R.id.userCard) ;
        admin = (ImageView) findViewById(R.id.imageAdminSelect);
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
        admin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent ( UserAdminSelect.this, AdminManageLogin.class);
                startActivity(intent);
                return false;
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