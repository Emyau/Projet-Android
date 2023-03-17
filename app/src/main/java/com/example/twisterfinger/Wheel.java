package com.example.twisterfinger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.twisterfinger.activities.views.objects.Couleur;
import com.example.twisterfinger.engine.FingerEnum;
import com.example.twisterfinger.engine.RandomFinger;

import java.util.ArrayList;
import java.util.Random;

public class Wheel {
    private enum State {
        SPINNING,
        IDLE
    }

    public FingerEnum finger;
    private final Integer weidth = 100;
    private State state = State.IDLE;
    private MicroHandler microHandler;
    private Context context;
    private Paint paint;
    private final Integer MAX_FRAME = 60;
    private Integer frame = 0;
    private RandomFinger random;
    private Couleur couleur;

    public Wheel(Context context){
        paint = new Paint();
        this.context = context;
        random = new RandomFinger();
        finger = random.getRandomFinger();
        couleur = random.getRandomCouleur();
    }

    public void draw(Canvas canvas){
        int color;
        switch (state){
            case IDLE:
                color = Color.rgb(couleur.getCouleurAsRed(),couleur.getCouleurAsGreen(),couleur.getCouleurAsBlue());
                paint.setColor(color);
                canvas.drawRect( (canvas.getWidth()/2)-(weidth/2), 0, canvas.getWidth()/2 + (weidth/2), weidth, paint);
                break;
            case SPINNING:
                finger = random.getRandomFinger();
                couleur = random.getRandomCouleur();
                color = Color.rgb(couleur.getCouleurAsRed(),couleur.getCouleurAsGreen(),couleur.getCouleurAsBlue());
                paint.setColor(color);
                canvas.drawRect( (canvas.getWidth()/2)-(weidth/2), 0, canvas.getWidth()/2 + (weidth/2), weidth, paint);
                frame = (frame+1)%MAX_FRAME ;
                if(frame == 0 ){
                    state = State.IDLE;
                }
                break;
        }
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        String text = finger.toString();
        canvas.drawText(text, (canvas.getWidth()/2)-text.length(), 50, p);
    }

    public void trigger(){
        microHandler = new MicroHandler(context);
        Thread wheelThread = new Thread(() ->{
            microHandler.startRecording();
            while( state == State.IDLE ) {
                if( microHandler.getAudioLevel() >= -10 ){
                    state = State.SPINNING;
                }
            }
            microHandler.stopRecording();
            Thread.currentThread().interrupt();
        });
        wheelThread.start();
    }


}
