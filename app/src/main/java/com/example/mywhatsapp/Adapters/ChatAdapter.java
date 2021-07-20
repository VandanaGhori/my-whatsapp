package com.example.mywhatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.Models.MessageModel;
import com.example.mywhatsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

// Adapter class which inherits only RecyclerView.Adapter as it contains more than one ViewHolder
public class ChatAdapter extends RecyclerView.Adapter {

    public static final int SENDER_VIEW_TYPE = 1;
    public static final int RECEIVER_VIEW_TYPE = 2;

    ArrayList<MessageModel> messagesModelList;
    Context context;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messagesModelList = messageModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messagesModelList.get(position);

        if(holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessageContent());
        } else {
            ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessageContent());
        }
    }

    @Override
    public int getItemCount() {
        return messagesModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // if logged in user and user found from database is same then, he/she is a sender and rest's are receivers
        if(messagesModelList.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    // Need to separate ViewHolder for handling Receiver and Sender's message
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg, receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
