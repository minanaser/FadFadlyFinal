package com.example.MNM;

public class ModelPatient {
     String name;
     String email;
     String image;
     String uid;


    public ModelPatient(){

    }

    public ModelPatient(String name, String email, String image, String uid) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.uid = uid;
    }

    public String getPaientID() {
        return uid;
    }

    public void setPaientID(String uid) {
        this.uid = uid;
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

