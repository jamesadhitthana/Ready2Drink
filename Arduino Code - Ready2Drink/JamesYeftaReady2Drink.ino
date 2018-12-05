//James Adhitthana - 00000021759
//Christopher Yefta - 00000026157
//--Libraries--//
#include <SoftwareSerial.h>    // import the serial library for bluetooth
#include <LiquidCrystal.h>     //library for the LCD display attached to the Arduino
#include <OneWire.h>           //library for 'one wire' cable attached to the probe
#include <DallasTemperature.h> //library for reading the temperature

//--Configuring Bluetooth--//
SoftwareSerial JamesBluetooth(9, 10); // TX = Pin 9 , RX = Pin 10
String BluetoothData = "";            // the data given from Computer

//--Configuring Temperature Probe--//
#define ONE_WIRE_BUS A3              //Temperature Probe = Pin A3
OneWire oneWire(ONE_WIRE_BUS);       //Configure temperature probe to use OneWire library
DallasTemperature sensors(&oneWire); //Configure temperature probe to use DallasTemperature library

DeviceAddress Thermometer = {0x28, 0xA0, 0xEE, 0x77, 0x91, 0x12, 0x2, 0x56}; //Address of the hardware that we are using right now that we bought on tokopedia (different vendors sell different hardware)

//--Configuring LCD Display--//
const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2; //LCD pins with the associated Arduino input pins
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);                  // initialize the library by associating any needed LCD interface pin with the arduino pin number it is connected to

byte smileyPicture[8] = {
    B00000,
    B10001,
    B00000,
    B00000,
    B10001,
    B01110,
    B00000,
};
byte thermometerPicture[8] = {
    B00100,
    B01010,
    B01010,
    B01110,
    B01110,
    B11111,
    B11111,
    B01110,
};
void setup()
{
  //--Bluetooth Setup--//
  JamesBluetooth.begin(9600); //Setting up bluetooth with the appropriate baud rate
  //JamesBluetooth.println("WASSUP MA BOI!");//TODO: check masih jalan atau ga app nya - barusan dihapus

  //--LCD Setup--//
  Serial.begin(9600);//Setting up LCD and Serial Monitor with the appropriate baud rate

  //--Temperature Probe Sensor Setup--//
  sensors.begin(); //Begin temperature probe measurement
  sensors.setResolution(Thermometer, 10); //Change resolution to output 10 bits so that we get increments of 0.25C, and 187.5ms to measure temperature

//--LCD Additional Setup After Temperature Probe is activated--//
  lcd.createChar(0, smileyPicture); //make char #0 (smileyPicture)
  lcd.createChar(1, thermometerPicture); //make char #1 (thermometerPicture)

  lcd.begin(16, 2); // set up the LCD's number of columns and rows:
  
  lcd.print("Ready2Drink App"); // Print a message to the LCD.
  lcd.write(byte(0)); //Print char #0 (smileyPicture face )
}

//---Function for printing temperature---//
float printTemperature(DeviceAddress deviceAddress)
{
  float tempC = sensors.getTempC(deviceAddress); //get temperature from the sensor
  if (tempC == -127.00) //this is the sensor's default error value, so it checks if the sensor is this temp and prints an error if it is
  {
    Serial.print("Error getting temperature!"); //prints error if sensor is faulty
  }
  else //if sensor is not faulty, then print the temperature to serial monitor
  {
    Serial.print("C: ");
    Serial.print(tempC);
  }
}

void loop()
{
  //--LCD: Reading and Printing the Results --//
  lcd.setCursor(0, 1);  // set the cursor to the second row (column 0, row 1 [1 being the second row])
  lcd.write(byte(1)); //Print char #1 (thermometerPicture)
  lcd.print(sensors.getTempC(Thermometer)); //Print the current temperature to the LCD
  lcd.print((char)223); //Print degrees symbol
  lcd.print("C");//Print 'C' character for celsius
  lcd.write(byte(0)); //call char #0 (smileyPicture)

   //--Thermometer: Reading and Printing the thermometer temperatures --//
  delay(500); //Delay to prepare bluetooth device and sensor to prevent faulty reading
  sensors.requestTemperatures(); //Get the temperatures from the sensor//
  printTemperature(Thermometer); //Print temperature of Thermometer//
  Serial.print("\n\r");//Print in the formatting

  //--Bluetooth: Getting the temperatures and Sending the temperatures through bluetooth --//
  JamesBluetooth.print(sensors.getTempC(Thermometer)); //Send the current temperatures through bluetooth
  JamesBluetooth.println("C");//Send a 'C' character at the end to tell the bluetooth receiver it is celsius (used in the application programming later)
}