package com.example.twisterfinger.engine;

import com.example.twisterfinger.activities.views.objects.Couleur;

import java.util.Random;

public class RandomFinger {
    private final Random randomGenerator;

    public RandomFinger() {
        randomGenerator = new Random();
    }

    public FingerEnum getRandomFinger() {
        return FingerEnum.values()[randomGenerator.nextInt(FingerEnum.values().length)];
    }

    public Couleur getRandomCouleur() {
        return Couleur.values()[randomGenerator.nextInt(Couleur.values().length)];
    }
}
