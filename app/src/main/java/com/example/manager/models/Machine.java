package com.example.manager.models;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class Machine implements Cloneable{

    private String serialNumber, dateOfInstallation, department, machineId, type, company, modelNumber, qrImageLink;

    private int serviceTime;
    private int[] cost;

    private float price, scrap, life, averageCost;
    private  int serviceNumber;

    private boolean status;
    private boolean working;

    private Manager manager;
    private HashMap<String,Request> pastRecordList;

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Machine() {
    }

    public Machine(String serialNumber, String dateOfInstallation, String department, String machineId,
                   String type, String company, String modelNumber, String qrImageLink, int serviceTime,
                   int[] cost, float price, boolean status, Manager manager, HashMap<String, Request> pastRecordList, float scrap, float life, boolean working) {

        this.serialNumber = serialNumber;
        this.dateOfInstallation = dateOfInstallation;
        this.department = department;
        this.machineId = machineId;
        this.type = type;
        this.company = company;
        this.modelNumber = modelNumber;
        this.qrImageLink = qrImageLink;
        this.serviceTime = serviceTime;
        this.cost = cost;
        this.price = price;
        this.status = status;
        this.manager = manager;
        this.pastRecordList = pastRecordList;
        this.scrap = scrap;
        this.life = life;
        this.serviceNumber = 0;
        this.averageCost = price-scrap;
        this.working = working;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDateOfInstallation() {
        return dateOfInstallation;
    }

    public void setDateOfInstallation(String dateOfInstallation) {
        this.dateOfInstallation = dateOfInstallation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getQrImageLink() {
        return qrImageLink;
    }

    public void setQrImageLink(String qrImageLink) {
        this.qrImageLink = qrImageLink;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int[] getCost() {
        return cost;
    }

    public void setCost(int[] cost) {
        this.cost = cost;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public HashMap<String, Request> getPastRecordList() {
        return pastRecordList;
    }

    public void setPastRecordList(HashMap<String, Request> pastRecordList) {
        this.pastRecordList = pastRecordList;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public float getScrap() {
        return scrap;
    }

    public void setScrap(float scrap) {
        this.scrap = scrap;
    }

    public float getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(float averageCost) {
        this.averageCost = averageCost;
    }

    public int getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(int serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }
}