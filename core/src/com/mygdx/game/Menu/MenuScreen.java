package com.mygdx.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.ZombiKiller;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private StretchViewport viewport;
    private OrthographicCamera camera;


    ZombiKiller zombiKiller;

    private Stage stageMenu;

    private Texture wallpaper;
    private Texture wallpaper1;
    private Texture logo;
    private Texture disconnect;

    private float timeInScreen;
    private float timerStartGame; // переменная для анимации



    public MenuScreen() {
    }

    public MenuScreen(ZombiKiller zombiKiller) {
        this.zombiKiller = zombiKiller;
        timeInScreen = 0;
        timerStartGame = 0;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(ZombiKiller.WHIDE_SCREEN, ZombiKiller.HIDE_SCREEN, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stageMenu = new Stage(viewport);
        ///...................
//        textureRegions.put(1, z.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr1"));
//        wallpaper = zombiKiller.("menuAsset/wallpaper.png", Texture.class);
        //...............
//        wallpaper = mainGame.assetManager.get("menuAsset/wallpaper.png", Texture.class);
//        wallpaper1 = mainGame.assetManager.get("menuAsset/wallpaper1.png", Texture.class);
//        logo = mainGame.assetManager.get("menuAsset/logo.png", Texture.class);
//        disconnect = mainGame.assetManager.get("menuAsset/disconct.png", Texture.class);
        stageMenu = new Stage(viewport);
       // skinMenu = mainGame.assetManager.get("skin/uiskin.json")
        //final TextField textField = new TextField(limit, skinMenu);
        Gdx.input.setInputProcessor(stageMenu);


    }

    private void upDateScreen(){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        upDateScreen();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.camera.update();
        this.batch.setProjectionMatrix(camera.combined);
        this.batch.begin();
        ////////....................
        ////////....................
        ////////....................
        ////////....................

        this.batch.end();
        stageMenu.draw();

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        wallpaper.dispose();
        logo.dispose();
    }
}
