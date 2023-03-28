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

    public StatisticsHelper(ArrayList<Float> altitude_history,ArrayList<Float> humidity_history,ArrayList<Float> temperature_history,ArrayList<Float> co2_history,ArrayList<Float> gas_history,ArrayList<Float> pressure_history,ArrayList<Float> tVOC_history ){
        this.altitude_history = altitude_history;
        this.humidity_history = humidity_history;
        this.temperature_history = temperature_history;
        this.co2_history = co2_history;
        this.gas_history = gas_history;
        this.pressure_history = pressure_history;
        this.tVOC_history = tVOC_history;
    }
    public Float calculateMean_Altitude(){
        Float sum = 00.00f;
        for (Float element:altitude_history) {
            sum += element;
        }
        return (sum/altitude_history.size());
    }

    public Float calculateMean_Temperature(){
        Float sum = 00.00f;
        for (Float element:temperature_history) {
            sum += element;
        }
        return (sum/temperature_history.size());
    }

    public Float calculateMean_Humidity(){
        Float sum = 00.00f;
        for (Float element:humidity_history) {
            sum += element;
        }
        return (sum/humidity_history.size());
    }

    public Float calculateMean_co2(){
        Float sum = 00.00f;
        for (Float element:co2_history) {
            sum += element;
        }
        return (sum/co2_history.size());
    }
    public Float calculateMean_Gas(){
        Float sum = 00.00f;
        for (Float element:gas_history) {
            sum += element;
        }
        return (sum/gas_history.size());
    }
    public Float calculateMean_Pressure(){
        Float sum = 00.00f;
        for (Float element:pressure_history) {
            sum += element;
        }
        return (sum/pressure_history.size());
    }
    public Float calculateMean_tVOC(){
        Float sum = 00.00f;
        for (Float element:tVOC_history) {
            sum += element;
        }
        return (sum/tVOC_history.size());
    }
    private Float findMinimum(ArrayList<Float> history_data){
        Float min = history_data.get(0);
        for (Float element:history_data) {
            if (min > element){
                min = element;
            }
            else{
                //do nothing
            }
        }
        return min;
    }
    private Float findMaximum(ArrayList<Float> history_data){
        Float max = history_data.get(0);
        for (Float element:history_data) {
            if (max < element){
                max = element;
            }
            else{
                //do nothing
            }
        }
        return max;
    }
    public Float calculateMedian_Temperature(){
        ArrayList<Float> tempArray = temperature_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMedian_Humidity(){
        ArrayList<Float> tempArray = humidity_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMedian_Pressure(){
        ArrayList<Float> tempArray = pressure_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMedian_Gas(){
        ArrayList<Float> tempArray = gas_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMedian_Altitude(){
        ArrayList<Float> tempArray = altitude_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMedian_Co2(){
        ArrayList<Float> tempArray = co2_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMedian_tVOC(){
        ArrayList<Float> tempArray = tVOC_history;
        Collections.sort(tempArray);
        if(tempArray.size()%2 == 0){
            return (tempArray.get(tempArray.size()/2) + tempArray.get(((tempArray.size())-1)/2))/2;
        }
        else {
            return tempArray.get(tempArray.size()/2);
        }
    }
    public Float calculateMin_Altitude(){
        Float min = findMinimum(altitude_history);
        return min;
    }
    public Float calculateMax_Altitude(){
        Float max = findMaximum(altitude_history);
        return max;
    }
    public Float calculateMin_Temperature(){
        Float min = findMinimum(temperature_history);
        return min;
    }
    public Float calculateMax_Temperature(){
        Float max = findMaximum(temperature_history);
        return max;
    }
    public Float calculateMin_Humidity(){
        Float min = findMinimum(humidity_history);
        return min;
    }
    public Float calculateMax_Humidity(){
        Float max = findMaximum(humidity_history);
        return max;
    }
    public Float calculateMin_Pressure(){
        Float min = findMinimum(pressure_history);
        return min;
    }
    public Float calculateMax_Pressure(){
        Float max = findMaximum(pressure_history);
        return max;
    }
    public Float calculateMin_Gas(){
        Float min = findMinimum(gas_history);
        return min;
    }
    public Float calculateMax_Gas(){
        Float max = findMaximum(gas_history);
        return max;
    }
    public Float calculateMin_tVOC(){
        Float min = findMinimum(tVOC_history);
        return min;
    }
    public Float calculateMax_tVOC(){
        Float max = findMaximum(tVOC_history);
        return max;
    }
    public Float calculateMin_co2(){
        Float min = findMinimum(co2_history);
        return min;
    }
    public Float calculateMax_co2(){
        Float max = findMaximum(co2_history);
        return max;
    }
}
