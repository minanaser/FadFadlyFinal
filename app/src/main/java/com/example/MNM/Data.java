package com.example.MNM;

import android.util.Log;

public class Data {
    String name,uid,gender,speciality,accountType, profileImage;

    public Data() {
    }

    public Data(String name, String uid, String gender, String speciality,String accountType, String profileImage) {
        this.name = name;
        this.uid = uid;
        this.gender = gender;
        this.speciality = speciality;
        this.accountType = accountType;
        this.profileImage = profileImage;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
