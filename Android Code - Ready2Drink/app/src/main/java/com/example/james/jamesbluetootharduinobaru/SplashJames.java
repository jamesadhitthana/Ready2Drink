package com.example.james.jamesbluetootharduinobaru;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SplashJames extends AppCompatActivity {
    Button startApp;
    /**
     * Duration of wait
     **/
    private int splashTime = 1250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_james);

        startApp = (Button) findViewById(R.id.buttonSplash);

        startApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "James Adhitthana - 00000021759 \n Christopher Yefta - 00000026157", Toast.LENGTH_SHORT).show();
            }
        });


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTime);
    }


}
