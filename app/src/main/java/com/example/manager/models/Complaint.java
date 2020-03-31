package com.example.manager.models;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class Complaint implements Cloneable{

    public static int generatedOnly = 1;
    public static int generatedAndAccpted = 2;
    public static int updateRequest = 3;
    public static int RequestApproved = 4;
    public static int complaintFinished = 5;

    private String description,generatedDate, completedDate;

    private Machine machine;
    private Manager manager;
    private Mechanic mechanic;
    private int status;
    private long complaintId;
    public float cost=0;
    private HashMap<String,Request> pendingRequest;
    private Chat chat;
    private String serviceType, instruction;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Complaint(){}

    public Complaint(long complaintId, String description, String generatedDate, String completedDate, Machine machine, Manager manager, Mechanic mechanic, int status, float cost, HashMap<String, Request> pendingRequest, Chat chat, String serviceType, String instruction) {
        this.complaintId = complaintId;
        this.description = description;
        this.generatedDate = generatedDate;
        this.completedDate = completedDate;
        this.machine = machine;
        this.manager = manager;
        this.mechanic = mechanic;
        this.status = status;
        this.cost = cost;
        this.pendingRequest = pendingRequest;
        this.chat = chat;
        this.serviceType = serviceType;
        this.instruction = instruction;
    }

    public long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(long complaintId) {
        this.complaintId = complaintId;
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

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public HashMap<String, Request> getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(HashMap<String, Request> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
