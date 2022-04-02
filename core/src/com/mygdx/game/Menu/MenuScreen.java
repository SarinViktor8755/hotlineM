package com.mygdx.game.Menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.ZombiKiller;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private StretchViewport viewport;
    private OrthographicCamera camera;


    ZombiKiller zombiKiller;
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

    }

    private void upDateScreen(){

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
