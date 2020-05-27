package com.example.james.jamesbluetootharduinobaru;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationView extends AppCompatActivity {
    ImageView readyImage;
    RelativeLayout layoutNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        layoutNotification = (RelativeLayout) findViewById(R.id.layoutNotification);
        readyImage = (ImageView) findViewById(R.id.readyImage);

        layoutNotification.setOnClickListener(new View.OnClickListener() {
        //when user taps anywhere on the screen, it closes this notification window
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }
}