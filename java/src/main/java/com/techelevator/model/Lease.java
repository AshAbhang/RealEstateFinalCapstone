package com.techelevator.model;

import java.util.Date;

public class Lease {
    //instance variables
    private int leaseId;
    private int userId;
    private int propId;
    private Date startDate;
    private Date endDate;
    private double rent;
    private String leaseStatus;
    private int term_length;

    //constructor
    public Lease(int userId, Date startDate, Date endDate, double rent, String leaseStatus, int term_length) {
        this.leaseId = leaseId;
        this.userId = userId;
        this.propId = propId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rent = rent;
        this.leaseStatus = leaseStatus;
        this.term_length = term_length;
    }


    //getters
    public int getLeaseId() {
        return leaseId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPropId(){
        return propId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getRent() {
        return rent;
    }

    public String getLeaseStatus() {
        return leaseStatus;
    }

    public int getTerm_length() {
        return term_length;
    }

    //setters

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPropId(int propId){
        this.propId = propId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public void setLeaseStatus(String leaseStatus) {
        this.leaseStatus = leaseStatus;
    }

    public void setTerm_length(int term_length) {
        this.term_length = term_length;
    }
}
