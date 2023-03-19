package com.example.coen_elec_390_project;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class User {
    private String name;
    private String DoB;
    private String location;
    private String height;
    private String weight;
    private String userId;
    //this is the email address that we are going to be getting from the login
    private String email_address;

    public User(String name, String DoB, String location, String height, String weight, String uid, String email) {
        this.name = name;
        this.DoB = DoB;
        this.location = location;
        this.height = height;
        this.weight = weight;
        this.userId = uid;
        this.email_address = email;
    }

    public void writeToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        DatabaseReference userRef = usersRef.child(email_address.replace(".",","));
        userRef.child("name").setValue(name);
        userRef.child("dob").setValue(DoB);
        userRef.child("location").setValue(location);
        userRef.child("height").setValue(height);
        userRef.child("weight").setValue(weight);
    }
    public void updateToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference().child("users");
        DatabaseReference userRef = usersRef.child(userId);

        // Update the fields only if they are not empty
        if (!name.isEmpty()) {
            userRef.child("name").setValue(name);
        }
        if (!DoB.isEmpty()) {
            userRef.child("dob").setValue(DoB);
        }
        if (!location.isEmpty()) {
            userRef.child("location").setValue(location);
        }
        if (!height.isEmpty()) {
            userRef.child("height").setValue(height);
        }
        if (!weight.isEmpty()) {
            userRef.child("weight").setValue(weight);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String DoB) {
        this.DoB = DoB;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return email_address;
    }

    public void setEmailAddress(String email_address) {
        this.email_address = email_address;
    }



}
