package com.example.twisterfinger.engine;

public class GameEngine {
    public enum StateEnum {
        WHEEL,
        FINGER,
        FREEZE,
        GAME_OVER
    }

    private StateEnum state;

    public GameEngine() {
        state = StateEnum.WHEEL;
    }

    private void nextState() {
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

    private void freeze() {
        if (StateEnum.FINGER.equals(state)) {
            state = StateEnum.FREEZE;
        }
    }

    public StateEnum getState() {
        return state;
    }
}
