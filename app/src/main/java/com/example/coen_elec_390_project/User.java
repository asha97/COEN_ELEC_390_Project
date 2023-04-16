package com.example.coen_elec_390_project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//TODO @Asha @Pavi it seems that this class has many methods that aren't used. Let me know if this isn't the case or please clean them up.
/**
 * This class represents each user and their biometrics
 * @author David Molina (4011257), Asha Islam (), Pavithra Sivagnanasunthrama()
 */
public class User {
    private String name;
    private String DoB;
    private String location;
    private int height;
    private double weight;
    private String userId;
    //this is the email address that we are going to be getting from the login
    private String email_address;

    /**
     * Default constructor
     */
    public User() {

    }

    /**
     * Custom constructor
     * @param name user's name
     * @param DoB user's date of birth
     * @param location user's location
     * @param height user's height
     * @param weight user's weight
     * @param email user's email
     */
    public User(String name, String DoB, String location, String height, String weight,String email) {
        this.name = name;
        this.DoB = DoB;
        this.location = location;
        this.height = Integer.parseInt(height);
        this.weight = Double.parseDouble(weight);
        this.email_address = email;
    }



    /**
     * Gets user's name
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDoB() {
        return DoB;
    }

    /**
     * Sets user's DOB
     */
    public void setDoB(String DoB) {
        this.DoB = DoB;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Sets user's location
     * @param location user's location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets user's height
     * @return user's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets user's height
     * @param height user's height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    /**
     * Sets user's weight
     * @param weight user's weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }



}
