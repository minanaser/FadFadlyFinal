package com.example.MNM;

public class ModelPatient {
     String name;
     String email;
     String image;
     String paientID;


    public ModelPatient(){

    }

    public ModelPatient(String name, String email, String image, String paientID) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.paientID = paientID;
    }

    public String getPaientID() {
        return paientID;
    }

    public void setPaientID(String paientID) {
        this.paientID = paientID;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}

