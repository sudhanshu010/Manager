package com.example.manager.models;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;

@Parcel
public class Mechanic implements Cloneable{

    private String userName, email, profilePicLink, uid;
    private int load = 0,numberOfRating;
    private float overallRating=0;
    private HashMap<String,Float> rating;
    private HashMap<String,Complaint> pendingComplaints, completedComplaints;
    private HashMap<String,Request> pendingRequest, completedRequest;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Mechanic() {
    }

    public Mechanic(String userName, String email, String profilePicLink, int load, int numberOfRating, float overallRating, HashMap<String, Float> rating, HashMap<String, Complaint> pendingComplaints, HashMap<String, Complaint> completedComplaints, HashMap<String, Request> pendingRequest, HashMap<String, Request> completedRequest, String uid) {
        this.userName = userName;
        this.email = email;
        this.profilePicLink = profilePicLink;
        this.load = load;
        this.numberOfRating = numberOfRating;
        this.overallRating = overallRating;
        this.rating = rating;
        this.pendingComplaints = pendingComplaints;
        this.completedComplaints = completedComplaints;
        this.pendingRequest = pendingRequest;
        this.completedRequest = completedRequest;
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public float getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(float overallRating) {
        this.overallRating = overallRating;
    }

    public HashMap<String, Float> getRating() {
        return rating;
    }

    public void setRating(HashMap<String, Float> rating) {
        this.rating = rating;
    }

    public HashMap<String, Complaint> getPendingComplaints() {
        return pendingComplaints;
    }

    public void setPendingComplaints(HashMap<String, Complaint> pendingComplaints) {
        this.pendingComplaints = pendingComplaints;
    }

    public HashMap<String, Complaint> getCompletedComplaints() {
        return completedComplaints;
    }

    public void setCompletedComplaints(HashMap<String, Complaint> completedComplaints) {
        this.completedComplaints = completedComplaints;
    }

    public HashMap<String, Request> getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(HashMap<String, Request> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public HashMap<String, Request> getCompletedRequest() {
        return completedRequest;
    }

    public void setCompletedRequest(HashMap<String, Request> completedRequest) {
        this.completedRequest = completedRequest;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
