package com.mygdx.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Service.NikName;
import com.mygdx.game.ZombiKiller;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private String limit = "";
    private float alphaScreen = 1;

    ZombiKiller zombiKiller;

    private Stage stageMenu;
    TextButton textButton;

    private Texture wallpaper;
    private Texture logo;


    private float timeInScreen;
    private float timerStartGame; // переменная для анимации

    Skin skinMenu;

    Vector2 nap;


    public MenuScreen() {
    }

    public MenuScreen(ZombiKiller zombiKiller) {
        this.zombiKiller = zombiKiller;
        timeInScreen = 0;
        timerStartGame = -1;
        nap = new Vector2(3, 0);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(ZombiKiller.WHIDE_SCREEN / 2, ZombiKiller.HIDE_SCREEN / 2, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 10, camera.viewportHeight / 10, 0);
        camera.update();

        stageMenu = new Stage(viewport);
        skinMenu = zombiKiller.assetsManagerGame.get("skin/uiskin.json");
        final TextField textField = new TextField(limit, skinMenu);
        wallpaper = zombiKiller.assetsManagerGame.get("menuAsset/wallpaper.png");
        logo = zombiKiller.assetsManagerGame.get("menuAsset/logo.png", Texture.class);
///////////////////////
        textField.setMaxLength(20);
        textField.setPosition(20, 250);
        textField.setText(NikName.getNikName());
///////////////////////////
        textButton = new TextButton("Play Game", skinMenu);
        textButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                NikName.setNikName(textField.getText());

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                NikName.setNikName(textField.getText());
                timerStartGame = 0;
                return true;
            }
        });

        stageMenu = new Stage(viewport);
        stageMenu.addActor(textButton);
        stageMenu.addActor(textField);

        Gdx.input.setInputProcessor(stageMenu);


    }

    private void upDateScreen(float delta) {
        timeInScreen += delta;
        nap.rotate(delta * 100);
        if (timerStartGame >= 0) {
            timerStartGame += delta;
            alphaScreen = 1 - (timerStartGame / 2);
            if (timerStartGame > 2) startGameScreen();
        }


    }

    private void startGameScreen() {
        zombiKiller.startGamePlay();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        upDateScreen(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.camera.update();
        this.batch.setProjectionMatrix(camera.combined);
        this.batch.begin();
        this.batch.setColor(1, 1, 1, alphaScreen);
        ////////....................
        ////////....................
        ////////....................
        ////////....................
        batch.draw(wallpaper, viewport.getScreenX(),
                viewport.getScreenY() - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 100)),
                camera.viewportWidth * 1.15f, camera.viewportHeight * 1.15f);


        for (int i = 15; i > 0; i--) {
            //batch.draw(logo, viewport.getScreenX() - (i * nap.x), (viewport.getScreenY() + 550 + ((MathUtils.cos(timeInScreen * 3) + 1) / 2) * 20) - (i * nap.y));

            if (MathUtils.randomBoolean(.8f))
                batch.setColor(MathUtils.sin(i), MathUtils.sin(i / 2f), MathUtils.sin(i / 2f), alphaScreen);
            batch.draw(logo, viewport.getScreenX() - (i * nap.x), viewport.getScreenY() + 550 - (i * nap.y));
            batch.setColor(1, 1, 1, 1);
        }


        //  batch.draw(wallpaper,0,0,1500,1500);
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

    }
}
