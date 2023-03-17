package com.example.twisterfinger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class Wheel {
    private enum State {
        SPINNING,
        IDLE
    }

    private enum Finger {
        MAJEUR,
        INDEX,
        POUCE,
        AURICULAIRE,
        ANNULAIRE
    }

    private enum Side {
        DROIT,
        GAUCHE
    }

    public Integer finger = 0;
    public Integer side = 0;
    private Random random = new Random();
    private ArrayList<Integer> list;
    private final Integer weidth = 100;
    private State state = State.IDLE;
    private MicroHandler microHandler;
    private Context context;
    private Paint paint;
    private final Integer MAX_FRAME = 60;
    private Integer frame = 0;

    public Wheel(Context context){
        paint = new Paint();
        this.context = context;
    }

    public void draw(Canvas canvas){
        switch (state){
            case IDLE:
                paint.setColor(Color.RED);
                canvas.drawRect( (canvas.getWidth()/2)-(weidth/2), 0, canvas.getWidth()/2 + (weidth/2), weidth, paint);
                break;
            case SPINNING:

                finger = random.nextInt(5);
                side = random.nextInt(2);
                paint.setColor(Color.BLUE);
                canvas.drawRect( (canvas.getWidth()/2)-(weidth/2), 0, canvas.getWidth()/2 + (weidth/2), weidth, paint);
                frame = (frame+1)%MAX_FRAME ;
                if(frame == 0 ){
                    state = State.IDLE;
                }
                break;
        }
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawText(Finger.values()[finger]+" "+Side.values()[side], (canvas.getWidth()/2)-(weidth/2), 50, p);
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
