package com.example.twisterfinger.engine;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.twisterfinger.activities.views.objects.Couleur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator {
    private final Random randomGenerator;
    List<FingerEnum> chosenFingers = new ArrayList<>();

    public RandomGenerator(Context context) {
        randomGenerator = new Random();
        SharedPreferences prefs = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        int nbFinger = prefs.getInt("nbFingers", 3);

        for (int i = 0; i < nbFinger; i++) {
            FingerEnum rfing;
            do {
                rfing = FingerEnum.values()[randomGenerator.nextInt(FingerEnum.values().length)];
            } while (chosenFingers.contains(rfing));
            chosenFingers.add(rfing);
        }
    }

    public FingerEnum getRandomFinger() {
        return chosenFingers.get(randomGenerator.nextInt(chosenFingers.size()));
    }

    public Couleur getRandomCouleur() {
        return Couleur.values()[randomGenerator.nextInt(Couleur.values().length)];
    }
}
