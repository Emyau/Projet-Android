package com.example.twisterfinger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import com.example.twisterfinger.engine.GameEngine;

import java.util.Random;

public class FreezeHandler {

    private final View view;
    private float[] accelVector;
    private GameEngine engine;
    private Bitmap freezeImage;
    private Bitmap freezeCrackImage;

    private enum FrozenState {
        UNFROZEN,
        MID_FROZEN,
        FULL_FROZEN
    }

    private Random random = new Random();
    private int freezeChance = 0;
    private FrozenState frozenState = FrozenState.UNFROZEN;


    public FreezeHandler(GameEngine engine, float[] accelVector, View view) {
        this.engine = engine;
        this.accelVector = accelVector;
        this.view = view;
        createScaledBitmap(view.getContext());
    }

    private void createScaledBitmap(Context context) {
        freezeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.freeze);
        freezeImage = Bitmap.createScaledBitmap(freezeImage, view.getWidth(), view.getHeight(), false);
        freezeCrackImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.freeze_crack);
        freezeCrackImage = Bitmap.createScaledBitmap(freezeCrackImage, view.getWidth(), view.getHeight(), false);
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
        engine.forceGameState(GameEngine.StateEnum.FREEZE);
        Log.d("DEV", "FULL_FROZEN");
    }

    public void checkUnfreeze(Canvas canvas) {
        Log.d("DEV", String.format("AccelVector : %f %f %f", accelVector[0], accelVector[0], accelVector[0]));
        if (frozenState != FrozenState.UNFROZEN) {
            if ((accelVector[0] > 10 && accelVector[0] < 15) ||
                    (accelVector[1] > 10 && accelVector[1] < 15) ||
                    (accelVector[2] > 10 && accelVector[2] < 15)) {
                Log.d("DEV", "MID_FROZEN");
                frozenState = FrozenState.MID_FROZEN;
            }
            // Big shake check
            if (accelVector[0] > 15 || accelVector[1] > 15 || accelVector[2] > 15) {
                Log.d("DEV", "UNFROZEN");
                frozenState = FrozenState.UNFROZEN;
                engine.nextState();
            }
            drawFreezeImage(frozenState, canvas);
        }
    }

    public void drawFreezeImage(FrozenState frozenState, Canvas canvas) {
        switch (frozenState) {
            case FULL_FROZEN:
                canvas.drawBitmap(freezeImage, 0, 0, null);
                break;
            case MID_FROZEN:
                canvas.drawBitmap(freezeCrackImage, 0, 0, null);
                break;
        }
    }
}
