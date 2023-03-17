package com.example.twisterfinger.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.twisterfinger.R;

public class TitleActivity extends AppCompatActivity {

    private Button oneFingerBtn;
    private Button twoFingerBtn;
    private Button threeFingerBtn;
    private Button calibrateLightBtn;

    private View.OnClickListener fingerButtonListener;

    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        setContentView(R.layout.activity_title);
        setButtons();
        setButtonListeners();
    }

    private void setButtons() {
        oneFingerBtn = findViewById(R.id.oneFingerBtn);
        twoFingerBtn = findViewById(R.id.twoFingerBtn);
        threeFingerBtn = findViewById(R.id.threeFingerBtn);
        calibrateLightBtn = findViewById(R.id.calibrateLightBtn);
    }

    private void setButtonListeners() {
        // Setting finger buttons listener
        fingerButtonListener = view -> {
            switch (view.getId()) {
                case(R.id.oneFingerBtn) :
                    saveFingerNumber(1);
                    break;
                case(R.id.twoFingerBtn) :
                    saveFingerNumber(2);
                    break;
                case(R.id.threeFingerBtn) :
                    saveFingerNumber(3);
                    break;
                default :
                    System.out.println("Unknown button id, setting 1 as default difficulty;");
                    saveFingerNumber(1);
                    break;
            }
            startGameActivity();
        };
        oneFingerBtn.setOnClickListener(fingerButtonListener);
        twoFingerBtn.setOnClickListener(fingerButtonListener);
        threeFingerBtn.setOnClickListener(fingerButtonListener);

        // Setting calibrateLightBtn listener
        calibrateLightBtn.setOnClickListener(view -> calibrateLight());

    }

    private void saveFingerNumber(int nbFingers) {
        SharedPreferences prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("nbFingers", nbFingers);
        prefs.edit().apply();
    }

    private void calibrateLight(){
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        final float[] lightLevel = {0f};
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                lightLevel[0] = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }, lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        prefs.edit().putFloat("lightLevel", lightLevel[0]);
        prefs.edit().apply();

        CharSequence text = "Lumière calibrée !";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(appContext, text, duration);
        toast.show();
    }

    private void startGameActivity() {

    }
}