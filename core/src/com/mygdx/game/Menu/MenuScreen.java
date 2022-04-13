package com.mygdx.game.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    private float alphaScreen;
    ZombiKiller zombiKiller;
    private Stage stageMenu;
    TextButton textButton;
    private Texture wallpaper;
    private Texture logo;
    private float timeInScreen;
    private float timerStartGame; // переменная для анимации
    TextField textField;
    Skin skinMenu;
    Vector2 nap;

    ShaderFilm shaderFilm;
    ShaderProgram shader;

    boolean long_logo;
    AudioEngineMenumain audioEngineMenumain;

    float noise_delta = 0;

    public MenuScreen() {
    }

    public MenuScreen(ZombiKiller zombiKiller) {

        shaderFilm = new ShaderFilm();
        shaderFilm.getShader().pedantic = false;
        shader = new ShaderProgram(shaderFilm.getShader().getVertexShaderSource(), shaderFilm.getShader().getFragmentShaderSource());
        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }
        batch = new SpriteBatch();
        //batch.setShader(shader);
        shaderFilm = new ShaderFilm();


        this.zombiKiller = zombiKiller;
        timeInScreen = 0;
        timerStartGame = -1;
        long_logo = false;
        nap = new Vector2(1.5f, 0);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(ZombiKiller.WHIDE_SCREEN / 2, ZombiKiller.HIDE_SCREEN / 2, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 10, camera.viewportHeight / 10, 0);
        camera.update();
        stageMenu = new Stage(viewport);
        skinMenu = zombiKiller.assetsManagerGame.get("skin/craftacular-ui.json");
        textField = new TextField(limit, skinMenu);
        wallpaper = zombiKiller.assetsManagerGame.get("menuAsset/wallpaper.png");
        logo = zombiKiller.assetsManagerGame.get("menuAsset/logo.png", Texture.class);
///////////////////////
        textField.setMaxLength(10);
        textField.setPosition(40, 520);
        textField.setSize(260, 35);
        textField.setText(NikName.getNikName());
///////////////////////////
        textButton = new TextButton("Play Game", skinMenu);
        textButton.setPosition(40, textField.getY() -180);
        textButton.setSize(260, 80);
        textButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (textField.getText().length() < 1) return;
                NikName.setNikName(textField.getText());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (textField.getText().length() < 1) {
                    textField.setColor(Color.SALMON);
                    return false;
                }
                if (timerStartGame < 0) {
                    NikName.setNikName(textField.getText());
                    timerStartGame = 0;
                }
                return true;
            }
        });
        stageMenu = new Stage(viewport);
        stageMenu.addActor(textButton);
        stageMenu.addActor(textField);
        Gdx.input.setInputProcessor(stageMenu);
        alphaScreen = 1;
        audioEngineMenumain = new AudioEngineMenumain(this);

    }

    private void upDateScreen(float delta) {

        shaderFilm.start(delta);
        shaderFilm.setGrayScaleExtraAmount(delta);
        // System.out.println(shaderFilm.getTimer() + " !!!" );

        if (long_logo) {
            if (MathUtils.randomBoolean(.01f)) {
                long_logo = false;

            }
        } else if (MathUtils.randomBoolean(.05f)) {
            long_logo = true;

        }
        if (long_logo) {
            nap.setLength(0.5f);
            audioEngineMenumain.stopNoise();
            noise_delta = 0;
           // batch.setShader(null);
        } else {nap.setLength(6.8f);
            audioEngineMenumain.pleyNoise(Gdx.graphics.getDeltaTime());
            noise_delta = -10;
           // batch.setShader(shader);
        }


        timeInScreen += delta;
        nap.rotate(delta * 100);
        if (timerStartGame >= 0) {
            timerStartGame += delta;
            alphaScreen = 1 - (timerStartGame / 2);
            textButton.setColor(1, 1, 1, alphaScreen);
            textField.setColor(1, 1, 1, alphaScreen);
            //logo.set
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
        if(long_logo)
        batch.draw(wallpaper, viewport.getScreenX() + noise_delta,
                viewport.getScreenY() - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 100)),
                camera.viewportWidth * 1.15f, camera.viewportHeight * 1.15f); else {

            this.batch.setColor(1, 1, 1, .8f);
            batch.draw(wallpaper, viewport.getScreenX() + noise_delta,
                    viewport.getScreenY()  - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 100)),
                    camera.viewportWidth * 1.15f, camera.viewportHeight * 1.15f);
            this.batch.setColor(0, 1, 0, .3f);
            batch.draw(wallpaper, viewport.getScreenX() + MathUtils.random(-3,+10),
                    viewport.getScreenY() - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 100)),
                    camera.viewportWidth * 1.15f, camera.viewportHeight * 1.15f);

            this.batch.setColor(1, 0, 0, .3f);
            batch.draw(wallpaper, viewport.getScreenX() + MathUtils.random(-10,+5),
                    viewport.getScreenY() - ((Interpolation.bounce.apply((MathUtils.sin(timeInScreen) + 1) / 2) * 100)),
                    camera.viewportWidth * 1.15f, camera.viewportHeight * 1.15f


            );


        }

        for (int i = 8; i > 0; i--) {
            //batch.draw(logo, viewport.getScreenX() - (i * nap.x), (viewport.getScreenY() + 550 + ((MathUtils.cos(timeInScreen * 3) + 1) / 2) * 20) - (i * nap.y));
            if (MathUtils.randomBoolean(.3f)){
                batch.setColor(MathUtils.random(.4f, 1f), MathUtils.sin(i / 2f), MathUtils.sin(i / 2f), alphaScreen);


            }
            this.batch.setColor(1, 1, .4f, alphaScreen);
            if (long_logo){
                batch.draw(logo, viewport.getScreenX() - (i * nap.x + MathUtils.random(.5f)), viewport.getScreenY() + 590 - (i * nap.y) + MathUtils.random(.5f));
            }
            else
                batch.draw(logo, viewport.getScreenX() - (i * nap.x + MathUtils.random(.5f)) + MathUtils.random(-5, 5), viewport.getScreenY() + 590 - (i * nap.y) + MathUtils.random(.5f) + MathUtils.random(-5, 5));
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
