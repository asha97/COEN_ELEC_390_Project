package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatisticsPage extends AppCompatActivity {
    private StatisticsHelper statisticsHelper;

    private TextView min_alt;
    private TextView min_co2;
    private TextView min_gas;
    private TextView min_hum;
    private TextView min_pre;
    private TextView min_tmp;
    private TextView min_voc;

    private TextView max_alt;
    private TextView max_co2;
    private TextView max_gas;
    private TextView max_hum;
    private TextView max_pre;
    private TextView max_tmp;
    private TextView max_voc;

    private TextView avg_alt;
    private TextView avg_co2;
    private TextView avg_gas;
    private TextView avg_hum;
    private TextView avg_pre;
    private TextView avg_tmp;
    private TextView avg_voc;

    private TextView med_alt;
    private TextView med_co2;
    private TextView med_gas;
    private TextView med_hum;
    private TextView med_pre;
    private TextView med_tmp;
    private TextView med_voc;

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        statisticsHelper = (StatisticsHelper) intent.getSerializableExtra("StatisticsHelper");

        min_alt=(TextView) findViewById(R.id.min_alt_tv);
        min_co2=(TextView) findViewById(R.id.min_co2_tv);
        min_gas=(TextView) findViewById(R.id.min_gas_tv);
        min_hum=(TextView) findViewById(R.id.min_hum_tv);
        min_pre=(TextView) findViewById(R.id.min_pre_tv);
        min_tmp=(TextView) findViewById(R.id.min_tmp_tv);
        min_voc=(TextView) findViewById(R.id.min_voc_tv);

        max_alt=(TextView) findViewById(R.id.max_alt_tv);
        max_co2=(TextView) findViewById(R.id.max_co2_tv);
        max_gas=(TextView) findViewById(R.id.max_gas_tv);
        max_hum=(TextView) findViewById(R.id.max_hum_tv);
        max_pre=(TextView) findViewById(R.id.max_pre_tv);
        max_tmp=(TextView) findViewById(R.id.max_tmp_tv);
        max_voc=(TextView) findViewById(R.id.max_voc_tv);

        avg_alt=(TextView) findViewById(R.id.avg_alt_tv);
        avg_co2=(TextView) findViewById(R.id.avg_co2_tv);
        avg_gas=(TextView) findViewById(R.id.avg_gas_tv);
        avg_hum=(TextView) findViewById(R.id.avg_hum_tv);
        avg_pre=(TextView) findViewById(R.id.avg_pre_tv);
        avg_tmp=(TextView) findViewById(R.id.avg_tmp_tv);
        avg_voc=(TextView) findViewById(R.id.avg_voc_tv);

        med_alt=(TextView) findViewById(R.id.med_alt_tv);
        med_co2=(TextView) findViewById(R.id.med_co2_tv);
        med_gas=(TextView) findViewById(R.id.med_gas_tv);
        med_hum=(TextView) findViewById(R.id.med_hum_tv);
        med_pre=(TextView) findViewById(R.id.med_pre_tv);
        med_tmp=(TextView) findViewById(R.id.med_tmp_tv);
        med_voc=(TextView) findViewById(R.id.med_voc_tv);

        Resources res = getResources();

        String temp = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_Altitude())));
        min_alt.setText(temp);

        String minCo2 = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_co2())));
        min_co2.setText(minCo2);

        String minGas = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_Gas())));
        min_gas.setText(minGas);

        String minHum = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_Humidity())));
        min_hum.setText(minHum);

        String minPre = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_Pressure())));
        min_pre.setText(minPre);

        String minTmp = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_Temperature())));
        min_tmp.setText(minTmp);

        String minVoc = String.format(res.getString(R.string.minimum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMin_tVOC())));
        min_voc.setText(minVoc);

        String maxAlt = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_Altitude())));
        max_alt.setText(maxAlt);

        String maxCo2 = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_co2())));
        max_co2.setText(maxCo2);

        String maxGas = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_Gas())));
        max_gas.setText(maxGas);

        String maxHum = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_Humidity())));
        max_hum.setText(maxHum);

        String maxPre = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_Pressure())));
        max_pre.setText(maxPre);

        String maxTmp = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_Temperature())));
        max_tmp.setText(maxTmp);

        String maxVoc = String.format(res.getString(R.string.maximum), String.valueOf(decimalFormat.format(statisticsHelper.calculateMax_tVOC())));
        max_voc.setText(maxVoc);

        String avgAlt = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_Altitude())));
        avg_alt.setText(avgAlt);

        String avgCo2 = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_co2())));
        avg_co2.setText(avgCo2);

        String avgGas = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_Gas())));
        avg_gas.setText(avgGas);

        String avgHum = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_Humidity())));
        avg_hum.setText(avgHum);

        String avgPre = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_Pressure())));
        avg_pre.setText(avgPre);

        String avgTmp = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_Temperature())));
        avg_tmp.setText(avgTmp);

        String avgVoc = String.format(res.getString(R.string.average), String.valueOf(decimalFormat.format(statisticsHelper.calculateMean_tVOC())));
        avg_voc.setText(avgVoc);

        String medAlt = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_Altitude())));
        med_alt.setText(medAlt);

        String medCo2 = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_Co2())));
        med_co2.setText(medCo2);

        String medGas = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_Gas())));
        med_gas.setText(medGas);

        String medHum = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_Humidity())));
        med_hum.setText(medHum);

        String medPre = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_Pressure())));
        med_pre.setText(medPre);

        String medTmp = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_Temperature())));
        med_tmp.setText(medTmp);

        String medVoc = String.format(res.getString(R.string.median), String.valueOf(decimalFormat.format(statisticsHelper.calculateMedian_tVOC())));
        med_voc.setText(medVoc);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}