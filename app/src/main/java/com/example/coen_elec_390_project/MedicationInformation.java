package com.example.coen_elec_390_project;

public class MedicationInformation {

    String medication;
    int frequency;

    public MedicationInformation(String med, int freq){
        medication = med;
        frequency = freq;
    }

    public void setMedication(String m){
        medication = m;
    }

    public void setMedFrequency(int f){
        frequency = f;
    }

    public String getMedication(){
        return medication;
    }

    public int getMedFrequency(){
        return frequency;
    }
}
