package com.example.manager.models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class Chat implements Cloneable{

    public String sender;
    public String receiver;
    public String message;
    public boolean isseen;
    public long messageTime;


    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Chat(String sender, String receiver, String message, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;

        messageTime = new Date().getTime();
    }

    public Chat() {
        messageTime = new Date().getTime();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
