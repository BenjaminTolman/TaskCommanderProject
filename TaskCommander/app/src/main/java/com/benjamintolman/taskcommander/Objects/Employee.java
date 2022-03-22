package com.benjamintolman.taskcommander.Objects;

import android.telephony.PhoneNumberUtils;

import java.util.ArrayList;

public class Employee {

    private String email;
    private String name;
    private String password;
    private String phone;
    private String role;
    private String companyCode;
    private String imageURL;

    private double lat = 0;
    private double lon = 0;

    private ArrayList<Job> jobList;

    public Employee() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Employee(String email, String name, String password, String phone, String role, String companyCode, String imageURL) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.companyCode = companyCode;
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
    public String getPhoneFormatted() {

        String fPhone = PhoneNumberUtils.formatNumber(phone, "US"); // output: (202) 555-0739
        return fPhone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public ArrayList<Job> getJobList() {
        return jobList;
    }

    public void setJobList(ArrayList<Job> jobList) {
        this.jobList = jobList;
    }
}
