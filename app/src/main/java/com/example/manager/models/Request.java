package com.example.manager.models;

public class Request {
    String serviceMan;
    String Responsible;
    String description;
    boolean status;
    String complaintId;
    String requestid;

    public String getServicemanName() {
        return servicemanName;
    }

    public String getResponsiblemanName() {
        return responsiblemanName;
    }

    public void setServicemanName(String servicemanName) {
        this.servicemanName = servicemanName;
    }

    public void setResponsiblemanName(String responsiblemanName) {
        this.responsiblemanName = responsiblemanName;
    }

    String servicemanName, responsiblemanName;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    boolean expanded;


    public Request(){
    }

    public Request(String serviceMan, String responsible, String description, boolean status, String complaintId) {
        this.serviceMan = serviceMan;
        Responsible = responsible;
        this.description = description;
        this.status = status;
        this.complaintId = complaintId;
    }

    public String getServiceMan() {
        return serviceMan;
    }

    public String getResponsible() {
        return Responsible;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStatus() {
        return status;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setServiceMan(String serviceMan) {
        this.serviceMan = serviceMan;
    }

    public void setResponsible(String responsible) {
        Responsible = responsible;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
}
