package com.example.twisterfinger.activities.views.objects;

public enum Couleur {
    ROUGE_F(150, 19, 22, 0),
    ROUGE_M(191,24,29, 0),
    ROUGE_C(227, 34, 39, 0),
    VERT_F(0, 117, 0, 1),
    VERT_M(0,163,0, 1),
    VERT_C(0,255,0, 1),
    BLEU_F(0, 48, 96, 2),
    BLEU_M(14, 134, 212, 2),
    BLEU_C(104, 187, 227, 2),
    VIOLET_F(66,1,117, 3),
    VIOLET_M(117, 0, 209, 3),
    VIOLET_C(203, 138, 255, 3);

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
