package com.mygdx.game.Menu;

import com.badlogic.gdx.audio.Sound;

public class AudioEngineMenumain {

    MenuScreen menuScreen;

    Sound noise;
    boolean noiseOn;
    long id_noise;

    float volme = 0;


    public AudioEngineMenumain(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
        noiseOn = false;
        noise = menuScreen.zombiKiller.assetsManagerGame.get("audio/tvstati.ogg", Sound.class);
        noise.setLooping(id_noise, true);
        volme = 0;
    }

    public void pleyNoise(float delta) {
        if (noiseOn) {
            volme += delta * 7;
            noise.setVolume(id_noise,volme);
            return;
        }
        volme = 0;
        noiseOn = true;
        id_noise = noise.play();
    }

    public void stopNoise() {
        noiseOn = false;
        noise.stop(id_noise);
        volme = 0;
    }


}
