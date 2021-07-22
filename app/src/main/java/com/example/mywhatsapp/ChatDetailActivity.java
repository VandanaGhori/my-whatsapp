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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

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
        setContentView(binding.getRoot());

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

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String profilePic = intent.getStringExtra("profilePic");

        // Other than Logged in user is considered as a receiver
        String receiverId = intent.getStringExtra("userId");

        binding.textViewUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImage);

        // Showing and passing the data to the RecyclerView
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, receiverId);
        binding.myRecyclerView.setAdapter(chatAdapter);

        // Dynamically add the layout manager to the ChatDetailActivity for RecyclerView and showing the content into it
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.myRecyclerView.setLayoutManager(layoutManager);

        // For generating Id for particular chat in the form of ReceiverId+SenderId

        // When user is logged in and wants to use WhatsApp he/she must be sender
        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        // get data from the node name "chats"
        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // For clearing old data from the recyclerview and later it will add data with the latest typed message
                        messageModels.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            // For getting unique message Id for deleting particular message when user clicks on it.
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }

                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Showing error message on editTextView
                if (binding.EditTextMessageBox.getText().toString().isEmpty()) {
                    binding.EditTextMessageBox.setError("Please type any message.");
                    return;
                }

                String message = binding.EditTextMessageBox.getText().toString();

                // When user types a message new message has been passed to MessageModel for storing it into firebase database along with time
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimeStamp(new Date().getTime());
                binding.EditTextMessageBox.setText("");

                // getReference is used for storing anything into the database.
                // push method is used to create a node into firebase tree
                // This will save message into firebase for sender node and then after same message is store into receiver node
                database.getReference().child("chats").child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });

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