package com.mygdx.game.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.BloodBullet.PoolBlood;
import com.mygdx.game.Characters.Animation.AnimationPers;
import com.mygdx.game.ClientNetWork.SteckApi.RequestStock;
import com.mygdx.game.HUDAudio.HelperScreen;
import com.mygdx.game.HUDAudio.MusicGame;
import com.mygdx.game.Lighting.B2lights;
import com.mygdx.game.MainGaming;
import com.mygdx.game.Service.Key_cod;
import com.mygdx.game.Service.NikName;
import com.mygdx.game.Service.OperationVector;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by 1 on 04.08.2019.
 */

public class MainCharacter extends Actor {
    MainGaming mg;
    public static final String myNikName = NikName.getNikName();

    AnimationPers animationPers;

    private Vector2 cookAngle; // навправление тела
    private Vector2 acceleration; // навправление движения
    private Vector2 position; // позиция
    private Vector2 velocity; // ускарение
    private boolean live;
    private final float max_velocity = 700;
    private float deathValleyTime = 0; // счетчик времени смерт
    private OtherPlayers otherPlayers;
    private PoolBlood poolBlood;
    public int fragWithLife;
    public int myPositionTablica;
    private Weapons weapons;
    TextureRegion tr;
    //  private Lighting lighting;
    private ArrayList<TextureRegion> maksTexture;

    private HelperScreen helperScreen;

    private BitmapFont textFont;

    public Color getMyColorGelet() {
        return myColorGelet;
    }

    public void setMyColorGelet(Color myColorGelet) {
        this.myColorGelet = myColorGelet;
    }

    private Color myColorGelet;
    private B2lights lith;

    private HashMap<String, Integer> dk;


    private float globalAlpha;


    public OtherPlayers getOtherPlayers() {
        return otherPlayers;
    }

    public void clearOtherPlayer() {
        getOtherPlayers().getPlayersList().clear();
    }


    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        if (live == false) deathValleyTime = 1.5f;
        this.live = live;
    }

    public MainCharacter(MainGaming mg) {
        this.createDk();
        this.mg = mg;
        this.position = new Vector2(0, 0);
        this.startRespawn(position);
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
        this.cookAngle = new Vector2(1, 0);
        this.animationPers = new AnimationPers(mg);
        this.otherPlayers = new OtherPlayers();
        this.poolBlood = new PoolBlood(mg);
        this.live = true;
        deathValleyTime = 0;
        //this.getOtherPlayers().cleaningSnapShots();
        globalAlpha = 1;
        this.myPositionTablica = 0; // Моя позиция в таблице
        this.weapons = new Weapons();
        //  this.lighting = new Lighting(mg);


        this.maksTexture = new ArrayList<TextureRegion>();
        for (int i = 1; i < 6; i++) {
            this.maksTexture.add(mg.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("mask" + i));
        }
        this.helperScreen = new HelperScreen(this.mg);

        lith = new B2lights(mg);

        textFont = new BitmapFont();
        textFont.setColor(Color.WHITE);
        textFont.setUseIntegerPositions(true);

    }

    private void createDk() {
        this.dk = new HashMap<String, Integer>();
        dk.put("hit1", 0);
        dk.put("hit2", 15);
        dk.put("hit3", 30);
        dk.put("hit4", 45);
    }

    public Weapons getWeapons() {
        return weapons;

    }

    public Color getMyColorAndGenerate() {
        if (this.getColor() != null)
            return this.getColor();
        int id = mg.getMainClient().getMyIdConnect();
        float r, g, b;
        r = Math.abs(MathUtils.sin(id));
        g = Math.abs(MathUtils.cos(id));
        b = Math.abs(MathUtils.cos(r - g));
        this.setColor(new Color(r, g, b, r + b + .5f));
        return this.getColor();
    }

    public float getGlobalAlpha() {
        return globalAlpha;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //System.out.println(getMyColorAndGenerate());
        //System.out.println(position.x+" :: "+ position.y);
        globalAlpha = MathUtils.sinDeg(mg.getHud().getTimer() / 20);
        //System.out.println(globalAlpha);
        Gdx.gl.glClearColor(1, 1, 1, parentAlpha);
        batch.setColor(1, 1, 1, parentAlpha);
        Gdx.gl.glClear(GL20.GL_BLEND_EQUATION);
        //System.out.println(mg.getHud().getTimer()/200000f);
//        if (mg.getHud().getTimer() > 30000) mg.getBatch().setColor(1, 1, 1, 1);
//        else mg.getBatch().setColor(1, 1, 1, mg.getHud().getTimer() / 30000f + .7f);
//        System.out.println(mg.getHud().getTimer() + 750 / 30000f);
        //batch.setColor(1, 1, 1, .75f);
//        System.out.println(weapons.getWeapon());
//        System.out.println(mg.getHero().weapons.getTemp_weapon());
//        System.out.println("__________________");
        try {
            mg.getIndexMap().renderBottomLevel();
            mg.getBatch().setColor(1, 1, 1, .6f);
            mg.getIndexMap().renderAverageLevel();
            mg.getBatch().setColor(1, 1, 1, 1);
            poolBlood.renders();
            mg.getBatch().setColor(1, 1, 1, 1);
            mg.renderAim();
            mg.getIndexMap().renderTopQualityMap();
            if (live) {
                //System.out.println("::::::::::::: "+ getCorrectionAngleBody());

                batch.draw(animationPers.getTextureLegsFromId(mg.getMainClient().getMyIdConnect()), (int) (position.x - 125), (int) (position.y - 125), 125, 125, 250, 250, 1, 1, velocity.angle());
                tr = animationPers.getTextureBodyFromId(mg.getMainClient().getMyIdConnect());
                batch.draw(tr, (int) (position.x - 125), (int) (position.y - 125), 125, 125, 250, 250, 1.375f, 1.375f, cookAngle.angle() + getCorrectionAngleBody(tr));
            }

            int key = mg.getMainClient().getMyIdConnect();
            ////////
//            mg.getBatch().setColor(getMyColorAndGenerate());

//            mg.getBatch().draw(animationPers.getTextureVestFromId(key, getWeapons().getWeapon()),
//                    (position.x - 125), (position.y - 125), 125, 125, 250, 250, 1.375f, 1.375f,
//                    mg.getHero().getCookAngle().angle());   // gelet

//            mg.getBatch().setColor(1, 1, 1, 1);
            /////////////
            // mg.getBatch().draw(animationPers.getTextureVestFromId(key, this.getWeapons().getWeapon(), (position.x - 125), (position.y - 125), 125, 125, 250, 250, 1.375f, 1.375f, this.getRotation());   // gelet


            otherPlayers.getPlayerToID(mg.getMainClient().getMyIdConnect()).updateCoordinatPleyer((int) position.x, (int) position.y, (int) cookAngle.angle());
            renderPlayers(animationPers);

            //mg.getIndexMap().renderFakePerspektiveLaier();
            // mg.getAssetsManagerGame().getProgress();
            // Gdx.app.log("Asset  ", String.valueOf(mg.getAssetsManagerGame().getProgress()));
            helperScreen.updateHelper();
        } catch (NullPointerException e) {
        }
    }

    private float getCorrectionAngleBody(TextureRegion tr) {
        try {
            float k = 0;
            k = dk.get(tr.toString());
            //System.out.println(k);
            return MathUtils.sinDeg(mg.getHero().getOtherPlayers().getTacktPlayer(mg.getMainClient().getMyIdConnect()) * 10) * 3 + k;
        } catch (NullPointerException e) {
            return 0;
        }

    }

    @Override
    public void act(float delta) {
        //System.out.println(fragWithLife);
        if (!mg.getApInput().isMove()) { // торможение
            velocity.scl(.850f);
            acceleration.scl(0);
            if (velocity.len() < .9f) { // остановка
                velocity.set(0, 0);
                velocity.setAngle(cookAngle.angle());
            }


        }
        //   lighting.updateLighting(delta);
        super.act(delta);
        movement(delta);
        mg.getHero().getOtherPlayers().upDateDeltaTimeAllPlayer(delta); // обновление времени всех сетевых пользователей для анимации
        try {
            for (int key : mg.getHero().getOtherPlayers().getPlayersList().keySet()) {  //ConcurrentModificationException
                try {
                    getOtherPlayers().getPlayerToID(key).getAnimatonBody().updateAnimation(delta);
                } catch (NullPointerException e) {
                    System.out.println("getOtherPlayers :: NPE");
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Animation failed");
        }
        mg.getAudioEngine().act(delta);
        this.deathValleyTime -= delta;
        if (this.deathValleyTime < 0) respawn();
        if (animationPers.isRedyToAttack()) { // смена оружия после анимации
            this.weapons.updateWeapon();
        }


        try {
            lith.upDateLights(this.position.x, this.position.y, this.cookAngle.angle());
        } catch (Exception e) {
        }


        if (weapons.getWeapon() == 1) lith.setLasetOn(false);
        else lith.setLasetOn(true);
        if(isLive()) lith.setLasetOn(false);
//        if (velocity.len2() > 250000)шаги
//            mg.getAudioEngine().addNewSoundStepToPleyerFromID(mg.getMainClient().getMyIdConnect());
    }

    private void movement(float delta) { // ldb;tybt

        if (!live) {
            stopSpeed();
            return;
        }
        int kefVelocety = 1;
        float x = position.x;
        float y = position.y;
        if (OperationVector.sclPrVectarov(getCookAngle(), acceleration)) kefVelocety *= 4;
        acceleration.nor();
        ///
        position.add(OperationVector.getBufferVector(velocity).scl(delta));
        if (!mg.getIndexMap().canMove((int) getPosition().x, (int) getPosition().y)) {
            position.x = x - getVelocity().nor().x * delta;
            position.y = y - getVelocity().nor().y * delta;
            int deltaCor;

            if (getVelocity().y < 0) deltaCor = (int) (420 * -delta);
            else deltaCor = (int) (420 * delta);
            if (mg.getIndexMap().canMove((int) getPosition().x, (int) getPosition().y + deltaCor)) {
                position.add(0, deltaCor);
            } else {
                if (getVelocity().x < 0) deltaCor = (int) (420 * -delta);
                else deltaCor = (int) (420 * delta);
                if (mg.getIndexMap().canMove((int) getPosition().x + deltaCor, (int) getPosition().y)) {
                    position.add(deltaCor, 0);
                }

            }
        }
//////////
        velocity.add(OperationVector.getBufferVector(acceleration).scl(delta * 2500 * kefVelocety)); // 2500 - смена скорости Глав Героя
        velocity.clamp(-1, max_velocity);
    }

    public void stopSpeed() {
        this.acceleration.setZero();
        this.velocity.setZero();
    }

    public void attackPipe(int id) {  // добавленеи анимации удара + отправка на сервер сообщение о нанесение удара атака
        getOtherPlayers().getPlayerToID(id).getAnimatonBody().addAnimationAttackPipe();// добавляем анимацию

        //System.out.println("addAnimationAttackPipe");
        int x = (int) (position.x + cookAngle.x * 80);
        int y = (int) (position.y + cookAngle.y * 80);
        try {
            //  mg.getHero().getLith().startBulletFlash(position.x + cookAngle.x * 20, position.y + cookAngle.x * 20); ///вспышка
        } catch (Exception e) {
        }

        mg.getAudioEngine().pleySoundKickStick();
        mg.getMainClient().getOutStock().addStockInQuery(new RequestStock(// отправить на сервер
                mg.getMainClient().getAndUpdateRealTime(), Key_cod.STICK_ATTACK,
                x, y,
                null, null, null, null, null, myNikName
        ));


    }

    public B2lights getLith() {
        return lith;
    }

    public void attackPistol(int id) {  // добавленеи анимации удара + отправка на сервер сообщение о нанесение удара атака
        int x = (int) (position.x + cookAngle.x * 20);  // начальное положение выстрела
        int y = (int) (position.y + cookAngle.y * 20);
        try {
            mg.getHero().getLith().startBulletFlash(position.x + cookAngle.x * 20, position.y + cookAngle.x * 20); ///вспышка
        } catch (Exception e) {
        }
        int cookAngle = (int) (getCookAngle().angle());  // направление
        mg.getMainClient().getOutStock().addStockInQuery(new RequestStock(// отправить на сервер
                mg.getMainClient().getAndUpdateRealTime(), Key_cod.GUN_SHOT,
                x, y,
                cookAngle, null, null, null, null, NikName.getNikName()
        ));
        getOtherPlayers().getPlayerToID(id).getAnimatonBody().addAnimationAttackPistols();// добавляем анимацию

        mg.getAudioEngine().pleySoundKickPistols();

        Vector2 delta = new Vector2(this.cookAngle);
        delta.rotate(40).scl(70);
        poolBlood.getBullet(this.cookAngle, (int) (position.x - delta.x), (int) (position.y - delta.y));
    }


    public void changeCheckWeapons() { // надо дописыть - проверяет фраги и если фораги достигли сменить оружие
//        if (this.fragWithLife > 2) this.weapons = 2;
//        if (this.fragWithLife > 5) this.weapons = 3;

    }

    public void attacShotgun(int id) {
        getOtherPlayers().getPlayerToID(id).getAnimatonBody().addAnimationAttackShotgun();// добавляем анимацию
        int x = (int) (position.x + cookAngle.x * 20);  // начальное положение выстрела
        int y = (int) (position.y + cookAngle.y * 20);
        try {
            mg.getHero().getLith().startBulletFlash(position.x + cookAngle.x * 20, position.y + cookAngle.x * 20); ///вспышка
        } catch (Exception e) {
        }
        int cookAngle = (int) (getCookAngle().angle());  // направление
        mg.getAudioEngine().pleySoundKickShotgun();
        mg.getMainClient().getOutStock().addStockInQuery(new RequestStock(// отправить на сервер
                mg.getMainClient().getAndUpdateRealTime(), Key_cod.SHOTGUN_SHOT,
                x, y,
                cookAngle, null, null, null, null, NikName.getNikName()
        ));

        Vector2 delta = new Vector2(this.cookAngle);
        delta.rotate(20).scl(70);
        for (int i = -10; i < 10; i++) {
            poolBlood.getBullet(this.cookAngle.cpy().rotate(i), (int) (position.x - delta.x), (int) (position.y - delta.y));
        }

    }


    public void changeYourWeapon(int idWeapon) {  // сменить оружие - сообщить на сервер и далее всем nbg ^^ 9


        mg.getMainClient().getOutStock().addStockInQuery(new RequestStock(// отправить на сервер
                mg.getMainClient().getAndUpdateRealTime(), 9,
                idWeapon, null,
                null, null, null, null, null, null
        ));
    }

    public void changeWeaponsForOlayer(int idP, int weapon) {
        try {
            if (mg.getMainClient().getMyIdConnect() != idP) {
                mg.getHero().getOtherPlayers().getPlayerToID(idP).setWeapons(weapon);
            } else {
                mg.getHero().weapons.setTemp_weapon(weapon);
            }
        } catch (NullPointerException e) {
        }
    }

    public AnimationPers getAnimationPers(int id) {
        return animationPers;
    }

    public PoolBlood getPoolBlood() {
        return poolBlood;
    }

    ////////////////
    public void goToPoint(float x, float y) { // идти в позицию
        acceleration.set(x, y);
        acceleration.angle(OperationVector.getBufferVector(x, y));
    }

    public void goToPoint(Vector2 vec) {
        goToPoint(vec.x, vec.y);
    }

    ///////////////////////
    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getCookAngle() {
        return cookAngle;
    }


    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void renderPlayers(AnimationPers animationPers) { // drugie igroki
        try {
            //mg.getBatch().setColor(1,.7f,.8f,1);
            Iterator<Integer> iter = mg.getHero().getOtherPlayers().getPlayersList().keySet().iterator();
            while (iter.hasNext()) {
                //mg.getBatch().setColor(1, 1, 1, 1);

//
//                for (String s : set) {
//                    set.add("ddd");
//                    System.out.println(s);
//                }

                //for ()
                Integer key = iter.next();
                //    System.out.println(MathUtils.sin(key));
                if (mg.getMainClient().getMyIdConnect() == key || (mg.getHero().getOtherPlayers().getXplayToId(key) == 0))
                    continue; //или это я же - иил у нас кент в загашнике на позиции - 0
                try {
                    if (!mg.getHero().getOtherPlayers().getLiveTiId(key)) continue;
                    if (mg.getHero().getOtherPlayers().isDeprecated(key)) continue;

                    int xz = mg.getHero().getOtherPlayers().getXplayToId(key);
                    int yz = mg.getHero().getOtherPlayers().getYplayToId(key);
                    if (xz == Integer.MIN_VALUE) continue;
//                    try {
                    // mg.getBatch().setBlendFunction();
                    mg.getBatch().setColor(1, 1, 1, 1);
                    mg.getBatch().draw(animationPers.getTextureLegsFromId(key), (xz - 125), (yz - 125), 125, 125, 250, 250, 1, 1, mg.getHero().getOtherPlayers().getRotationBotsToId(key)); // nogi
                    // mg.getBatch().draw(animationPers.getTextureBodyFromId(key), (xz - 125), (yz - 125), 125, 125, 250, 250, 1.375f, 1.375f, mg.getHero().getOtherPlayers().getRotationToId(key)); // telo

                    mg.getBatch().draw(animationPers.getTextureBodyFromId(key),
                            (xz - 125), (yz - 125),
                            125, 125,
                            250, 250,
                            1.375f, 1.375f,
                            mg.getHero().getOtherPlayers().getRotationToId(key)); // telo


//Color.BROWN
//Color.GREEN
//Color.ROYAL
//Color.RED
                    //if(viseble)mg.getBatch().setColor(mg.getHero().getOtherPlayers().getColorPfromId(key));
                    // setColor(getOtherPlayers().getPlayerToID(key).getColor());
                    mg.getBatch().setColor(getOtherPlayers().getPlayerToID(key).getColor());
                    mg.getBatch().draw(animationPers.getTextureVestFromId(key, getOtherPlayers().getPlayerToID(key).getWeapons()), (xz - 125), (yz - 125), 125, 125, 250, 250, 1.375f, 1.375f, mg.getHero().getOtherPlayers().getRotationToId(key));   // gelet желет
                    mg.getBatch().setColor(1, 1, 1, 1);
                    if (key < 0) // маска
                        mg.getBatch().draw(maksTexture.get(mg.getHero().getOtherPlayers().getMaskToID(key - 1)),
                                (xz - 125), (yz - 125),
                                125, 125,
                                250, 250,
                                1.375f, 1.375f, mg.getHero().getOtherPlayers().getRotationToId(key)); // mask
                } catch (NullPointerException e) {
                    //System.out.println("rener other");
                    //e.printStackTrace();
                }

            }
        } catch (ConcurrentModificationException exception) {
            // System.out.println("rener other");

            //exception.printStackTrace();
        }
        mg.getBatch().setColor(1, 1, 1, 1);
       // randerNikNameOtherPlayers(mg.getBatch());

    }

    private void randerNikNameOtherPlayers(SpriteBatch spriteBatch) {
        Iterator<Integer> iter = mg.getHero().getOtherPlayers().getPlayersList().keySet().iterator();
        while (iter.hasNext()) {
            try {
                Integer key = iter.next();
                int xz = mg.getHero().getOtherPlayers().getXplayToId(key);
                int yz = mg.getHero().getOtherPlayers().getYplayToId(key);
                textFont.draw(spriteBatch, this.getMyNikNamePlayer(key), xz, yz);
            } catch (ConcurrentModificationException e) {
            }
        }

    }

    static public String getNikNameGen(int id) {
        if (id < 0) id = id * -1;
        ArrayList<String> names = new ArrayList<String>();
        names.add("Bubba");
        names.add("Honey");
        names.add("Bo");
        names.add("Sugar");
        names.add("Doll");
        names.add("Peach");
        names.add("Snookums");
        names.add("Queen");
        names.add("Ace");
        names.add("Punk");
        names.add("Sugar");
        names.add("Gump");
        names.add("Rapunzel");
        names.add("Teeny");
        names.add("MixFix");
        names.add("BladeMight");
        names.add("Rubogen");
        names.add("Lucky");
        names.add("Tailer");
        names.add("IceOne");
        names.add("Sugar");
        names.add("Gump");
        names.add("Rapunzel");
        names.add("Teeny");
        names.add("MixFix");
        names.add("BladeMight");
        names.add("Rubogen");
        names.add("Lucky");
        names.add("Tailer");
        names.add("IceOne");
        names.add("TrubochKa");
        names.add("HihsheCKA");
        names.add("R2-D2");
        names.add("Breha Organa");
        names.add("Yoda");
        names.add("Obi-Wan Kenob");
        names.add("C-3PO");
        names.add("Darth Sidious");
        names.add("Darth Vader");
        names.add("Boba Fett");
        names.add("Sarin");
        names.add("Sasha");

        //  System.out.println(id);
        while (id >= names.size()) {
            id = id / 2;
        }
        return names.get(id) + ":Bot";
    }


    public boolean makeHit() {
        if (!isLive()) return false;
        if (!animationPers.isRedyToAttack()) return false;
        //ystem.out.println(mg.getHero().getWeapons().getWeapon() + "    ---weapons");
        if (getWeapons().getWeapon() == 1) attackPipe(mg.getMainClient().myIdConnect);
        if (getWeapons().getWeapon() == 2) attackPistol(mg.getMainClient().myIdConnect);
        if (getWeapons().getWeapon() == 3) attacShotgun(mg.getMainClient().myIdConnect);
        return true;
    }

    public void respawn() { // возраждение
        if (deathValleyTime > 0) return;
        if (this.isLive()) return;
        boolean truePosition = true;
        int x, y;
        weapons.setWeapon(1);
        weapons.setTemp_weapon(0);
        while (truePosition) {
            Vector2 temp = mg.getIndexMap().getFreeSpace();
            x = (int) temp.x;
            y = (int) temp.y;
            truePosition = false;
            for (int key : mg.getHero().getOtherPlayers().getPlayersList().keySet()) {
                float dist = OperationVector.getDistance(x, y, mg.getHero().getOtherPlayers().getPlayerToID(key).getX(), mg.getHero().getOtherPlayers().getPlayerToID(key).getY());
                if (dist < 700) {
                    truePosition = true;
                    break;
                }
            }
            getPosition().set(x, y);
        }

        this.acceleration.setZero();
        this.velocity.setZero();
        this.setLive(true);
        // this.fragWithLife = 0;
        //this.weapon.setFragWithLife(0);
        //создаем пакет и отправляем
        mg.getMainClient().getOutStock().addStockInQuery(new RequestStock(// отправить на сервер - возраждение
                mg.getMainClient().getAndUpdateRealTime(), Key_cod.RESPOWN_PLAYER,
                null, null,
                null, null, null, null, null, null
        ));
    }

    public void startRespawn(Vector2 position) {
        boolean startPos = true;
        float x = 0;
        float y = 0;
        while (startPos) {
            x = MathUtils.random(0, mg.getIndexMap().lengthMap);
            y = MathUtils.random(0, mg.getIndexMap().lengthMap);
            if (mg.getIndexMap().canMove((int) x, (int) y)) startPos = false;
        }
        position.x = x;
        position.y = y;
    }

    public boolean canIsee(int idPlayer) {
        OperationVector.setTemp_vector(getOtherPlayers().getXplayToId(idPlayer), getOtherPlayers().getYplayToId(idPlayer));
        float l = this.position.cpy().sub(OperationVector.get_Setter_Temp_vector()).len();
        if (l > 1200) return false;
        float x = this.position.cpy().sub(OperationVector.get_Setter_Temp_vector()).x;
        float y = this.position.cpy().sub(OperationVector.get_Setter_Temp_vector()).y;
        return true;
    }


    public String getMyNikNamePlayer(int id) {
        String result = "";
        if (id < 0) result = getNikNameGen(id);
        else if (id == mg.getMainClient().myIdConnect) result = MainCharacter.myNikName; else result = otherPlayers.getNikName(id);
        if (result==null) return "";

        return result;
    }


}
