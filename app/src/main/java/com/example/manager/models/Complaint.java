package com.example.manager.models;

import java.util.Date;

public class Complaint {

    String complaintGenerator;
    String complaintAllocatedTo;
    String complaintMachineId;
    String complaintId;
    String complaintGeneratedDate;
    String complaintCompletedDate;

    String generatorName;
    String servicemanName;

    boolean expanded;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    Chat chat;

    int status;
    int generatedOnly = 1;
    int generatedAndAccpted = 2;
    int updateRequest = 3;
    int RequestApproved = 4;
    int complaintFinished = 5;


    public Complaint(){}

    public Complaint(String complaintGenerator, String complaintAllocatedTo, String complaintMachineId, Date complaintGeneratedDate, Date complaintCompletedDate, int status, String complaintDescription) {
        this.complaintGenerator = complaintGenerator;
        this.complaintAllocatedTo = complaintAllocatedTo;
        this.complaintMachineId = complaintMachineId;
//        this.complaintGeneratedDate = complaintGeneratedDate;
//        this.complaintCompletedDate = complaintCompletedDate;
        this.status = status;
        this.complaintDescription = complaintDescription;
    }


    public String getGeneratorName() {
        return generatorName;
    }

    public String getServicemanName() {
        return servicemanName;
    }

    public void setGeneratorName(String generatorName) {
        this.generatorName = generatorName;
    }

    public void setServicemanName(String servicemanName) {
        this.servicemanName = servicemanName;
    }






    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }




    public int getGeneratedOnly() {
        return 1;
    } public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getGeneratedAndAccpted() {
        return 2;
    }

    public int getUpdateRequest() {
        return 3;
    }

    public int getRequestApproved() {
        return 4;
    }

    public int getComplaintFinished() {
        return 5;
    }


    String complaintDescription;




    public String getComplaintGenerator() {
        return complaintGenerator;
    }

    public String getComplaintAllocatedTo() {
        return complaintAllocatedTo;
    }

    public String getComplaintMachineId() {
        return complaintMachineId;
    }

    public String  getComplaintGeneratedDate() {
        return complaintGeneratedDate;
    }

    public String getComplaintCompletedDate() {
        return complaintCompletedDate;
    }

    public int getStatus() {
        return status;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintGenerator(String complaintGenerator) {
        this.complaintGenerator = complaintGenerator;
    }

    public void setComplaintAllocatedTo(String complaintAllocatedTo) {
        this.complaintAllocatedTo = complaintAllocatedTo;
    }

    public void setComplaintMachineId(String complaintMachineId) {
        this.complaintMachineId = complaintMachineId;
    }

    public void setComplaintGeneratedDate(String complaintGeneratedDate) {
        this.complaintGeneratedDate = complaintGeneratedDate;
    }

    public void setComplaintCompletedDate(String complaintCompletedDate) {
        this.complaintCompletedDate = complaintCompletedDate;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }
}
