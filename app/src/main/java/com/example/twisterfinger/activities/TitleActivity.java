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
import android.preference.PreferenceManager;
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

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        setContentView(R.layout.activity_title);
        prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
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
        prefs.edit().putInt("nbFingers", nbFingers);
        prefs.edit().apply();
    }

    private void calibrateLight(){
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        SharedPreferences prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (lightSensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
        }

        SensorEventListener lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
                if (prefs.getFloat("coefLumi", 0) == 0) {
                    editor.putFloat("coefLumi", value);
                    editor.apply();
                    CharSequence text = "Lumière calibrée !";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(appContext, text, duration);
                    toast.show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //osef
            }
        };
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void startGameActivity() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        editor.putFloat("coefLumi", 0);
        editor.apply();
    }

}