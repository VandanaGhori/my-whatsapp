package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.mywhatsapp.Adapters.ChatAdapter;
import com.example.mywhatsapp.Models.MessageModel;
import com.example.mywhatsapp.databinding.ActivityChatDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        binding.myToolbar.getNavigationIcon().setTint(Color.WHITE);
        binding.myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Authenticated that means logged in user is a sender
        final String senderId = mAuth.getUid();

        // Other than Logged in user is considered as a receiver
        String receiveId = getIntent().getStringExtra("userId");

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String profilePic = intent.getStringExtra("profilePic");

        binding.textViewUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImage);

        // Showing and passing the data to the RecyclerView
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this);
        binding.myRecyclerView.setAdapter(chatAdapter);

        // Dynamically add the layout manager to the ChatDetailActivity for RecyclerView and showing the content into it
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.myRecyclerView.setLayoutManager(layoutManager);

        setContentView(binding.getRoot());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}