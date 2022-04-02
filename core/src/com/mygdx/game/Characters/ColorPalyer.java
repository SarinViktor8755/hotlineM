package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;


public class ColorPalyer {
    public static Color getColorfromId(int id) {
        float z = MathUtils.sin(id);
        z = MathUtils.random(-1, 1);

        if (z < -.5f) return Color.BROWN;
        if ((z > .5f)) return Color.GREEN;
        if (z < 0) return Color.ROYAL;
        return Color.BLUE;
    }
}
