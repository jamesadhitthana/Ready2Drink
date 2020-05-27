package com.example.james.jamesbluetootharduinobaru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

public class PresetsJames extends AppCompatActivity {
    Button goToMain, buttonRoomTemp, buttonCoffee, buttonTea,buttonMilk, buttonChocolate, buttonCancel;
    NumberPicker numberPickerTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets_james);

        Bundle extras = getIntent().getExtras();
        int currentTargetTemp = extras.getInt("currentTargetTemp");

        numberPickerTemp = (NumberPicker) findViewById(R.id.numberPickerTemp);
        numberPickerTemp.setValue(currentTargetTemp);
        goToMain = (Button) findViewById(R.id.buttonCustomPage);
        buttonRoomTemp = (Button) findViewById(R.id.buttonRoomTemp);
        buttonCoffee = (Button) findViewById(R.id.buttonCoffee);
        buttonMilk = (Button)findViewById(R.id.buttonMilk);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonTea = (Button) findViewById(R.id.buttonTea);
        buttonChocolate = (Button) findViewById(R.id.buttonChocolate);

        buttonRoomTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerTemp.setValue(26);
            }
        });

        buttonCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerTemp.setValue(58); //research from university of texas
                //https://www.ncbi.nlm.nih.gov/pubmed/18226454
            }
        });

        buttonMilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerTemp.setValue(65);
            }
        });
        buttonTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerTemp.setValue(60);
            }
        });
        buttonChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerTemp.setValue(52);//research from thermoworks.com
                //https://blog.thermoworks.com/beverages/hot-chocolate-best-serving-temperature/
            }
        });

        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent();
                    i.putExtra("msgKu", Integer.toString(numberPickerTemp.getValue()));
                    setResult(RESULT_OK, i); //sending an ok result
                    finish();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened goToMain - james", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Bundle extras = getIntent().getExtras();
                    final int currentTargetTemp = extras.getInt("currentTargetTemp");
                    Intent i = new Intent();
                    i.putExtra("msgKu", Integer.toString(currentTargetTemp));
                    setResult(RESULT_OK, i); //sending an ok result
                    finish();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened buttonCancel- james", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                Intent i = new Intent();
                i.putExtra("msgKu", Integer.toString(numberPickerTemp.getValue()));
                setResult(RESULT_OK, i); //sending an ok result
                finish();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error happened onKeyDown- james", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
