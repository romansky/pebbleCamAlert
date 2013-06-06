package com.fun.radarpusht;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startBackground();
            }
        });
    }


    public void startBackground() {
        Toast.makeText(this, "Starting background service..", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), RadarService.class);
        startService(intent);
    }
}
