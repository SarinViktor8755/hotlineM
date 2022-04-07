package com.mygdx.game.Assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class AssetsManagerGame {

    static public final AssetManager loadAllAsset(AssetManager assetManager) {
        GdxNativesLoader.load();
        assetManager.load("pauseAsset/pause", TextureAtlas.class);
        assetManager.finishLoading();
        assetManager.load("character/character", TextureAtlas.class);
        assetManager.load("map/obstacles", TextureAtlas.class);
        assetManager.load("map/groiund", TextureAtlas.class);
        assetManager.load("map/ground", TextureAtlas.class);
        assetManager.load("pauseAsset/pause", TextureAtlas.class);
        assetManager.load("audio/best.ogg", Sound.class);
        assetManager.load("audio/rockroll.ogg", Sound.class);
        assetManager.load("audio/best.ogg", Sound.class);
        assetManager.load("audio/hit1.ogg", Sound.class);
        assetManager.load("audio/hit2.ogg", Sound.class);
        assetManager.load("audio/death1.ogg", Sound.class);
        assetManager.load("audio/death2.ogg", Sound.class);
        assetManager.load("audio/top1.ogg", Sound.class);
        assetManager.load("audio/top2.ogg", Sound.class);
        assetManager.load("audio/top3.ogg", Sound.class);
        assetManager.load("audio/f.ogg", Sound.class);
        assetManager.load("audio/shotgun.ogg", Sound.class);

        assetManager.load("audio/loose.ogg", Sound.class);
        assetManager.load("audio/win.ogg", Sound.class);
        assetManager.load("audio/pistolShooting1.ogg", Sound.class);
        assetManager.load("audio/pistolShooting2.ogg", Sound.class);

        assetManager.load("voice/voice1.ogg", Sound.class);
        assetManager.load("voice/voice2.ogg", Sound.class);
        assetManager.load("voice/voice3.ogg", Sound.class);
        assetManager.load("voice/voice4.ogg", Sound.class);
        assetManager.load("voice/voice5.ogg", Sound.class);
        assetManager.load("audio/lostPrimuschestvo.ogg", Sound.class);
        assetManager.load("fonts/font.fnt", BitmapFont.class);
        assetManager.load("audio/music.ogg", Sound.class);
        assetManager.finishLoading();
        return assetManager;
    }

    static public final AssetManager loadLoadingScreen(AssetManager assetManager) {
        assetManager.load("pauseAsset/pause", TextureAtlas.class);
        assetManager.finishLoading();
        return assetManager;
    }

    static public final AssetManager loadAssetForMenu(AssetManager assetManager) {
        assetManager.clear();
        GdxNativesLoader.load();

        assetManager.load("menuAsset/logo.png", Texture.class);
        assetManager.load("menuAsset/wallpaper.png", Texture.class);
        assetManager.load("skin/craftacular-ui.json", Skin.class);


        assetManager.finishLoading();
        return assetManager;
    }

    static public final AssetManager unloadAllAsset(AssetManager assetManager) {
        assetManager.dispose();
        return assetManager;
    }

    static public float getProgress(AssetManager assetManager){
        return assetManager.getProgress();

    }

    static public boolean loadAsset(AssetManager assetManager){
        if(!assetManager.isLoaded("pauseAsset/pause")) return false;
        if(!assetManager.isLoaded("map/obstacles")) return false;
        if(!assetManager.isLoaded("map/ground")) return false;
        if(!assetManager.isLoaded("character/character")) return false;

        if(!assetManager.isLoaded("audio/top3.ogg")) return false;
        if(!assetManager.isLoaded("voice/voice3.ogg")) return false;
        if(!assetManager.isLoaded("voice/voice3.ogg")) return false;
        if(!assetManager.isLoaded("voice/voice4.ogg")) return false;
        if(!assetManager.isLoaded("fonts/font.fnt")) return false;
        return true;
    }


}
