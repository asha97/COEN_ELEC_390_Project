//Wifi Libraries and Pins
#include "WiFi.h"

#define WIFI_NETWORK "O"
#define WIFI_PASSWORD "papaya02"
#define WIFI_TIMETOUT_MS 20000

// To make the Blue LED Blink for debugging purposes
#define LED 2


//Connecting to WiFi Function
void connectToWiFi(){
  Serial.println("\n\n\nConnecting to Wifi");
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_NETWORK, WIFI_PASSWORD);

  unsigned long startAttemptTime = millis();

  while(WiFi.status() != WL_CONNECTED && millis() - startAttemptTime < WIFI_TIMETOUT_MS){
    Serial.print(".");
    delay(100);
  }


  if(WiFi.status() != WL_CONNECTED){
    Serial.println("Failed");
  }
  else {
  Serial.print("Connected! \t IP: ");
  Serial.println(WiFi.localIP());

  // To make the Blue LED Blink for debugging purposes
  delay(500);
  digitalWrite(LED,HIGH);
  delay(500);
  digitalWrite(LED,LOW);
   

  }


}




//Adafruit BME680 Libraries and Pins
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"

#define BME_SCK 13
#define BME_MISO 12
#define BME_MOSI 11
#define BME_CS 10

#define SEALEVELPRESSURE_HPA (1013.25)

Adafruit_BME680 bme; // I2C
//Adafruit_BME680 bme(BME_CS); // hardware SPI
//Adafruit_BME680 bme(BME_CS, BME_MOSI, BME_MISO,  BME_SCK);




//Sparkfun CCS811 Libraries and Pins
#include <Wire.h>

#include "SparkFunCCS811.h" //Click here to get the library: http://librarymanager/All#SparkFun_CCS811

#define CCS811_ADDR 0x5B //Default I2C Address
//#define CCS811_ADDR 0x5A //Alternate I2C Address

CCS811 mySensor(CCS811_ADDR);

//GYML8511_ESP32 {no libraries} and inputs
int UVOUT = 15; //Output from the sensor
int REF_3V3 = 4; //3.3V power on the Arduino board


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  //WiFi Connection Setup
  // To make the Blue LED Blink for debugging purposes
  pinMode(LED,OUTPUT);
  connectToWiFi();


  // BME680 Setup
  while (!Serial);
  Serial.println(F("BME680 test"));

  if (!bme.begin(0x76)) {
    Serial.println("Could not find a valid BME680 sensor, check wiring!");
    while (1);
  }

  // Set up oversampling and filter initialization
  bme.setTemperatureOversampling(BME680_OS_8X);
  bme.setHumidityOversampling(BME680_OS_2X);
  bme.setPressureOversampling(BME680_OS_4X);
  bme.setIIRFilterSize(BME680_FILTER_SIZE_3);
  bme.setGasHeater(320, 150); // 320*C for 150 ms


  //Sparkfun CCS811 Setup
  Serial.println("CCS811 Basic Example");

  Wire.begin(); //Inialize I2C Hardware

  if (mySensor.begin() == false)
  {
    Serial.print("CCS811 error. Please check wiring. Freezing...");
    while (1)
      ;
  }


  //GYML8511 Setup
  pinMode(UVOUT, INPUT);
  pinMode(REF_3V3, INPUT);

  Serial.println("GYML8511 example");

}

void loop() {
  //BME 680 Loop
  if (! bme.performReading()) {
    Serial.println("Failed to perform reading :(");
    return;
  }

  
  //Serial.print("BME680 Readings:\t");

  //Temperature
  Serial.print("T=");
  Serial.print(bme.temperature);
  Serial.print("°C ");

  //Pressure
  Serial.print("P=");
  Serial.print(bme.pressure / 100.0);
  Serial.print("hPa ");

  //Humidity
  Serial.print("H=");
  Serial.print(bme.humidity);
  Serial.print("% ");

  //Gas
  Serial.print("Gas=");
  Serial.print(bme.gas_resistance / 1000.0);
  Serial.print("KOhms ");

  // AA: Approximate Altitude
  Serial.print("AA=");
  Serial.print(bme.readAltitude(SEALEVELPRESSURE_HPA));
  Serial.print("m  ");







  // Sparkfun CCS811 loop
  //Serial.print("CCS811 Readings:\t");
  //Check to see if data is ready with .dataAvailable()
  if (mySensor.dataAvailable())
  {
    //If so, have the sensor read and calculate the results.
    //Get them later
    mySensor.readAlgorithmResults();

    Serial.print("CO2(ppm)=");
    //Returns calculated CO2 reading
    Serial.print(mySensor.getCO2());
    Serial.print(" tVOC(g/m^3)=");  //Units (g/m^3), may want to change to (mg/m^3)
    //Returns calculated TVOC (Total Volatile Organic Compounds) reading
    Serial.print(mySensor.getTVOC());
    Serial.print(" t(ms)=");
    //Display the time since program start
    Serial.print(millis());
  }


  //GYML8511 Loop



  int uvLevel = averageAnalogRead(UVOUT);
  int refLevel = averageAnalogRead(REF_3V3);
  
  //Use the 3.3V power pin as a reference to get a very accurate output value from sensor
  float outputVoltage = 3.3 / refLevel * uvLevel;
  
  float uvIntensity = mapfloat(outputVoltage, 0.99, 2.9, 0.0, 15.0);

  //UVLevel
  Serial.print(" UVLvl=");
  Serial.print(uvLevel);

  //Voltage
  Serial.print(" V=");
  Serial.print(outputVoltage);

  //UV Intensity
  Serial.print(" UV_I(mW/cm^2)=");
  Serial.print(uvIntensity);  
  
  Serial.println();

  delay(500);
}



//Function for GYML8511
//Takes an average of readings on a given pin
//Returns the average
int averageAnalogRead(int pinToRead)
{
  byte numberOfReadings = 8;
  unsigned int runningValue = 0; 

  for(int x = 0 ; x < numberOfReadings ; x++)
    runningValue += analogRead(pinToRead);
  runningValue /= numberOfReadings;

  return(runningValue);  
}

//The Arduino Map function but for floats
//From: http://forum.arduino.cc/index.php?topic=3922.0
float mapfloat(float x, float in_min, float in_max, float out_min, float out_max)
{ 
  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}