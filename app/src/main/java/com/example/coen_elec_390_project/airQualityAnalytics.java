package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


public class airQualityAnalytics extends AppCompatActivity {
    private BarChart barChart;
    private DatabaseReference reference;
    private ListView listView;

    Button notifyButton;

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

        // container for data
        BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>(), "Sensor Data");

        // colors of chart
        dataSet.setColors(new int[] { R.color.purple_500 });
        dataSet.setValueTextColor(R.color.black);

        // add data of barchart into object
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();

        //display into the box the right metrics
        String [] nameMetric = {"Altitude (m)", "CO2 (ppm)", "Gas (KOhms)", "Humidity (%)", "Pressure (hPa)", "Temperature (*C)", "Elapsed Time (ms)", "tVOC (g*m^-3)" };

        // Attach a ValueEventListener to the Firebase database node to get the data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear data to make sure there isn't anything else
                dataSet.clear();
                list.clear();

                // looping in data
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    float value = Float.parseFloat(snapshot.getValue().toString());
                    //not going to be displaying the elapsed time
                    if (i !=6) {
                        dataSet.addEntry(new BarEntry(i, value, nameMetric[i]));
                        String addData = nameMetric[i] + ": " + snapshot.getValue().toString();
                        if (nameMetric[i].equals("Temperature (*C)")){
                            String addFahrenheit = "Temperature (*F): " + convertToFahrenheit(snapshot.getValue().toString());
                            list.add(addFahrenheit);
                        }
                        if (nameMetric[i].equals("Pressure (hPa)")){
                            String addBar = "Pressure (Bar): " + convertToBar(snapshot.getValue().toString());
                            list.add(addBar);
                        }
                        //we want to make sure that the air quality particles are not surpassing the threshold
                        if (nameMetric[i].equals("CO2 (ppm)")){
                            int co2PPM = Integer.parseInt(snapshot.getValue().toString());
                        }

                        if (nameMetric[i].equals("Gas (KOhms)")){
                            double gasMetric = Double.parseDouble(snapshot.getValue().toString());
                        }

                        list.add(addData);
                        i++;
                    }
                }

                // notification that data in chart has been updated
                barData.notifyDataChanged();
                barChart.notifyDataSetChanged();
                barChart.invalidate();
                adapter.notifyDataSetChanged();

                //----------------------CREATION OF NOTIFICATIONS--------------------------//
                String co2String = list.get(1);
                String[] value1 = co2String.split(": ", 2);
                double co2Value = Double.parseDouble(value1[1]);

//                String gasString = list.get(2);
//                String [] value2 = gasString.split(": ", 2);
//                double gasValue = Double.parseDouble(value2[1]);

                if(co2Value > 1000) {
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

                String tVOCString = list.get(7);
                String [] value7 = tVOCString.split(": ", 2);
                double tVOCValue = Double.parseDouble(value7[1]);

                if(tVOCValue > 0.001) {
                    //notification code for high tVOC warning
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(airQualityAnalytics.this, "My Notification");
                    builder.setContentTitle("Warning: There are high amounts of particles in this area");
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
                    managerCompat.notify(5, builder.build());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
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

    private String convertToFahrenheit(String value){
        double celsiusValue = Double.parseDouble(value);
        return String.valueOf((celsiusValue*1.80000)+32.00);
    }

    private String convertToBar(String value){
        double hpaValue = Double.parseDouble(value);
        return String.valueOf((hpaValue * 0.001));
    }
}
