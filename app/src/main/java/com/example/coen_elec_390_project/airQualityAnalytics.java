package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.components.YAxis;


import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

/**
 * this class is going to be redirecting the user into the real-time air quality page where
 * we are going to be visualizing the real time change of data in a bar graph
 * there is also going to be the use of a ListView in order to see the data in a list format
 */
public class airQualityAnalytics extends AppCompatActivity {
    private BarChart barChart;
    private DatabaseReference reference;
    private ListView listView;

    /**
     * @author David Molina (40111257), Asha Islam (40051511), Pavithra Sivagnanasuntharam(40117356)
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *  Build.VERSION.SDK_INT: this is checking if the Android device which is running the mobile application
     *     has an OS version of API 26) or later.
     *
     *  NotificationChannel channel: this is creating a notification channel in order to create a notification
     *  getSupportActionBar(): this is going to be displaying the back button so that the user could go
     *    back to the home page.
     *
     *  listView: this is going to be used in order to display the list of data that is analyzed by the sensors.
     *  barChart: this is going to be used in order to display the data of the sensor graphically in a bar chart.
     *  reference: this is going to be invoking the reference object in order to get the data from Firebase Real-Time Database
     *  reference.ValueEventListener: invoked when there is a change in the data at a specific location in the database
     *  NotificationManagerCompat: used in order to send notification. We use this for all of the metrics that have
     *   higher than normal concentration of the metrics.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality_analytics);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //this is going to be displaying the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listview);
        barChart = findViewById(R.id.bar_chart);

        // fetch data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Sensor");

        //--------CREATION OF LIST OF DATA TO DISPLAY---------//
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_data, list);
        listView.setAdapter(adapter);

        //----------------------CREATION OF BARCHART--------------------------//

        //display into the box the right metrics
        String [] nameMetric = {"Altitude (m)", "CO2 (ppm)", "Gas Resistance (KOhms)", "Humidity (%)", "Pressure (KPa)", "Temperature (*C)", "Elapsed Time (ms)", "tVOC (g*m^-3)" };

        // Attach a ValueEventListener to the Firebase database node to get the data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // clear data to make sure there isn't anything else
                list.clear();

                // Create ArrayList to hold bar entries
                ArrayList<BarEntry> altitude_data = new ArrayList<>();
                ArrayList<BarEntry> co2_data = new ArrayList<>();
                ArrayList<BarEntry> gas_data = new ArrayList<>();
                ArrayList<BarEntry> humidity_data = new ArrayList<>();
                ArrayList<BarEntry> pressure_data = new ArrayList<>();
                ArrayList<BarEntry> temperature_data = new ArrayList<>();

                int i = 0;
                /**
                 * this is going to iterate through the data and populate the array list with the
                 * appropriate data.
                 */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    float value = Float.parseFloat(snapshot.getValue().toString());
                    //not going to be displaying the elapsed time
                    if (i !=6) {
                        if(i==0){ //
                            altitude_data.add(new BarEntry(i, value));
                        }
                        if(i==1){
                            co2_data.add(new BarEntry(i, value));
                        }
                        if(i==2){
                            gas_data.add(new BarEntry(i, value));
                        }
                        if(i==3){
                            humidity_data.add(new BarEntry(i,value));
                        }
                        if (i==4){
                            //Reducing pressure by a factor of 10: 1000hPa -> 100KPa
                            pressure_data.add(new BarEntry(i, (value/10))); // This will add the pressure in KPa to the bar graph
                        }
                        if(i==5){
                            temperature_data.add(new BarEntry(i, value));
                        }

                        //adding temperature into Fahrenheit in the listview only
                        String label = nameMetric[i];
                        String addData = label + ": " + snapshot.getValue().toString();
                        if (label.equals("Temperature (*C)")){
                            String addFahrenheit = "Temperature (*F): " + convertToFahrenheit(value);
                            list.add(addFahrenheit);
                        }

                        //adding pressure into Bar in the listview only
                        if (label.equals("Pressure (KPa)")){
                            String addBar = "Pressure (Bar): " + convertToBar(value/10);
                            list.add(addBar);
                        }

                        list.add(addData);
                        i++;
                    }
                }

                //custom colors for the bar graph
                int customColor1 = Color.rgb(54,21,30);
                int customColor2 = Color.rgb(89,63,98);
                int customColor3 = Color.rgb(123,109,141);
                int customColor4 = Color.rgb(148,154,255);
                int customColor5 = Color.rgb(165,196,212);
                int customColor6 = Color.rgb(129,5,97);

                //define each data set in order to add them into the barDataSets
                BarDataSet altitudeSet = new BarDataSet(altitude_data, "Altitude (m)");
                altitudeSet.setColor(customColor1);

                BarDataSet co2Set = new BarDataSet(co2_data, "CO2 (ppm)");
                co2Set.setColor(customColor2);

                BarDataSet gasSet = new BarDataSet(gas_data, "Gas (KOhms)");
                gasSet.setColor(customColor3);

                BarDataSet humiditySet = new BarDataSet(humidity_data, "Humidity (%)");
                humiditySet.setColor(customColor4);

                BarDataSet pressureSet = new BarDataSet(pressure_data, "Pressure (KPa)");
                pressureSet.setColor(customColor5);

                BarDataSet temperatureSet = new BarDataSet(temperature_data, "Temperature (*C)");
                temperatureSet.setColor(customColor6);

                ArrayList<IBarDataSet> barDataSets = new ArrayList<>();

                barDataSets.add(altitudeSet);
                barDataSets.add(co2Set);
                barDataSets.add(gasSet);
                barDataSets.add(humiditySet);
                barDataSets.add(pressureSet);
                barDataSets.add(temperatureSet);

                // add data sets into the bar chart
                BarData barData = new BarData(barDataSets);
                barChart.setData(barData);

                //change color of the background of the barchart
                int customChartBg = Color.rgb(222,229,255);
                barChart.setBackgroundColor(customChartBg);

                // --------Remove the grid-----------------
                XAxis xAxis = barChart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawAxisLine(true);

                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setDrawAxisLine(true);

                YAxis rightAxis = barChart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setDrawAxisLine(false);
                // --------Remove the grid-----------------

                barData.notifyDataChanged(); //notify the BarData that the data has changed to change the appearance of the chart
                barChart.notifyDataSetChanged(); //notify that dataset has been changed
                barChart.invalidate(); //visually update the chart
                adapter.notifyDataSetChanged(); //notify adapter that dataset has changed

                //----------------------CREATION OF NOTIFICATIONS--------------------------//
                String co2String = list.get(1);
                String[] value1 = co2String.split(": ", 2);
                double co2Value = Double.parseDouble(value1[1]);

                if(co2Value > 1200) {
                    //notification code for high CO2 warning
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(airQualityAnalytics.this, "My Notification");
                    builder.setContentTitle("Warning: There is high CO2 exposure");
                    builder.setContentText("Make sure to wear a mask or leave the premises!");
                    builder.setSmallIcon(R.drawable.ic_launcher_background);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(airQualityAnalytics.this);
                    if (ActivityCompat.checkSelfPermission(airQualityAnalytics.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    managerCompat.notify(1, builder.build());
                }

                String humidityString = list.get(3);
                String [] value3 = humidityString.split(": ", 2);
                double humidityValue = Double.parseDouble(value3[1]);

                if(humidityValue > 50.00000) {
                    //notification code for high humidity warning
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(airQualityAnalytics.this, "My Notification");
                    builder.setContentTitle("Warning: There is high humidity levels");
                    builder.setContentText("Make sure to have an inhaler if needed or leave the premises!");
                    builder.setSmallIcon(R.drawable.ic_launcher_background);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(airQualityAnalytics.this);
                    if (ActivityCompat.checkSelfPermission(airQualityAnalytics.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    managerCompat.notify(2, builder.build());
                }

                String pressureString = list.get(4);
                String [] value4 = pressureString.split(": ", 2);
                double pressureValue = Double.parseDouble(value4[1]);

                if(pressureValue > 1000) {
                    //notification code for high pressure warning
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(airQualityAnalytics.this, "My Notification");
                    builder.setContentTitle("Warning: There is high pressure levels");
                    builder.setContentText("Make sure to have an inhaler if needed or leave the premises!");
                    builder.setSmallIcon(R.drawable.ic_launcher_background);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(airQualityAnalytics.this);
                    if (ActivityCompat.checkSelfPermission(airQualityAnalytics.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    managerCompat.notify(3, builder.build());
                }

                String temperatureString = list.get(5);
                String [] value5 = temperatureString.split(": ", 2);
                double temperatureValue = Double.parseDouble(value5[1]);

                if(temperatureValue > 32.00000) {
                    //notification code for high temperature warning
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(airQualityAnalytics.this, "My Notification");
                    builder.setContentTitle("Warning: You are in a high temperature area");
                    builder.setContentText("Make sure to wear appropriate clothing or leave the premises!");
                    builder.setSmallIcon(R.drawable.ic_launcher_background);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(airQualityAnalytics.this);
                    if (ActivityCompat.checkSelfPermission(airQualityAnalytics.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    managerCompat.notify(4, builder.build());
                }
//                String tVOCString = list.get(7);
//                String [] value7 = tVOCString.split(": ", 2);
//                double tVOCValue = Double.parseDouble(value7[1]);
//
//                if(tVOCValue > 0.1) {
//                    //notification code for high tVOC warning
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(airQualityAnalytics.this, "My Notification");
//                    builder.setContentTitle("Warning: There are high amounts of particles in this area");
//                    builder.setContentText("Make sure to wear a mask or leave the premises!");
//                    builder.setSmallIcon(R.drawable.ic_launcher_background);
//                    builder.setAutoCancel(true);
//
//                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(airQualityAnalytics.this);
//                    if (ActivityCompat.checkSelfPermission(airQualityAnalytics.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    managerCompat.notify(5, builder.build());
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     *
     * @param item The menu item that was selected.
     *        Method created to prompt the back button so that we could return to home page
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //go back to main activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param value: this is going to be taking the temperature value in celsius and convert it in Fahrenheit
     * @return
     */
    private String convertToFahrenheit(Float value){
        return String.valueOf((value*1.80000)+32.00);
    }

    /**
     * @param value: this is going to be taking the value of the pressure and is going to be
     *        converting it into Bar
     * @return
     */
    private String convertToBar(Float value){
        return String.valueOf(value * 0.01);
    }
}
