package com.example.coen_elec_390_project;

public class Information {
    private String dob, height, location, name, weight;

    public Information(){
    }

    public Information(String dob, String height, String location, String name, String weight){
        this.dob = dob;
        this.height = height;
        this.location = location;
        this.name = name;
        this.weight = weight;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
