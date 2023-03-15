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
    public User(String name, String DoB, String location, String height, String weight, String uid) {
        this.name = name;
        this.DoB = DoB;
        this.location = location;
        this.height = height;
        this.weight = weight;
        userId = uid;
    }
    public void writeToFirebase(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        DatabaseReference userRef = usersRef.child(id);

        userRef.child("name").setValue(name);
        userRef.child("dob").setValue(DoB);
        userRef.child("location").setValue(location);
        userRef.child("height").setValue(height);
        userRef.child("weight").setValue(weight);
    }


}
