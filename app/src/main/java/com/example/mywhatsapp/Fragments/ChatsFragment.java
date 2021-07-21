package com.example.mywhatsapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mywhatsapp.Adapters.UsersAdapter;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    // Concept of ViewBinding
    FragmentChatsBinding binding;
    ArrayList<Users> usersArrayList = new ArrayList<>();
    FirebaseDatabase database;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        // Creating and manipulating adapter for the recyclerview of chat functionality
        UsersAdapter usersAdapter = new UsersAdapter(usersArrayList, getContext());
        binding.chatRecyclerView.setAdapter(usersAdapter);

        // Setting a layout for the recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        // fetching data from the firebase
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());

                    // Logged in user does not show into the userList's on Chats Screen
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                        usersArrayList.add(users);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG", "onCancelled: " + error.getMessage());
                Toast.makeText(getContext(), "Error found from database." + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return binding.getRoot();
    }
}