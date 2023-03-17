package com.example.twisterfinger.engine;

import java.util.Random;

public class RandomFinger {
    private final Random randomGenerator;

    public RandomFinger() {
        randomGenerator = new Random();
    }

    public FingerEnum getRandomFinger() {
        return FingerEnum.values()[randomGenerator.nextInt(FingerEnum.values().length)];
    }
}
