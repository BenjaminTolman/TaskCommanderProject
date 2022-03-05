package com.benjamintolman.taskcommander.Objects;

public class Job {

    private String jobTitle;
    private String jobAddress;
    private Double jobLat;
    private Double jobLon;
    private String jobStatus;
    private int jobHour;
    private int jobMin;
    private int jobDay;
    private int jobMonth;
    private int jobYear;
    private String jobNotes;
    private String clientName;

    public Double getJobLat() {
        return jobLat;
    }

    public void setJobLat(Double jobLat) {
        this.jobLat = jobLat;
    }

    public Double getJobLon() {
        return jobLon;
    }

    public void setJobLon(Double jobLon) {
        this.jobLon = jobLon;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getJobDay() {
        return jobDay;
    }

    public void setJobDay(int jobDay) {
        this.jobDay = jobDay;
    }

    public int getJobMonth() {
        return jobMonth;
    }

    public void setJobMonth(int jobMonth) {
        this.jobMonth = jobMonth;
    }

    public int getJobYear() {
        return jobYear;
    }

    public void setJobYear(int jobYear) {
        this.jobYear = jobYear;
    }

    private String clientPhone;
    private String employeeAssigned;

    public Job(String jobTitle, String jobAddress, int jobHour, int jobMin, int jobDay, int jobMonth, int jobYear, String jobNotes, String clientName, String clientPhone, String employeeAssigned, String jobStatus) {
        this.jobTitle = jobTitle;
        this.jobAddress = jobAddress;
        this.jobHour = jobHour;
        this.jobMin = jobMin;
        this.jobDay = jobDay;
        this.jobMonth = jobMonth;
        this.jobYear = jobYear;
        this.jobNotes = jobNotes;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.employeeAssigned = employeeAssigned;
        this.jobStatus = jobStatus;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobTime(){
        //todo create the AM/PM CHECK AND send back actual time not military
        return String.valueOf(jobHour) + ":" + String.valueOf(jobMin);
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

    public int getJobHour() {
        return jobHour;
    }

    public void setJobHour(int jobHour) {
        this.jobHour = jobHour;
    }

    public int getJobMin() {
        return jobMin;
    }

    public void setJobMin(int jobMin) {
        this.jobMin = jobMin;
    }

    public String getJobDate() {
        //todo create the AM/PM CHECK AND send back actual time not military
        return String.valueOf(jobDay) + "/" + String.valueOf(jobMonth) + "/" + String.valueOf(jobYear);
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
