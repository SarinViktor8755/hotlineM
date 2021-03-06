package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.LoadingScreen.LoadingScreen;
import com.mygdx.game.LoadingScreen.StartScreen;
import com.mygdx.game.Pause.PauseScreen;
import com.mygdx.game.adMod.AdAds;


public class ZombiKiller extends Game {
    public AssetManager assetsManagerGame;

    static public final int WHIDE_SCREEN = 720;
    static public final int HIDE_SCREEN = 1280;

//    static public final int HIDE_SCREEN = 2340;
//    static public final int WHIDE_SCREEN = 1080;
    public byte tip = 0;
    private AdAds adMod;

    private MainGaming mGaming;
    private PauseScreen pauseScreen;
    private LoadingScreen loadingScreen;
    private StartScreen startScreen;

    public ZombiKiller(int tip,AdAds adMod) {

        this.adMod = adMod;
        this.tip = (byte) tip;

    }

    public ZombiKiller(int tip) {
        this.tip = (byte) tip;
        adMod = new AdAds() {
            @Override
            public void show() {

            }
        };
    }

    public boolean isAndroid(){
        if(tip == 1) return true; return false;
    }

    public void watchAds(){
        this.adMod.show();
    }

    @Override
    public void create() {
        this.startScreen = new StartScreen(this);
        this.assetsManagerGame = new AssetManager();
        getMainGaming();


    }

    public void getPauseScreen(){
        this.pauseScreen = new com.mygdx.game.Pause.PauseScreen(this,true);
        this.setScreen(this.pauseScreen);
    }

    public void getPauseScreen(int PauseTime){
        //System.out.println("Pause Screen");
        mGaming.getMainClient().client.stop();
        boolean ad = true;
        if(mGaming.getTimeInGame() < 40) ad = false;
        this.pauseScreen = new PauseScreen(this, PauseTime,ad);
        this.setScreen(this.pauseScreen);
    }

    public void getLoadingScreen(){
        loadingScreen = new LoadingScreen(this);
        this.setScreen(loadingScreen);
    }

    public void loarToGame(){
        this.loadingScreen.dispose();
        this.mGaming = new MainGaming(this);

       // this.setScreen(this.mGaming);
    }

    public void getMainGaming(){
        //if(this.loadingScreen != null) this.loadingScreen.dispose();
        if(this.pauseScreen != null) this.pauseScreen.dispose();
        this.mGaming = new MainGaming(this);
        this.setScreen(this.mGaming);
        //this.setScreen(this.startScreen);
    }

    public void getMainGameScreen(){
        this.setScreen(this.mGaming);
    }

    public Screen getMainGameScreen(boolean flag){
        return  this.mGaming;
    }

    public void render() {
        super.render(); // ??????????!
    }



}
