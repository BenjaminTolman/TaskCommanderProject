package com.benjamintolman.taskcommander.Objects;

import java.util.ArrayList;
import java.util.Comparator;

public class Job{

    private String jobTitle;
    private String jobAddress;
    private Double jobLat = 0.0;
    private Double jobLon = 0.0;
    private String jobStatus;
    private int jobHour;
    private int jobMin;
    private int jobDay;
    private int jobMonth;
    private int jobYear;
    private String jobNotes;
    private String clientName;
    private String companyCode;
    public ArrayList<String> jobImageURLs;
    private String updated = "";

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

    public String getUpdated() {
        return updated;
    }

    public Job(String jobTitle, String jobAddress, int jobHour, int jobMin, int jobDay,
               int jobMonth, int jobYear, String jobNotes, String clientName, String clientPhone,
               String employeeAssigned, String jobStatus, String companyCode, String updated) {
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
        this.companyCode = companyCode;
        this.updated = updated;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobTime(){

        int newHour = jobHour;
        String newMin = String.valueOf(jobMin);

        String ampm = "";
        if(jobHour > 12){
            newHour = jobHour - 12;
            ampm = "PM";
        }else{
            ampm = "AM";
        }
        if(jobHour == 12){
            ampm = "PM";
        }
        if(jobMin < 10){
            newMin = "0" + jobMin;
        }
        return String.valueOf(newHour) + ":" + String.valueOf(newMin) + " " + ampm;
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
        String jobMonthString = "";
        switch(jobMonth){
            case 0: jobMonthString = "Jan";
            break;
            case 1: jobMonthString = "Feb";
                break;
            case 2: jobMonthString = "Mar";
                break;
            case 3: jobMonthString = "Apr";
                break;
            case 4: jobMonthString = "May";
                break;
            case 5: jobMonthString = "Jun";
                break;
            case 6: jobMonthString = "Jul";
                break;
            case 7: jobMonthString = "Aug";
                break;
            case 8: jobMonthString = "Sep";
                break;
            case 9: jobMonthString = "Oct";
                break;
            case 10: jobMonthString = "Nov";
                break;
            case 11: jobMonthString = "Dec";
                break;
        }
        return String.valueOf(jobMonthString)  + " " + String.valueOf(jobDay) + " " + String.valueOf(jobYear);
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

    public static Comparator<Job> jobStatusCompare = new Comparator<Job>() {

        @Override
        public int compare(Job job, Job t1) {

            String status1
                    = job.getJobStatus().toUpperCase();
            String status2
                    = t1.getJobStatus().toUpperCase();

            // ascending order
            return status1.compareTo(
                    status2);
        }

    };
    public static Comparator<Job> jobStatusCompareD = new Comparator<Job>() {

        @Override
        public int compare(Job job, Job t1) {

            String status1
                    = job.getJobStatus().toUpperCase();
            String status2
                    = t1.getJobStatus().toUpperCase();

            // ascending order
            return status2.compareTo(
                    status1);
        }

    };

    public static Comparator<Job> compareDateDescending = new Comparator<Job>() {

        @Override
        public int compare(Job job, Job t1) {

            String date1
                    = job.getJobDate();
            String date2
                    = t1.getJobDate();

            // ascending order
            return date2.compareTo(
                    date1);
        }

    };

    public static Comparator<Job> compareDateAscending = new Comparator<Job>() {

        @Override
        public int compare(Job job, Job t1) {

            String date1
                    = job.getJobDate();
            String date2
                    = t1.getJobDate();

            // ascending order
            return date1.compareTo(
                    date2);
        }

    };

}
