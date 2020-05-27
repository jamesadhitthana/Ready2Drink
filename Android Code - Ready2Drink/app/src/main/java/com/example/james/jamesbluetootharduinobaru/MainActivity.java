package com.example.james.jamesbluetootharduinobaru;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private final String DEVICE_ADDRESS = "00:21:13:00:2A:21"; //MAC Address of the bluetooth module (hardcoded on)//
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");// Universal standard Bluetooth serial board DSerial Port Service UUID
    RelativeLayout layoutKu;
    Button startButton, stopButton, customButton, presetButton, buttonSettings;
    TextView textView, tempLabel, labelTargetTemp, labelStatus;
    boolean deviceConnected = false;
    boolean modeColdToHot = false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    double currentTemp = -100.00;
    String temperatureRaw;
    String temperatureProcessed;
    boolean targetTempIsReached = false; //TODO: james
    int targetTemp = 30; //TODO: james
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_james);

        layoutKu = (RelativeLayout) findViewById(R.id.layoutKu);
        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);
        customButton = (Button) findViewById(R.id.button3);
        presetButton = (Button) findViewById(R.id.button4);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        textView = (TextView) findViewById(R.id.textView);
        tempLabel = (TextView) findViewById(R.id.tempLabel);
        tempLabel.setText("-\u2103");
        labelTargetTemp = (TextView) findViewById(R.id.labelTargetTemp);
        labelStatus = (TextView) findViewById(R.id.labelStatus);

        setUiEnabled(false);

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), CustomJames.class);
                    i.putExtra("currentTargetTemp", targetTemp);
                    startActivityForResult(i, 2);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened - james", Toast.LENGTH_SHORT).show();
                }
            }
        });

        presetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), PresetsJames.class);
                    i.putExtra("currentTargetTemp", targetTemp);
                    startActivityForResult(i, 3);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened - james", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), SettingsJames.class);
                    i.putExtra("currentAppMode", modeColdToHot);
                    startActivityForResult(i, 4);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened - james", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Tap Target methods (INTRO TUTORIAL PART)//
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.tempLabel), "View Current Temperature ", "Your current drink temperature goes here").outerCircleColor(R.color.jamesOrange).targetRadius(80),
                        TapTarget.forView(findViewById(R.id.labelTargetTemp), "View Target Temperature ", "Your target temperature goes here").outerCircleColor(R.color.jamesOrange).targetRadius(70),
                        TapTarget.forView(findViewById(R.id.labelStatus), "Drink Status ", "See if we are monitoring your drink temperature or if your drink is ready!").outerCircleColor(R.color.jamesOrange),
                        TapTarget.forView(findViewById(R.id.buttonSettings), "Configure ", "Our app has two different modes, Hot to Cold (enabled by default) or Cold to Hot!").outerCircleColor(R.color.jamesOrange),
                        TapTarget.forView(findViewById(R.id.button3), "Customize ", "You can customize the temperature you want here!").outerCircleColor(R.color.jamesOrange),
                        TapTarget.forView(findViewById(R.id.button4), "Use our Presets ", "You can also use our presets for the optimum temperature of drinks according to experts").outerCircleColor(R.color.jamesOrange),
                        TapTarget.forView(findViewById(R.id.buttonStart), "Start ", "That's it! You can start to use ready to drink by pressing the button here. Don't forget to pair your device before using the app!").outerCircleColor(R.color.jamesOrange))
                .start();
        //END OF: Tap Target methods (INTRO TUTORIAL PART)//
    }//END OF onCreate

    public void onResume() {
        super.onResume();
        labelTargetTemp.setText("Target Temp: " + targetTemp + "\u2103");
    }

    // Call Back method  to get the Message from other Activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed here it is 2
        if (requestCode == 2) {
            targetTemp = Integer.parseInt(data.getStringExtra("msgKu"));
            targetTempIsReached = false;
            labelStatus.setText("Status: Monitoring drink temperature");

        }
        if (requestCode == 3) {
            targetTemp = Integer.parseInt(data.getStringExtra("msgKu"));
            targetTempIsReached = false;
            labelStatus.setText("Status: Monitoring drink temperature");

        }
        if (requestCode == 4) {
            modeColdToHot = Boolean.parseBoolean(data.getStringExtra("modeKu"));
        }
    }


    //--end of notification
    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(!bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);

    }

    public boolean BTinit() {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Your Device Doesn't Support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the device to the Arduino first before continuing", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect() {
        boolean connected = true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        if (connected) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connected;
    }

    public void onClickStart(View view) {
        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();

        if (BTinit()) {
            if (BTconnect()) {
                setUiEnabled(true);
                deviceConnected = true;
                beginListenForData();
                Toast.makeText(getApplicationContext(), "Ready2Drink Connected!", Toast.LENGTH_SHORT).show();
                startButton.setBackgroundResource(R.drawable.buttonconnected);
                stopButton.setBackgroundResource(R.drawable.buttondisconnectred);
                //TODO make bool
            } else {
                Toast.makeText(getApplicationContext(), "Cannot connect to Arduino. Make sure Arduino is properly setup.", Toast.LENGTH_LONG).show();
            }

        } else {
            //nothing
        }
    }


    void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, "UTF-8");
                            handler.post(new Runnable() {
                                public void run() {
                                    textView.append(string);
                                    //Tambahan james//

                                    //TODO:
                                    if (string.contains("C")) { //ITS WORKING!!
                                        temperatureRaw = textView.getText().toString();//
                                        temperatureProcessed = temperatureRaw.substring(0, temperatureRaw.length() - 3);
                                        try {
                                            double value = Double.parseDouble(temperatureProcessed);
                                            currentTemp = value;
                                            tempLabel.setText(currentTemp + "\u00b0C");
                                        } catch (Exception e) {
                                            Log.e("JamesLOG", "Error convverting to double");
                                        }

                                        if (modeColdToHot == true) {
                                            //--MODE: COLD TO HOT//
                                            if (currentTemp >= targetTemp && targetTempIsReached == false) {
                                                targetTempIsReached = true;
                                                labelStatus.setText("Status: Drink ready!");
                                                if (targetTempIsReached == true) {

                                                    //TODO: Keep this too fo show the notification page
                                                    try {
                                                        //Show the drink ready activity//
                                                        Intent i = new Intent(getApplicationContext(), NotificationView.class);
                                                        startActivity(i);
                                                        //---
                                                    } catch (Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed to show the notification activity", Toast.LENGTH_SHORT).show();
                                                    }

                                                    addNotification(); //TODO: KEEP THIS
                                                }

                                            }
                                            //END OF:  MODE COLD TO HOT--//
                                        } else {
                                            //--MODE: HOT TO COLD//
                                            if (currentTemp <= targetTemp && targetTempIsReached == false) {
                                                targetTempIsReached = true;
                                                labelStatus.setText("Status: Drink ready!");
                                                if (targetTempIsReached == true) {

                                                    //TODO: Keep this too fo show the notification page
                                                    try {
                                                        //Show the drink ready activity//
                                                        Intent i = new Intent(getApplicationContext(), NotificationView.class);
                                                        startActivity(i);
                                                        //---
                                                    } catch (Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed to show the notification activity", Toast.LENGTH_SHORT).show();
                                                    }

                                                    addNotification(); //TODO: KEEP THIS
                                                }

                                            }
                                            //END OF:  MODE HOT TO COLD--//
                                        }


                                        textView.setText(""); //clears the textview so that it doesnt print more lines of temperatures


                                    }

                                    //END OF: Tambahan James

                                }
                            });

                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected = false;
        Toast.makeText(getApplicationContext(), "Ready2Drink Disconnected!", Toast.LENGTH_SHORT).show();
        startButton.setBackgroundResource(R.drawable.buttonconnect);
        stopButton.setBackgroundResource(R.drawable.buttondisconnect);
    }

    //TODO: Notification thing James
    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ready2drinklogoblue) //set icon for notification
                        .setContentTitle("Your drink is ready!") //set title of notification
                        .setContentText("Enjoy your drink!")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(this, NotificationView.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Play default sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        manager.notify(0, builder.build());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //when the app is closed using the back key, this makes sure that bluetooth connection is closed and thread is closed.
            if (deviceConnected == true) {
                try {
                    stopThread = true;
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                    setUiEnabled(false);
                    deviceConnected = false;
                    Toast.makeText(getApplicationContext(), "Ready2Drink Disconnected Successfully.", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ready2Drink had a problem closing the app and connection", Toast.LENGTH_LONG).show();
                }
            }
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
