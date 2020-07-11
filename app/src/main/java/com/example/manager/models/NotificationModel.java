package com.example.manager.models;

public class NotificationModel {

    String message;
    int type;
    String subject;

    public NotificationModel() {
    }

    public NotificationModel(String message, int type, String subject) {
        this.message = message;
        this.type = type;
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
