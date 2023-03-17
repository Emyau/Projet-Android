package com.example.twisterfinger.engine;

import com.example.twisterfinger.activities.views.objects.Couleur;
import com.example.twisterfinger.activities.views.objects.TwisterCircle;

public class GameEngine {
    public void checkGoodCircle(TwisterCircle circleTouched) {
        if (!circleTouched.getCouleur().equals(rCouleur)) {
            this.state = StateEnum.GAME_OVER;
        } else {
            state = StateEnum.WHEEL;
        }
    }

    public enum StateEnum {
        WHEEL,
        FINGER,
        FREEZE,
        GAME_OVER
    }

    private StateEnum state;

    private Couleur rCouleur;
    private FingerEnum rfinger;


    public GameEngine() {
        state = StateEnum.WHEEL;
    }

    public void nextState() {
        switch (state) {
            case WHEEL:
                state = StateEnum.FINGER;
                break;
            case FINGER:
            case FREEZE:
            case GAME_OVER:
                state = StateEnum.WHEEL;
                break;
        }
    }

    public void freeze() {
        if (StateEnum.FINGER.equals(state)) {
            state = StateEnum.FREEZE;
        }
    }

    public void gameover() {
        state = StateEnum.GAME_OVER;
    }

    public StateEnum getState() {
        return state;
    }

    public Couleur getrCouleur() {
        return rCouleur;
    }

    public void setrCouleur(Couleur rCouleur) {
        this.rCouleur = rCouleur;
    }

    public FingerEnum getRfinger() {
        return rfinger;
    }

    public void setRfinger(FingerEnum rfinger) {
        this.rfinger = rfinger;
    }
}
