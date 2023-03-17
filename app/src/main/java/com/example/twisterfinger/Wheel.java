package com.example.twisterfinger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.twisterfinger.activities.views.objects.Couleur;
import com.example.twisterfinger.engine.FingerEnum;
import com.example.twisterfinger.engine.RandomGenerator;

public class Wheel {

    public enum State {
        WAIT_TO_BLOW,
        SPINNING,
        IDLE,
    }

    private State state = State.SPINNING;
    private MicroHandler microHandler;
    private final Context context;
    private final Paint paint;
    private Integer frame = 0;
    private final RandomGenerator random;
    private Couleur couleur;
    private FingerEnum finger;

    public Wheel(Context context) {
        paint = new Paint();
        this.context = context;
        random = new RandomGenerator(context);
        finger = random.getRandomFinger();
        couleur = random.getRandomCouleur();
    }

    public void draw(Canvas canvas) {
        int width = 100;
        int MAX_FRAME = 60;
        if (state == State.SPINNING) {
            finger = random.getRandomFinger();
            couleur = random.getRandomCouleur();
            frame = (frame + 1) % MAX_FRAME;
            if (frame == 0) {
                state = State.IDLE;
            }
        }
        int color = Color.rgb(couleur.getCouleurAsRed(), couleur.getCouleurAsGreen(), couleur.getCouleurAsBlue());
        paint.setColor(color);
        canvas.drawRect((canvas.getWidth() / 2) - (width / 2), 0, canvas.getWidth() / 2 + (width / 2), width, paint);

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(30);
        String text = finger.toString();
        canvas.drawText(text, (canvas.getWidth() / 2) + (width/2) + 5, 35, p);
    }

    public void trigger() {
        microHandler = new MicroHandler(context);
        Thread wheelThread = new Thread(() -> {
            microHandler.startRecording();
            while (state == State.WAIT_TO_BLOW) {
                if (microHandler.getAudioLevel() >= -10) {
                    state = State.SPINNING;
                }
            }
            microHandler.stopRecording();
            Thread.currentThread().interrupt();
        });
        wheelThread.start();
    }

    public void valueConsumed() {
        state = State.WAIT_TO_BLOW;
    }

    public State getState() {
        return state;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public FingerEnum getFinger() {
        return finger;
    }
}
