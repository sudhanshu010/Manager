package com.example.manager.models;

public class NotificationModel {

    String message;
    String type;
    String subject;

    public NotificationModel() {
    }

    public NotificationModel(String type, String subject, String message) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
