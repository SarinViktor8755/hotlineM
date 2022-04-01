package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.M_Util;


public class ColorPalyer {
    public static Color getColorfromId(int id) {
        float z = MathUtils.sin(id);
        if (M_Util.isEqual(-1, -.5f)) return Color.BLUE;
        else if (M_Util.isEqual(-.5f, 0)) return Color.RED;
        else if (M_Util.isEqual(0, .5f)) return Color.GREEN;
        else if (M_Util.isEqual(.5f, 1)) return Color.GOLD;
        return Color.BLUE;
    }
}
