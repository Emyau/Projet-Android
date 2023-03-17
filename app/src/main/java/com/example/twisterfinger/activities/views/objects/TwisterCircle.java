package com.example.twisterfinger.activities.views.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TwisterCircle {

    public static final int MARGIN = 50;

    private int cx, cy;
    private final Paint paint;
    private int radius;

    private Couleur couleur;

    public TwisterCircle(Couleur couleur) {
        this.cx = 0;
        this.cy = 0;
        this.couleur = couleur;

        paint = new Paint();
        paint.setColor(Color.rgb(couleur.getCouleurAsRed(), couleur.getCouleurAsGreen(), couleur.getCouleurAsBlue()));
    }

    public void draw(Canvas canvas, float ambiantLight, float coefLumi) {

        //Couleur newCouleur = couleur;

        if (ambiantLight < coefLumi / 3) {
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

        paint.setColor(Color.rgb(couleur.getCouleurAsRed(), couleur.getCouleurAsGreen(), couleur.getCouleurAsBlue()));
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public static boolean isInsideTheCircle(TwisterCircle circle, float px, float py) {
        return Math.sqrt(Math.abs(Math.pow(px - circle.cx, 2)) + Math.abs(Math.pow(py - circle.cy, 2))) <= circle.radius;
    }

    public void setColor(int color) {
        paint.setColor(color);
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
}
