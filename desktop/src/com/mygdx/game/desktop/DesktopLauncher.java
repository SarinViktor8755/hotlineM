package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.ZombiKiller;
import com.mygdx.game.adMod.AdAds;

public class DesktopLauncher implements AdAds {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//        config.title = "HotLINE D";
//        config.width = ZombiKiller.WHIDE_SCREEN / 2;
//        config.height = ZombiKiller.HIDE_SCREEN / 2;
//        config.

        config.setWindowedMode(ZombiKiller.WHIDE_SCREEN / 2,ZombiKiller.HIDE_SCREEN / 2);
        new Lwjgl3Application(new ZombiKiller(3), config);

    }

    @Override
    public void show() {
        System.out.println("Pokaz !!!!!!!");

    }
}
