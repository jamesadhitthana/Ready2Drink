package com.example.james.jamesbluetootharduinobaru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsJames extends AppCompatActivity {
    Switch switchSettings;
    Button buttonBackSettings;
    Boolean modeColdToHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_james);
        Bundle extras = getIntent().getExtras();

        buttonBackSettings = (Button) findViewById(R.id.buttonBackSettings);
        switchSettings = (Switch) findViewById(R.id.switchSettings);

        //Load switch
        modeColdToHot = extras.getBoolean("currentAppMode");
        if (modeColdToHot == true) {
            switchSettings.setChecked(true);
        } else {
            switchSettings.setChecked(false);
        }

        buttonBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToHome();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //back to home screen function that gives the switch mode back to app thread
    private void backToHome() {
        String valueToSendHome = "";
        if (switchSettings.isChecked() == true) {
            valueToSendHome = "true";
        }
        if (switchSettings.isChecked() == false) {
            valueToSendHome = "false";
        }
        //Send back home
        try {
            Intent i = new Intent();
            i.putExtra("modeKu", valueToSendHome);
            setResult(RESULT_OK, i); //sending an ok result
            finish();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error happened buttonBackSettings- james", Toast.LENGTH_SHORT).show();
        }
    }

}
