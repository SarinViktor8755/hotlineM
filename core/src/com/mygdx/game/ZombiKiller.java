package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Assets.AssetsManagerGame;
import com.mygdx.game.ClientNetWork.MainClient;
import com.mygdx.game.LoadingScreen.LoadingScreen;
import com.mygdx.game.LoadingScreen.StartScreen;
import com.mygdx.game.Menu.MenuScreen;
import com.mygdx.game.Pause.PauseScreen;
import com.mygdx.game.Service.NikName;
import com.mygdx.game.adMod.AdAds;


public class ZombiKiller extends Game {
    public AssetManager assetsManagerGame;

    public static String myNikName;

    static public final int WHIDE_SCREEN = 720;
    static public final int HIDE_SCREEN = 1520;
//    static public final int WHIDE_SCREEN = (int)(720 * .8);
//    static public final int HIDE_SCREEN = (int)(1520 * .8);

    public byte tip = 0;
    private AdAds adMod;


    private MainClient mainClient;
    private MainGaming mGaming;
    private PauseScreen pauseScreen;
    private LoadingScreen loadingScreen;
    private StartScreen startScreen;
    private MenuScreen menuScreen;

    public ZombiKiller(int tip, AdAds adMod) {

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

    public boolean isAndroid() {
        if (tip == 1) return true;
        return false;
    }

    public void watchAds() {
        this.adMod.show();
    }

    @Override
    public void create() {
        this.startScreen = new StartScreen(this);
        this.assetsManagerGame = new AssetManager();
        AssetsManagerGame.loadAssetForMenu(assetsManagerGame);
        getMenu();
        //getMainGaming();

    }

    public void createGame(){
        this.mGaming = new MainGaming(this);
    }

    public void startGamePlay(){

        this.setScreen(this.mGaming);
        this.myNikName = NikName.getNikName();
    }


    public MainGaming getmGaming() {
        return mGaming;
    }



    public void getPauseScreen() {
        this.pauseScreen = new com.mygdx.game.Pause.PauseScreen(this, true);
        this.setScreen(this.pauseScreen);
    }

    public void getPauseScreen(int PauseTime) {
        mGaming.getMainClient().client.stop();
        boolean ad = true;
        if (mGaming.getTimeInGame() < 40) ad = false;
        this.pauseScreen = new PauseScreen(this, PauseTime, ad);
        this.setScreen(this.pauseScreen);
    }

    public void getLoadingScreen() {
        loadingScreen = new LoadingScreen(this);
        this.setScreen(loadingScreen);
    }

    public void loarToGame() {
        this.loadingScreen.dispose();
        this.mGaming = new MainGaming(this);

        // this.setScreen(this.mGaming);
    }

    public void getMenu(){


        this.menuScreen = new MenuScreen(this);
        this.setScreen(this.menuScreen);
    }

    public void getMainGaming() {
        if (this.pauseScreen != null) this.pauseScreen.dispose();
        this.mGaming = new MainGaming(this);
        this.setScreen(this.mGaming);
    }

    public static String getMyNikName() {
        return myNikName;
    }

    public void getMainGameScreen() {
        this.setScreen(this.mGaming);
    }

    public Screen getMainGameScreen(boolean flag) {
        return this.mGaming;
    }

    public void render() {
        super.render(); // важно!
    }


}
