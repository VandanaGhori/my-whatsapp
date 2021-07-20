package com.example.mywhatsapp.Models;

public class MessageModel {
    // uId is either the Sender or Receiver
    String uId;
    String messageContent;
    Long timeStamp;

    public MessageModel(String uId, String messageContent, Long timeStamp) {
        this.uId = uId;
        this.messageContent = messageContent;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String uId, String messageContent) {
        this.uId = uId;
        this.messageContent = messageContent;
    }

    // For firebase we need to create an empty constructor
    public MessageModel() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
