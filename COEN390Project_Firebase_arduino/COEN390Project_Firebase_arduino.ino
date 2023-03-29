//Time, WiFi and Firebase Libraries
#include <time.h>
#include <WiFi.h>
#include <Firebase_ESP_Client.h>
//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"


//Adafruit BME680 Libraries
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"

//Sparkfun CCS811 Libraries
#include <Wire.h>
#include "SparkFunCCS811.h" //Click here to get the library: http://librarymanager/All#SparkFun_CCS811



// Insert your network credentials
#define WIFI_SSID "O"
#define WIFI_PASSWORD "papaya00"


// Insert Firebase project API Key
#define API_KEY "AIzaSyDxbx2Rge58dtOSzhGUTmYVjDrjGUHdfPo"
// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://dermair-app-default-rtdb.firebaseio.com/" 

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

//Adafruit BME680 Variable Declarations
unsigned long sendDataPrevMillis = 0;
bool signupOK = false;
float Temperature = 0.0;
float Pressure = 0.0;
float Humidity = 0.0;
float Gas = 0.0;
float AA = 0.0; //Approximate Altitude

//Sparkfun CCS811 Variable Declarations
float CO2 = 0.0;
float tVOC = 0.0;
float elapsedTime = 0.0;




//Adafruit BME680 Pin Definitions
#define BME_SCK 13
#define BME_MISO 12
#define BME_MOSI 11
#define BME_CS 10

#define SEALEVELPRESSURE_HPA (1013.25)

Adafruit_BME680 bme; // I2C
//Adafruit_BME680 bme(BME_CS); // hardware SPI
//Adafruit_BME680 bme(BME_CS, BME_MOSI, BME_MISO,  BME_SCK);


//Sparkfun CCS811 Pin Definitions
#define CCS811_ADDR 0x5B //Default I2C Address
//#define CCS811_ADDR 0x5A //Alternate I2C Address

CCS811 mySensor(CCS811_ADDR);


// To make the Blue LED Blink for debugging purposes
#define LED 2

void setup(){
  Serial.begin(9600);

  // To make the Blue LED Blink for debugging purposes
  pinMode(LED,OUTPUT);

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

  //WiFi Protocols + Connection to Firebase
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("\n\n\nConnecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop(){
  //BME 680 Loop
  if (! bme.performReading()) {
    Serial.println("Failed to perform reading :(");
    return;
  }




  // Sparkfun CCS811 loop
  //Serial.print("CCS811 Readings:\t");
  //Check to see if data is ready with .dataAvailable()
  if (mySensor.dataAvailable())
  {
    //If so, have the sensor read and calculate the results.
    //Get them later
    mySensor.readAlgorithmResults();

  }

  //Repeatedly send data to firebase 
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();


    //Creating variables to store in Firebase
    //Adafruit BME680
    Temperature = (float)bme.temperature;
    Pressure = (float)bme.pressure/100.0;
    Humidity = (float)bme.humidity;
    Gas = (float)bme.gas_resistance / 1000.0;
    AA = (float)bme.readAltitude(SEALEVELPRESSURE_HPA)+90;

    //Sparkfun CCS811
    CO2 = (float)mySensor.getCO2();
    tVOC = (float)mySensor.getTVOC();
    elapsedTime = (float)millis();
    
    


    // Storing each variable on the database path
    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/Temperature(*C)", Temperature)){
      Serial.println("PASSED");
      Serial.print(Temperature);Serial.println(" °C");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }


    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/Pressure(hPa)", Pressure)){
      Serial.println("PASSED");
      Serial.print(Pressure);Serial.println(" hPa");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }

    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/Humidity(percent)", Humidity)){
      Serial.println("PASSED");
      Serial.print(Humidity);Serial.println(" %");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }

    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/Gas(KOhms)", Gas)){
      Serial.println("PASSED");
      Serial.print(Gas);Serial.println(" kOhms");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }

    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/Approximate_Altitude(m)", AA)){
      Serial.println("PASSED");
      Serial.print(AA);Serial.println(" m");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }

    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/CO2(ppm)", CO2)){
      Serial.println("PASSED");
      Serial.print(CO2);Serial.println(" ppm");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }

    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/tVOC(g*m^-3)", tVOC)){
      Serial.println("PASSED");
      Serial.print(tVOC);Serial.println(" g/m^3");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }

    if (Firebase.RTDB.setFloat(&fbdo, "Sensor/elapsedTime(ms)", elapsedTime)){
      Serial.println("PASSED");
      Serial.print(elapsedTime);Serial.println(" ms");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("\tTYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("\tTYPE: " + fbdo.dataType());
      Serial.println("REASON: " + fbdo.errorReason());
    }   
  }
}