package com.example.manager.models;

import java.util.List;

public class Mechanic {

    String userName, Email;
    int load = 0;

    float rating=0;
    int numberOfRating = 0;

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    String imageURL;

    List<String> pendingRequest, completedRequest, pendingComplaint, completedComplaint;

    public Mechanic(){}

    public Mechanic(String userName, String email, List<String> pendingRequest, List<String> completedRequest, List<String> pendingComplaint, List<String> completedComplaint) {
        this.userName = userName;
        Email = email;
        this.pendingRequest = pendingRequest;
        this.completedRequest = completedRequest;
        this.pendingComplaint = pendingComplaint;
        this.completedComplaint = completedComplaint;
    }

    public List<String> getPendingRequest() {
        return pendingRequest;
    }

    public List<String> getCompletedRequest() {
        return completedRequest;
    }

    public List<String> getPendingComplaint() {
        return pendingComplaint;
    }

    public List<String> getCompletedComplaint() {
        return completedComplaint;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setPendingRequest(List<String> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public void setCompletedRequest(List<String> completedRequest) {
        this.completedRequest = completedRequest;
    }

    public void setPendingComplaint(List<String> pendingComplaint) {
        this.pendingComplaint = pendingComplaint;
    }

    public void setCompletedComplaint(List<String> completedComplaint) {
        this.completedComplaint = completedComplaint;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }
}
