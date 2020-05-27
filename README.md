<h1 align="center">
    Ready2Drink
</h1>
<p align="center">
<sup>
<b>Optimize hot or cold drinks for maximum taste and receive notifications when your drinks are "ready to drink!"</b>
</sup>
</p>


Ready2Drink is an Android app connected via Bluetooth to an Arduino with a DS18B20 temperature probe that allows you to monitor hot or cold drinks and receive notifications when your drinks are "ready to drink!"

Through extensive research, we have found the optimal temperature for you to enjoy the maximum taste you can get from your drinks. Using our "preset" system, you can select a drink (for example, hot coffee or tea), and the app monitors your drink until it reaches the optimal temperature. You can even view the temperature on your phone in real-time to see your drink's current temperature. When it reaches the temperature, a notification is sent to your phone via Bluetooth telling you that your drink is "ready to drink!"

![tutorial2](https://raw.githubusercontent.com/jamesadhitthana/UPH_SentimentForecast/master/Screenshots/Tutorial2.jpg)


# Using it is as easy as 1-2-3:
1. Open the app and choose a drink to optimize
2. Enter the temperature probe into your drink
3. When the notification on your phone rings and appears, your drink is ready to drink!

# How it Works:
Ready2Drink utilizes an Arduino microcontroller connected to a stainless steel DS18B20 temperature probe that you insert in your drink tomonitor the drink's real-time temperature. Using Bluetooth, the Arduino microcontroller reads the real-time temperature in your drink and then sends the drink's temperature to the android app. The application automatically monitors the temperature and provides notification when the drink temperature is optimal to drink (usually 49 °C up to 60°C for hot coffee). When the drink is ready, the app automatically sends a notification. Then you can remove the temperature probe and rinse the probe with tap water and enjoy your drink.

# Libraries Needed for Arduino
- OneWire.h
- DallasTemperature.h
- SoftwareSerial.h
- LiquidCrystal.h

# Software Needed
- Arduino IDE
- Android Studio

# Materials
- 1x Android Phone
- 1x Arduino Uno
- 1x Breadboard (400 points)
- 1x DS18B20 Temperature Sensor
- 1x HC-05 Bluetooth Module
- 1x Potensiometer
- 1x LCD 1602 (16X2) Display
- 1x PIN HEADER SET (Untuk di solder ke LCD)
- Jumper Cables
- 2x Resistor 330 OHM
- 2x Resistor 10K OHM
- 2x Resistor 1K OHM
- 1x Soldering iron
- 1x Timah solder
