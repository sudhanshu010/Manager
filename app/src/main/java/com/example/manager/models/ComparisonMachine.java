package com.example.manager.models;

import java.util.HashMap;

public class ComparisonMachine {

    String machineUID;
    String dept;
    String typeOfMachine;
    float Tavg;
    int sum;
    int Extras;
    String nextServiceTime;
    String managerId;
    int ServiceCount;
    int BuyCost;
    int ServiceTime;

    public ComparisonMachine(){}

    public ComparisonMachine(String dept, String typeOfMachine, int sum, String nextServiceTime, String managerUid, float Tavg, int Extras,int ServiceCount, int BuyCost, int ServiceTime, String machineUID) {
        this.dept = dept;
        this.typeOfMachine = typeOfMachine;
        this.sum = sum;
        this.nextServiceTime = nextServiceTime;
        this.managerId = managerUid;
        this.Tavg = Tavg;
        this.Extras = Extras;
        this.ServiceCount = ServiceCount;
        this.BuyCost = BuyCost;
        this.ServiceTime = ServiceTime;
        this.machineUID = machineUID;
    }

    public int getBuyCost() {
        return BuyCost;
    }

    public void setBuyCost(int buyCost) {
        BuyCost = buyCost;
    }

    public int getServiceTime() {
        return ServiceTime;
    }

    public void setServiceTime(int serviceTime) {
        ServiceTime = serviceTime;
    }

    public String getMachineUID() {
        return machineUID;
    }

    public void setMachineUID(String machineUID) {
        this.machineUID = machineUID;
    }

    public int getServiceCount() {
        return ServiceCount;
    }

    public void setServiceCount(int serviceCount) {
        ServiceCount = serviceCount;
    }

    public int getExtras() {
        return Extras;
    }

    public void setExtras(int extras) {
        Extras = extras;
    }

    public float getTavg() {
        return Tavg;
    }

    public void setTavg(float tavg) {
        Tavg = tavg;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTypeOfMachine() {
        return typeOfMachine;
    }

    public void setTypeOfMachine(String typeOfMachine) {
        this.typeOfMachine = typeOfMachine;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getNextServiceTime() {
        return nextServiceTime;
    }

    public void setNextServiceTime(String nextServiceTime) {
        this.nextServiceTime = nextServiceTime;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

}
