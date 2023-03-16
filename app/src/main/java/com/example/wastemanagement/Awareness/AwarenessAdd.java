package com.example.wastemanagement.Awareness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastemanagement.Admin.AdminManage;
import com.example.wastemanagement.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AwarenessAdd extends AppCompatActivity {
    private ImageView addHousePicture;
    private Button addBTN ;
    private TextView addImage;
    private EditText titleTXT, headerTXT, descriptionTXT, listersNameTXT, listersPhoneNumberTXT;
    private String title, header, description, listersName, listersPhoneNumber, saveCurrentDate, saveCurrentTime;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String RandomKey, downloadImageUrl;
    private StorageReference awarenessiImageRef;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingBar;
    private ImageButton back;

    private FirebaseUser User;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_add);
        loadingBar = new ProgressDialog(this);
        awarenessiImageRef = FirebaseStorage.getInstance().getReference().child("Cover Images");


        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myListing);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AwarenessAdd.this, AdminManage.class);
                startActivity(intent);
            }
        });

        addBTN = (Button) findViewById(R.id.addPost);
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        addHousePicture = (ImageView) findViewById(R.id.awarenessCover);
        addImage = (TextView) findViewById(R.id.addAwarenessBTN);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        titleTXT = (EditText) findViewById(R.id.addtitleAware);
        headerTXT = (EditText) findViewById(R.id.addHeaderAware);
        descriptionTXT = (EditText) findViewById(R.id.addDescriptionAware);


    }
    private void add() {
        title = titleTXT.getText().toString();
        header = headerTXT.getText().toString();
        description = descriptionTXT.getText().toString();
        if (ImageUri == null) {
            Toast.makeText(this, "Cover image is needed...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(title)) {
            titleTXT.setError("Required!");
        }else if (TextUtils.isEmpty(header)) {
            headerTXT.setError("Required!");
        } else if (TextUtils.isEmpty(description)) {
            descriptionTXT.setError("Required!");
        }else {
            storeToDatabase();
        }
    }
    private void storeToDatabase() {
        loadingBar.setTitle("Adding Please Wait");
        loadingBar.setMessage("Loading...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        RandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = awarenessiImageRef.child(ImageUri.getLastPathSegment() + RandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AwarenessAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            SaveInfoToDatabase();
                        }

                    }
                });
            }
        });

    }

    private void SaveInfoToDatabase() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lid", RandomKey);
        hashMap.put("image", downloadImageUrl);
        hashMap.put("title", title);
        hashMap.put("header", header);
        hashMap.put("description", description);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Awareness");
        databaseReference.child(RandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AwarenessAdd.this, AdminManage.class);
                    startActivity(intent);
                    finish();
                    loadingBar.dismiss();
                    Toast.makeText(AwarenessAdd.this, "Post has been added", Toast.LENGTH_SHORT).show();
                }else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AwarenessAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            addHousePicture.setImageURI(ImageUri);
        }
    }
}