package com.example.coen_elec_390_project;

import static java.lang.Math.abs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Stopwatch {

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");
    private String startTime;
    private String stopTime;

    public Stopwatch (){
        startTime = null;
        stopTime = null;
    }

    public void start(){
        startTime = FORMAT.format(new Date());
    }

    public void stop(){
        stopTime = FORMAT.format(new Date());
    }

    public String getElapsedTime(){
        if (startTime == null || stopTime == null){
            return null;
        }
        else{
            String[] startArray = startTime.split(":");
            String[] stopArray = stopTime.split(":");
            int hours = abs(Integer.parseInt(stopArray[0]) - Integer.parseInt(startArray[0]));
            int minutes = abs(Integer.parseInt(stopArray[1]) - Integer.parseInt(startArray[1]));
            int seconds = abs(Integer.parseInt(stopArray[2]) - Integer.parseInt(startArray[2]));
            String result = Integer.toString(hours)+":"+Integer.toString(minutes)+":"+Integer.toString(seconds);
            return result;
        }
    }
}
