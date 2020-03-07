package com.example.manager.models;

import org.parceler.Parcel;

@Parcel
public class Request implements Cloneable{

    private Complaint complaint;
    private String description, generatedDate, approvedDate;
    private int requestId, availabilityOfParts;
    private float cost;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Request() {
    }

    public Request(Complaint complaint, String description, String generatedDate, String approvedDate, int requestId, int availabilityOfParts, float cost) {
        this.complaint = complaint;
        this.description = description;
        this.generatedDate = generatedDate;
        this.approvedDate = approvedDate;
        this.requestId = requestId;
        this.availabilityOfParts = availabilityOfParts;
        this.cost = cost;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getAvailabilityOfParts() {
        return availabilityOfParts;
    }

    public void setAvailabilityOfParts(int availabilityOfParts) {
        this.availabilityOfParts = availabilityOfParts;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
