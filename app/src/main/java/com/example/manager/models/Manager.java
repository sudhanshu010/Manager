package com.example.manager.models;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class Manager implements Cloneable{

    private String email, userName, profilePicLink, idCardLink, phoneNumber,savedAddress,uid;
    private HashMap<String, Request> pendingApprovalRequest;
    private HashMap<String,Complaint> pendingComplaints;
    private HashMap<String,Complaint> completedComplaints;
    private HashMap<String,Machine> myMachines;
    private String password, empId, department, designation, longitude, latitude;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Manager()
    {
    }

    public Manager(String email, String userName, String profilePicLink, String phoneNumber, String savedAddress, String uid, HashMap<String, Request> pendingApprovalRequest, HashMap<String, Complaint> pendingComplaints, HashMap<String, Complaint> completedComplaints, HashMap<String, Machine> myMachines, String password, String empId, String department, String designation, String longitude, String latitude, String idCardLink) {
        this.email = email;
        this.userName = userName;
        this.profilePicLink = profilePicLink;
        this.phoneNumber = phoneNumber;
        this.savedAddress = savedAddress;
        this.uid = uid;
        this.pendingApprovalRequest = pendingApprovalRequest;
        this.pendingComplaints = pendingComplaints;
        this.completedComplaints = completedComplaints;
        this.myMachines = myMachines;
        this.password = password;
        this.empId = empId;
        this.department = department;
        this.designation = designation;
        this.longitude = longitude;
        this.latitude = latitude;
        this.idCardLink = idCardLink;
    }

    public String getIdCardLink() {
        return idCardLink;
    }

    public void setIdCardLink(String idCardLink) {
        this.idCardLink = idCardLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSavedAddress() {
        return savedAddress;
    }

    public void setSavedAddress(String savedAddress) {
        this.savedAddress = savedAddress;
    }

    public HashMap<String, Request> getPendingApprovalRequest() {
        return pendingApprovalRequest;
    }

    public void setPendingApprovalRequest(HashMap<String, Request> pendingApprovalRequest) {
        this.pendingApprovalRequest = pendingApprovalRequest;
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

    public HashMap<String, Machine> getMyMachines() {
        return myMachines;
    }

    public void setMyMachines(HashMap<String, Machine> myMachines) {
        this.myMachines = myMachines;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
