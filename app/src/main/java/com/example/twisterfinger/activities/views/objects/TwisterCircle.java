package com.example.twisterfinger.activities.views.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.twisterfinger.engine.FingerEnum;

public class TwisterCircle {

    private int cx, cy;
    private final Paint paint;
    private int radius;

    private FingerEnum fingerEnum;

    private Couleur couleur;

    public TwisterCircle(Couleur couleur) {
        this.cx = 0;
        this.cy = 0;
        this.couleur = couleur;

        paint = new Paint();
        paint.setColor(Color.rgb(couleur.getCouleurAsRed(), couleur.getCouleurAsGreen(), couleur.getCouleurAsBlue()));
    }

    public void draw(Canvas canvas, float ambiantLight, float coefLumi) {

        int couleurExte;

        if (ambiantLight < coefLumi / 3) {
            couleurExte = Color.WHITE;
            switch (couleur.getCouleurAsHue()) {
                case (0):
                    couleur = Couleur.ROUGE_C;
                    break;
                case (1):
                    couleur = Couleur.VERT_C;
                    break;
                case (2):
                    couleur = Couleur.BLEU_C;
                    break;
                case (3):
                    couleur = Couleur.VIOLET_C;
                    break;
            }
        }

        else if (ambiantLight < (coefLumi/3)*2) {
            couleurExte = Color.LTGRAY;
            switch (couleur.getCouleurAsHue()) {
                case (0):
                    couleur = Couleur.ROUGE_M;
                    break;
                case (1):
                    couleur = Couleur.VERT_M;
                    break;
                case (2):
                    couleur = Couleur.BLEU_M;
                    break;
                case (3):
                    couleur = Couleur.VIOLET_M;
                    break;
            }
        }

        else {
            couleurExte = Color.GRAY;
            switch (couleur.getCouleurAsHue()) {
                case (0):
                    couleur = Couleur.ROUGE_F;
                    break;
                case (1):
                    couleur = Couleur.VERT_F;
                    break;
                case (2):
                    couleur = Couleur.BLEU_F;
                    break;
                case (3):
                    couleur = Couleur.VIOLET_F;
                    break;
            }
        }

        Paint paintExte = new Paint();
        paintExte.setColor(couleurExte);
        paint.setColor(Color.rgb(couleur.getCouleurAsRed(), couleur.getCouleurAsGreen(), couleur.getCouleurAsBlue()));
        canvas.drawCircle(cx, cy, radius+5, paintExte);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public static boolean isInsideTheCircle(TwisterCircle circle, float px, float py) {
        return Math.sqrt(Math.abs(Math.pow(px - circle.cx, 2)) + Math.abs(Math.pow(py - circle.cy, 2))) <= circle.radius;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public FingerEnum getFingerEnum() {
        return fingerEnum;
    }

    public void setFingerEnum(FingerEnum fingerEnum) {
        this.fingerEnum = fingerEnum;
    }
}
