package com.example.wastemanagement.ShopItems;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Admin.AdminDashboard_1;
import com.example.wastemanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeItem extends AppCompatActivity {
    FirebaseAuth Auth;
    FirebaseUser User;
    private ImageButton settingsArrowBack;
    private CircleImageView profileImage;
    private EditText address, itemName, itemPrice;
    private Button updateBTN;
    private Button changeProfilePic;
    private DatabaseReference databaseReference;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    ActivityUpdateDataBinding
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_item);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_ChangePassword);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeItem.this, AdminDashboard_1.class);
                startActivity(intent);
            }
        });



        updateBTN = (Button) findViewById(R.id.securityChangePassBTN);
        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    updateWithNoProfilePic();

            }
        });

        itemPrice = (EditText) findViewById(R.id.newItemPrice);
        itemName = (EditText) findViewById(R.id.newItemPrice);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ItemUser item = snapshot.getValue(ItemUser.class);
                assert item != null;

                itemPrice.setText(item.getHeader());
                itemName.setText(item.getTitle());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangeItem.this, "Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void updateWithNoProfilePic() {
        String name = itemPrice.getText().toString();
        String name1 = itemName.getText().toString();


        if (TextUtils.isEmpty(name)) {
            itemPrice.setError("Enter your new price");
            Toast.makeText(ChangeItem.this, "Please enter price", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name1)) {
            itemPrice.setError("Enter the item name");
            Toast.makeText(ChangeItem.this, "Please enter item name", Toast.LENGTH_SHORT).show();
        }else {
            updateData(name, name1);
        }
    }

    private void updateData(String name, String name1) {
        HashMap Users = new HashMap();
        Users.put("header", name);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Shop Items");
        databaseReference.child(User.getUid()).updateChildren(Users).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){

                    Toast.makeText(ChangeItem.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeItem.this, AdminDashboard_1.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChangeItem.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}