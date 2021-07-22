package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.mywhatsapp.Adapters.ChatAdapter;
import com.example.mywhatsapp.Models.MessageModel;
import com.example.mywhatsapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModelsList = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.textViewUserName.setText(getApplicationContext().getString(R.string.group_user_name));

        // Set adapter to a RecyclerView
        ChatAdapter chatAdapter = new ChatAdapter(messageModelsList, this);
        binding.myRecyclerView.setAdapter(chatAdapter);

        // Set LinearLayout to a RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.myRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModelsList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            messageModelsList.add(model);
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
                if(binding.EditTextMessageBox.getText().toString().isEmpty()) {
                    binding.EditTextMessageBox.setError("Please type any message.");
                    return;
                }

                final String message = binding.EditTextMessageBox.getText().toString();

                // Store latest typed message into model and also store the latest time
                final MessageModel model = new MessageModel(senderId,message);
                model.setTimeStamp(new Date().getTime());

                binding.EditTextMessageBox.setText("");

                // Whenever anything like message created, it is stored into model and then that model helps to store data in the firebase.
                // Store recently typed message into database
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });
    }
}