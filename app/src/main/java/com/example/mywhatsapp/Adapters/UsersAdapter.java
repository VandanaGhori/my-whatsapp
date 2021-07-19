package com.example.mywhatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.ChatDetailActivity;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyUserViewHolder> {

    ImageView image;
    TextView userName, lastMessage;
    ArrayList<Users> usersArrayList;
    Context context;

    public UsersAdapter(ArrayList<Users> usersArrayList, Context context) {
        this.usersArrayList = usersArrayList;
        this.context = context;
    }

    public class MyUserViewHolder extends RecyclerView.ViewHolder {
        public MyUserViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }

    @Override
    public MyUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_show_user,parent,false);
        return new MyUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.MyUserViewHolder holder, int position) {
        Users user = usersArrayList.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.user).into(image);
        userName.setText(user.getUserName());

        // On click event of user's chat row opens a new activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                //intent.putExtra("userId",user.getUserId());
                intent.putExtra("userName",user.getUserName());
                intent.putExtra("profilePic",user.getProfilePic());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
}
