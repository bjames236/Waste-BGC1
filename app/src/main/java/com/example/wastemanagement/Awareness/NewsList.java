package com.example.wastemanagement.Awareness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wastemanagement.Dashboard;
import com.example.wastemanagement.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;
    private RecyclerView myList;

    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ImageButton arrowBack = (ImageButton) findViewById(R.id.arrowback_myAwareness);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (NewsList.this, Dashboard.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.AwarenessuserList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Awareness");
        FirebaseRecyclerOptions<AwarenesssUsers> options = new FirebaseRecyclerOptions.Builder<AwarenesssUsers>()
                .setQuery(databaseReference.orderByChild("title"), AwarenesssUsers.class).build();
        FirebaseRecyclerAdapter<AwarenesssUsers, AwarenessViewHolder> adapter =
                new FirebaseRecyclerAdapter<AwarenesssUsers, AwarenessViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AwarenessViewHolder houseViewHolder, int i, @NonNull AwarenesssUsers house) {
                        houseViewHolder.title.setText(house.getTitle());
                        houseViewHolder.header.setText(house.getHeader());
                        houseViewHolder.description.setText(house.getDescription());
                        Glide.with(getApplicationContext()).load(house.getImage()).into(houseViewHolder.imageView);

                        houseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(NewsList.this, AwarenessViewList.class);
                                intent.putExtra("lid", house.getLid());
                                startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public AwarenessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.awarenesslayout, parent, false);
                        AwarenessViewHolder holder = new AwarenessViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}