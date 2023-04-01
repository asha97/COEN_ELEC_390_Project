package com.example.coen_elec_390_project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class StatisticsHelper implements Serializable {
    private ArrayList<Float> temperature_history;
    private ArrayList<Float> humidity_history;
    private ArrayList<Float> altitude_history;
    private ArrayList<Float> co2_history;
    private ArrayList<Float> gas_history;
    private ArrayList<Float> pressure_history;
    private ArrayList<Float> tVOC_history;

    public StatisticsHelper(){

    }
    public StatisticsHelper(ArrayList<Float> altitude_history_in,ArrayList<Float> humidity_history_in,ArrayList<Float> temperature_history_in,ArrayList<Float> co2_history_in,ArrayList<Float> gas_history_in,ArrayList<Float> pressure_history_in,ArrayList<Float> tVOC_history_in ){
        this.altitude_history = altitude_history_in;
        this.humidity_history = humidity_history_in;
        this.temperature_history = temperature_history_in;
        this.co2_history = co2_history_in;
        this.gas_history = gas_history_in;
        this.pressure_history = pressure_history_in;
        this.tVOC_history = tVOC_history_in;
    }
    public float calculateMean_Altitude(){
        float sum = 00.00f;
        for (float element:altitude_history) {
            sum += element;
        }
        return (sum/altitude_history.size());
    }

    public float calculateMean_Temperature(){
        float sum = 00.00f;
        for (float element:temperature_history) {
            sum += element;
        }
        return (sum/temperature_history.size());
    }

    public float calculateMean_Humidity(){
        float sum = 00.00f;
        for (float element:humidity_history) {
            sum += element;
        }
        return (sum/humidity_history.size());
    }

    public float calculateMean_co2(){
        float sum = 00.00f;
        for (float element:co2_history) {
            sum += element;
        }
        return (sum/co2_history.size());
    }
    public float calculateMean_Gas(){
        float sum = 00.00f;
        for (float element:gas_history) {
            sum += element;
        }
        return (sum/gas_history.size());
    }
    public float calculateMean_Pressure(){
        float sum = 00.00f;
        for (float element:pressure_history) {
            sum += element;
        }
        return (sum/pressure_history.size());
    }
    public float calculateMean_tVOC(){
        float sum = 00.00f;
        for (float element:tVOC_history) {
            sum += element;
        }
        return (sum/tVOC_history.size());
    }
    private float findMinimum(ArrayList<Float> history_data){
        float min = history_data.get(0);
        for (float element:history_data) {
            if (min > element){
                min = element;
            }
            else{
                //do nothing
            }
        }
        return min;
    }
    private float findMaximum(ArrayList<Float> history_data){
        float max = history_data.get(0);
        for (float element:history_data) {
            if (max < element){
                max = element;
            }
            else{
                //do nothing
            }
        }
        return max;
    }
    public float calculateMedian_Temperature(){
        ArrayList<Float> tempArray = temperature_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMedian_Humidity(){
        ArrayList<Float> tempArray = humidity_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMedian_Pressure(){
        ArrayList<Float> tempArray = pressure_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMedian_Gas(){
        ArrayList<Float> tempArray = gas_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMedian_Altitude(){
        ArrayList<Float> tempArray = altitude_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMedian_Co2(){
        ArrayList<Float> tempArray = co2_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMedian_tVOC(){
        ArrayList<Float> tempArray = tVOC_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public float calculateMin_Altitude(){
        float min = findMinimum(altitude_history);
        return min;
    }
    public float calculateMax_Altitude(){
        float max = findMaximum(altitude_history);
        return max;
    }
    public float calculateMin_Temperature(){
        float min = findMinimum(temperature_history);
        return min;
    }
    public float calculateMax_Temperature(){
        float max = findMaximum(temperature_history);
        return max;
    }
    public float calculateMin_Humidity(){
        float min = findMinimum(humidity_history);
        return min;
    }
    public float calculateMax_Humidity(){
        float max = findMaximum(humidity_history);
        return max;
    }
    public float calculateMin_Pressure(){
        float min = findMinimum(pressure_history);
        return min;
    }
    public float calculateMax_Pressure(){
        float max = findMaximum(pressure_history);
        return max;
    }
    public float calculateMin_Gas(){
        float min = findMinimum(gas_history);
        return min;
    }
    public float calculateMax_Gas(){
        float max = findMaximum(gas_history);
        return max;
    }
    public float calculateMin_tVOC(){
        float min = findMinimum(tVOC_history);
        return min;
    }
    public float calculateMax_tVOC(){
        float max = findMaximum(tVOC_history);
        return max;
    }
    public float calculateMin_co2(){
        float min = findMinimum(co2_history);
        return min;
    }
    public float calculateMax_co2(){
        float max = findMaximum(co2_history);
        return max;
    }
}
