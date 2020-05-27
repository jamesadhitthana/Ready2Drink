package com.example.james.jamesbluetootharduinobaru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

public class CustomJames extends AppCompatActivity {

    Button goToMain,setTargetTemperature;
    NumberPicker numberPickerTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_james);

        Bundle extras = getIntent().getExtras();
        final int currentTargetTemp = extras.getInt("currentTargetTemp");

        numberPickerTemp = (NumberPicker) findViewById(R.id.numberPickerTemp);
        numberPickerTemp.setValue(currentTargetTemp);
        goToMain = (Button) findViewById(R.id.buttonCustomPage);
        setTargetTemperature = (Button) findViewById(R.id.setTargetTemperature);

        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent();
                    i.putExtra("msgKu",Integer.toString(currentTargetTemp));
                    setResult(RESULT_OK,i); //sending an ok result
                    finish();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened - james", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTargetTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent();
                    i.putExtra("msgKu",Integer.toString(numberPickerTemp.getValue()));
                    setResult(RESULT_OK,i); //sending an ok result
                   finish();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error happened - james", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            try {
                Bundle extras = getIntent().getExtras();
                final int currentTargetTemp = extras.getInt("currentTargetTemp");
                Intent i = new Intent();
                i.putExtra("msgKu",Integer.toString(currentTargetTemp));
                setResult(RESULT_OK,i); //sending an ok result
                finish();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error happened - james", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
