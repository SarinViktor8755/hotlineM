package com.mygdx.game.HUDAudio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MainGaming;
import com.mygdx.game.ZombiKiller;

/**
 * Created by Виктор on 19.01.2021.
 */

public class HelperScreen {
    TextureRegion helpList = new TextureRegion();
    MainGaming mainGaming;
    private float timer = 0;
    private boolean tuach = false;

    public HelperScreen(MainGaming mg) {
        this.mainGaming = mg;
        helpList = mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("helper");
        this.tuach = false;
        this.timer = 0;
    }

    public void updateHelper() {
        //this.mainGaming.getApInput();
        this.timer = this.timer + Gdx.graphics.getDeltaTime();
    }


}
