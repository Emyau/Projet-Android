package com.example.twisterfinger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.Random;

public class FreezeHandler {

    public enum FrozenState {
        UNFROZEN,
        MID_FROZEN,
        FULL_FROZEN
    }

    private Random random = new Random();
    private int freezeChance = 0;
    private static final int MAX_FREEZE_CHANCE = 100;
    private FrozenState frozenState = FrozenState.UNFROZEN;

    private float[] accelVector = {0f, 0f, 0f};

    SensorEventListener listenerAccel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            accelVector[0] = Math.abs(event.values[0]);
            accelVector[1] = Math.abs(event.values[1]);
            accelVector[2] = Math.abs(event.values[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //not used
        }
    };

    public FreezeHandler(Context context) {
        setAccelSensor(context);
    }

    private void setAccelSensor(Context context) {
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(listenerAccel, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Check probability of freeze, if freeze fails, increase probability for next attempt
    public void attemptFreeze() {
        int chance = random.nextInt(100);
        if (chance + freezeChance > 100) {
            triggerFreeze();
            freezeChance = 0;
        } else {
            freezeChance += 25;
        }
    }

    private void triggerFreeze() {
        frozenState = FrozenState.FULL_FROZEN;
        Log.d("DEV", "FULL_FROZEN");
        waitForUnfreeze();
    }

    private void waitForUnfreeze() {
        if (frozenState != FrozenState.UNFROZEN) {
            if ((accelVector[0] > 10 && accelVector[0] < 15) ||
                    (accelVector[1]> 10 && accelVector[1]< 15) ||
                    (accelVector[2]> 10 && accelVector[2]< 15)) {
                Log.d("DEV", "MID_FROZEN");
                frozenState = FrozenState.MID_FROZEN;
            }
            // Big shake check
            if (accelVector[0] > 15 || accelVector[1]> 15 || accelVector[2]> 15) {
                Log.d("DEV", "UNFROZEN");
                frozenState = FrozenState.UNFROZEN;
            }
        }
    }

    public FrozenState getFrozenState() {
        return this.frozenState;
    }
}
