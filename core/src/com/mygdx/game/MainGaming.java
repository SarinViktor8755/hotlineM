package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Assets.AssetsManagerGame;
import com.mygdx.game.Characters.MainCharacter;
import com.mygdx.game.ClientNetWork.MainClient;
import com.mygdx.game.HUDAudio.AudioEngine;
import com.mygdx.game.HUDAudio.Hud;
import com.mygdx.game.HUDAudio.SoundTrack;
import com.mygdx.game.Ip.AndroidInputProcessorGamePley;

import com.mygdx.game.Ip.DesktopInputProcessorGamePley;
import com.mygdx.game.Ip.InputProc;
import com.mygdx.game.LoadingScreen.StartScreen;
import com.mygdx.game.SpaceMap.IndexMap;
import com.mygdx.game.Service.OperationVector;


public class MainGaming implements Screen {
    private MainClient mainClient;
    private ZombiKiller zk;
    private Camera camera;

    private World world;

    private SpriteBatch batch;
    private GameSpace gSpace;
    private MainCharacter hero;
    private Group gHero;

    private Hud hud;
    private IndexMap indexMap; // карта
    private AudioEngine audioEngine;
    private SoundTrack soundtrack;
    private AssetsManagerGame assetsManagerGame;
    private TextureRegion textureAim;
    private FillViewport viewport;

    private RenderStartScreen renderStartScreen;

    private float timeInGame;

    private StartScreen startScreen;

    public Hud getHud() {
        return hud;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    public MainGaming setApInput(InputProc apInput) {
        this.apInput = apInput;
        return this;
    }

    private InputProc apInput;

    public MainGaming(ZombiKiller zk) {
        this.zk = zk;
    }

    @Override
    public void show() {
        this.startScreen = new StartScreen(zk);

        this.world = new World(new Vector2(0,0),true);
                //zk.setScreen(startScreen);
                //        System.out.println("------------");
                //        System.out.println(Gdx.graphics.getWidth());
                //        System.out.println(Gdx.graphics.getHeight());
                //        System.out.println(" -- ");
        setAssetsManagerGame(AssetsManagerGame.loadAllAsset(getAssetsManagerGame()));
        this.audioEngine = new AudioEngine(this);
        this.gSpace = new GameSpace();

        mainClient = new MainClient(this);
        mainClient.coonectToServer();

        this.indexMap = new IndexMap(this);
        this.hero = new MainCharacter(this);
        this.gHero = new Group();
        gHero.addActor(hero);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(zk.WHIDE_SCREEN, zk.HIDE_SCREEN);
        viewport = new FillViewport(zk.WHIDE_SCREEN, zk.HIDE_SCREEN, camera);

        // System.out.println(zk.isAndroid() + "1111111111111111111111");
        if (zk.isAndroid()) apInput = new AndroidInputProcessorGamePley(this);
        else {
            apInput = new DesktopInputProcessorGamePley(this);
            Gdx.input.setCursorCatched(true);
        }
        Gdx.input.setInputProcessor(apInput);
        //zk.getMainGameScreen();
        hud = new Hud(this);
        soundtrack = new SoundTrack(this);
        textureAim = getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("aim");
        this.timeInGame = 0;
        renderStartScreen = new RenderStartScreen(zk,camera,viewport,getBatch());
    }

    public float getTimeInGame() {
        return timeInGame;
    }

    @Override
    public void render(float delta) {
        if(!mainClient.isConnectToServer()) {
            renderStartScreen.render(delta);
            mainClient.coonectToServer();
            return;
        }

        //   System.out.println("assets:: " + AssetsManagerGame.loadAsset(zk.assetsManagerGame));
//        System.out.println(getHero().getPosition());
//        System.out.println("camera " +getCamera().position);
//        getBatch().getTransformMatrix().scl(55);
//        getBatch().getProjectionMatrix().scl(55);
//        getBatch().getProjectionMatrix().inv();
//        viewport.
        // System.out.println(Gdx.graphics.getHeight());
        float dt = Gdx.graphics.getDeltaTime();
        apInput.act(delta);
        getMainClient().actionMainClient();
        gHero.act(dt);
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(.2f + (getHero().getGlobalAlpha() / 50), .2f - (getHero().getGlobalAlpha() / 80), .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        getBatch().setProjectionMatrix(getBatch().getProjectionMatrix().scl(.2f));
//        getBatch().setProjectionMatrix(getBatch().getProjectionMatrix().scl(.3f));
        batch.begin();
        gHero.draw(batch, 1);
        this.renderAim();
        getHero().getPoolBlood().renderAd(getBatch(), this);
        try {
            getHero().getLith().renderLights(camera); // освещение
        }catch (Exception e){}

        batch.end();
        //System.out.println(delta);
        hud.update(delta);
        cameraMove();
        soundtrack.ubdate(dt);
        hud.render(dt);
        this.timeInGame += delta;
    }


    private void renderAim() { // отрисовать прицел
        if (!getHero().isLive()) return;
        Vector2 rot = new Vector2(camera.up.x, camera.up.y);
        int l = 0;
        if (getHero().getWeapons().getWeapon() != 1) l = 1000;
        else l = 300;
        for (int i = 250; i < l; i += 200) {
            getBatch().draw(textureAim,
                    (getHero().getPosition().x + rot.x * i) - textureAim.getRegionWidth() / 2,
                    (getHero().getPosition().y + rot.y * i) - textureAim.getRegionWidth() / 2
                    , textureAim.getRegionWidth() / 2, textureAim.getRegionWidth() / 2, textureAim.getRegionWidth(), textureAim.getRegionWidth(),
                    .8f, .8f,
                    rot.angle());
        }
    }

    private void cameraMove() {
        camera.up.set(getHero().getCookAngle(), 0);
        OperationVector.setTemp_vector(getHero().getPosition().cpy().add(getHero().getCookAngle().cpy().nor().scl(420)));
        camera.position.set(OperationVector.getTemp_vector().x, OperationVector.getTemp_vector().y, 0);
        camera.update();
        // System.out.println(getHero().getOtherPlayers().getSizeGamePlayer());
    }

    public void writeDead(int id) { // записать как мертвый
        getHero().getOtherPlayers().setLiveTiId(id, false);
    }

    public void writeLiving(int id) { // записать как живой
        getHero().getOtherPlayers().setLiveTiId(id, true);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    public MainGaming setAssetsManagerGame(AssetManager assetsManagerGame) {
        this.zk.assetsManagerGame = assetsManagerGame;
        return this;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose();
        batch.dispose();
        gSpace.dispose();
    }

    public World getWorld() {
        return world;
    }

    public AudioEngine getAudioEngine() {
        return audioEngine;
    }

    public SoundTrack getSoundtrack() {
        return soundtrack;
    }

    public AssetManager getAssetsManagerGame() {
        return this.zk.assetsManagerGame;
    }

    public MainClient getMainClient() {
        return mainClient;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public Camera getCamera() {
        return camera;
    }

    public MainCharacter getHero() {
        return hero;
    }

    public ZombiKiller getZk() {
        return zk;
    }

    public InputProc getApInput() {
        return apInput;
    }

    public IndexMap getIndexMap() {
        return indexMap;
    }


}
