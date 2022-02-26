package com.benjamintolman.taskcommander.Objects;

public class Job {

    private String jobTitle;
    private String jobAddress;
    private String jobTime;
    private String jobDate;
    private String jobNotes;
    private String clientName;
    private String clientPhone;
    private String employeeAssigned;


    public Job(String jobTitle, String jobAddress, String jobTime, String jobDate, String jobNotes, String clientName, String clientPhone, String employeeAssigned) {
        this.jobTitle = jobTitle;
        this.jobAddress = jobAddress;
        this.jobTime = jobTime;
        this.jobDate = jobDate;
        this.jobNotes = jobNotes;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.employeeAssigned = employeeAssigned;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobNotes() {
        return jobNotes;
    }

    public void setJobNotes(String jobNotes) {
        this.jobNotes = jobNotes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getEmployeeAssigned() {
        return employeeAssigned;
    }

    public void setEmployeeAssigned(String employeeAssigned) {
        this.employeeAssigned = employeeAssigned;
    }
}
