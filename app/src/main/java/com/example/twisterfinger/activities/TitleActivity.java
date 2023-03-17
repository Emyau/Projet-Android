package com.example.twisterfinger.activities;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.twisterfinger.R;

public class TitleActivity extends AppCompatActivity {

    private Button oneFingerBtn;
    private Button twoFingerBtn;
    private Button threeFingerBtn;
    private Button calibrateLightBtn;
    private ImageButton calibrateInfoBtn;

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
        calibrateInfoBtn = findViewById(R.id.calibrateInfoBtn);
    }

    private void setButtonListeners() {
        // Setting finger buttons listener
        fingerButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        };
        oneFingerBtn.setOnClickListener(fingerButtonListener);
        twoFingerBtn.setOnClickListener(fingerButtonListener);
        threeFingerBtn.setOnClickListener(fingerButtonListener);

        // Setting calibrateLightBtn listener
        calibrateLightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calibrateLight();
            }
        });

        // Setting calibrateInfoBtn listener
        calibrateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCalibrateInfo();
            }
        });

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

    private void displayCalibrateInfo() {
        CharSequence text = "Ce jeu va vous demander de cacher la caméra pour assombrir ou éclaircir les couleurs des cases. Calibrez la lumière pour définir la lumière ambiante comme luminosité maximale";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(appContext, text, duration);
        toast.show();
    }

    private void startGameActivity() {

    }
}