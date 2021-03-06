package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.ZombiKiller;
import com.mygdx.game.adMod.AdAds;

public class DesktopLauncher implements AdAds {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "HotLINE D";
        config.width = ZombiKiller.WHIDE_SCREEN;
        config.height = ZombiKiller.HIDE_SCREEN;

        new LwjglApplication(new ZombiKiller(3), config);

    }

    @Override
    public void show() {
        System.out.println("Pokaz !!!!!!!");

    }
}
