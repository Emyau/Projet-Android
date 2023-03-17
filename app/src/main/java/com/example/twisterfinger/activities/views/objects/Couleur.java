package com.example.twisterfinger.activities.views.objects;

public enum Couleur {
    ROUGE_F(255, 0, 0, 0),
    ROUGE_M(215,153,2, 0),
    ROUGE_C(254, 254, 51, 0),
    VERT_F(0, 117, 0, 1),
    VERT_M(0,163,0, 1),
    VERT_C(0,255,0, 1),
    BLEU_F(0, 48, 96, 2),
    BLEU_M(14, 134, 212, 2),
    BLEU_C(104, 187, 227, 2),
    VIOLET_F(92,1,163, 3),
    VIOLET_M(117, 0, 209, 3),
    VIOLET_C(194, 20, 96, 3);

    private final int r;
    private final int g;
    private final int b;
    private final int hue;

    Couleur(int r, int g, int b, int hue) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.hue = hue;
    }

    public int getCouleurAsRed() {
        return this.r;
    }

    public int getCouleurAsGreen() {
        return this.g;
    }

    public int getCouleurAsBlue() {
        return this.b;
    }

    public int getCouleurAsHue() {
        return this.hue;
    }
}
