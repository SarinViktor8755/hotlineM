package com.mygdx.game.BloodBullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MainGaming;
import com.mygdx.game.Service.OperationVector;
import com.mygdx.game.Service.StaticService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class PoolBlood {
    private ArrayDeque<SlidingAd> slidingAdDeque;
    private ArrayDeque<Blood> myPriorityQueue; // кровь маски  и все такое
    private ArrayDeque<Bullet> myBulletQueue;
    private MainGaming mainGaming;
    private String blood, blood1;
    private int SIZE_BULLET_QUEUE = 3500;
    private int SIZE_BLOOD_QUEUE = 750;
    private ArrayDeque<Flash> fleshs;
    private float reboundTimer = 0;
    Color color;

    private HashMap<Integer, TextureRegion> textureRegions; // тело1, тело 2 , тело 3, капля, лужа

    private ArrayList<Integer> bodyBlood; // набор из мертвых тел просто ссылки

    public MainGaming getMainGaming() {
        return mainGaming;
    }

    public PoolBlood(MainGaming mg) {
        color = new Color();
        this.mainGaming = mg;
        reboundTimer = 0;
        this.myPriorityQueue = new ArrayDeque<Blood>(SIZE_BLOOD_QUEUE);
        this.myBulletQueue = new ArrayDeque<Bullet>(SIZE_BULLET_QUEUE); // потроны
        this.slidingAdDeque = new ArrayDeque<SlidingAd>(3);
        this.fleshs = new ArrayDeque<Flash>();

        for (int i = 0; i < SIZE_BLOOD_QUEUE; i++) {
            myPriorityQueue.addFirst(new Blood());
        }

        for (int i = 0; i < SIZE_BULLET_QUEUE; i++) {
            myBulletQueue.addFirst(new Bullet());
        }

        for (int i = 0; i < 2; i++) {
            SlidingAd slidingAd = new SlidingAd();
            slidingAdDeque.add(slidingAd);
        }

        for (int i = 0; i < 10; i++) {
            this.fleshs.add(new Flash(Integer.MIN_VALUE, Integer.MIN_VALUE));
        }

        blood = "blood";
        blood1 = "blood1";

        textureRegions = new HashMap<Integer, TextureRegion>();


        textureRegions.put(1, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr1"));
        textureRegions.put(2, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr2"));
        textureRegions.put(3, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr3"));
        textureRegions.put(4, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr9"));
        textureRegions.put(5, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr4"));

        textureRegions.put(7, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr5"));// без гооловы
        textureRegions.put(8, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr6"));// без руки

        textureRegions.put(9, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr7")); // голова
        textureRegions.put(10, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr8")); // рука

        ///цвета желетов тел
        textureRegions.put(31, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr9g"));
        textureRegions.put(32, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr2g"));
        textureRegions.put(33, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr3g"));
        textureRegions.put(34, textureRegions.get(31));
        textureRegions.put(35, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr4g"));

        textureRegions.put(37, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr5g"));// без гооловы
        textureRegions.put(38, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("tr6g"));// без руки
        ///////////blood
        textureRegions.put(60, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion(blood1));
        textureRegions.put(61, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion(blood));
        textureRegions.put(62, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("bullet"));//пуля
        //////
        textureRegions.put(20, mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("plus")); // +1
        textureRegions.put(21, mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("lost")); // +lose
        textureRegions.put(22, mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("victory")); // +winer
        textureRegions.put(23, mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("fight")); // +Fight
        textureRegions.put(24, mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("first")); // +first
        textureRegions.put(25, mainGaming.getAssetsManagerGame().get("pauseAsset/pause", TextureAtlas.class).findRegion("lostlead")); // +loast lead
        //////////////////

        textureRegions.put(100, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("mask1"));
        textureRegions.put(101, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("mask2"));
        textureRegions.put(102, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("mask3"));
        textureRegions.put(103, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("mask4"));
        textureRegions.put(104, mainGaming.getAssetsManagerGame().get("character/character", TextureAtlas.class).findRegion("mask5"));


    }

    public ArrayDeque<Bullet> getMyBulletQueue() {
        return myBulletQueue;
    }


    private Blood getElementDequare() {
//        Blood b = myPriorityQueue.pollLast();
//        myPriorityQueue.addFirst(b);
        Blood b = myPriorityQueue.pollFirst();
        myPriorityQueue.addLast(b);
        return b;
    }

    private void getBoload(int x, int y, TextureRegion textureRegion, float actiontimer, float score, int xr, int yr) {
        Blood te = getElementDequare();
        te.color = null;
        te.timer = 0;
        te.actiontimer = actiontimer;
        te.setX(x);
        te.setY(y);
        te.flip = 0;
        te.angle.set(xr, yr);
        te.texture = textureRegion;
        te.score = score;
        te.transparent = false;
    }

    private Blood getBoload(int x, int y, TextureRegion textureRegion, float actiontimer, float score, int xr, int yr, boolean transparent) {
        Blood te = getElementDequare();
        te.color = null;
        te.timer = 0;
        te.actiontimer = actiontimer;
        te.setX(x);
        te.setY(y);
        te.flip = 0;
        te.angle.set(xr, yr);
        te.texture = textureRegion;
        te.score = score;
        te.transparent = transparent;
        return te;
    }

    private void getBoload(int x, int y, TextureRegion textureRegion, Vector2 angel, float actiontimer, float score) {
        Blood te = getElementDequare();
        te.color = null;

        te.timer = 0;
        te.actiontimer = actiontimer;
        te.setX(x);
        te.setY(y);
        te.flip = 0;
        te.angle = angel;
        te.texture = textureRegion;
        te.score = score;
        te.transparent = false;

    }

    ///////////////////
    private void getBoload(int x, int y, TextureRegion textureRegion, Vector2 angel, float actiontimer, float score, Color color) {
        //System.out.println("!!! "+ color);
        Blood te = getElementDequare();
        te.timer = 0;
        te.actiontimer = actiontimer;
        te.setX(x);
        te.setY(y);
        te.flip = 0;
        te.angle = angel;
        te.texture = textureRegion;
        te.score = score;
        te.transparent = false;
        te.color = color;
    }


    public void getDistroyAnimation(int q, int x, int y, int player) { // простая анимация
        ejectionBlood(q, x, y); // // капли
        getPoolBlood(x, y, textureRegions.get(60), 1, 1); // luga
        int nomer_texture = MathUtils.random(1, 4);
        int nomer_texture_j = nomer_texture + 30;


        getCorpse(x, y, textureRegions.get(nomer_texture), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(nomer_texture_j), player); // telo

    }

    public void getDistroyHeadAnimation(int q, int x, int y, int player) { // простая анимация  - отрыв головы
        ejectionBlood(q, x, y); // // капли
        getPoolBlood(x, y, textureRegions.get(60), 1, 1); // luga
        int nomer_texture = MathUtils.random(1, 4);
        getCorpse(x, y, textureRegions.get(nomer_texture), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(nomer_texture + 30), player); // telo

    }


    public void getDistroyAnimation(int q, int x, int y, int player, int weapon, int angel) { // создание остатков тела
        // System.out.println(angel);
        // System.out.println(weapon);


        if (weapon == 2) {
            //System.out.println("ubit pistols");
            ejectionBlood(MathUtils.random(3, 12), x, y, angel); // направление
            getPoolBlood(x, y, textureRegions.get(60), 1, 1); // luga
            int nomer_texture = MathUtils.random(1, 4);
            getCorpse(x, y, textureRegions.get(nomer_texture), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(nomer_texture + 30), player); // telo
            if (player < 0) getPoolMask(x, y, player);

            return;
        }
        if (weapon == 3) {
            // System.out.println("ubit shootgun");
            if (StaticService.selectWithProbability(20)) {
                ejectionBlood(MathUtils.random(7, 14), x, y, angel); // направление

                int nomer_texture = MathUtils.random(1, 4);
                getCorpse(x, y, textureRegions.get(nomer_texture), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(nomer_texture + 30), player); // telo
                //желет
                if (player < 0) getPoolMask(x, y, player);
                getPoolBlood(x, y, textureRegions.get(60), 1, 1); // luga
                return;
            }

            if (StaticService.selectWithProbability(20)) {
                ejectionBlood(MathUtils.random(7, 14), x, y, angel); // направление
                getPoolBlood(x, y, textureRegions.get(60), 1, 1); // luga
                getCorpse(x, y, textureRegions.get(8), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(8 + 30), player); // telo
                //желет
                //
                if (player < 0) getPoolMask(x, y, player);
                return;
            }

            if (StaticService.selectWithProbability(20)) {
                //    System.out.println("palka ubil 1 ");
                ejectionBlood(MathUtils.random(7, 14), x, y, angel); // направление

                getCorpse(x, y, textureRegions.get(7), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(37), player); // telo
                if (player < 0) getPoolMask(x, y, player);
                getPoolBlood(x, y, textureRegions.get(61), 1, 1); // luga
                //желет

                int xp = 15;
                int yp = 15;
                if (MathUtils.randomBoolean()) xp *= -1;
                if (MathUtils.randomBoolean()) yp *= -1;
                getBoload(x, y, textureRegions.get(9), MathUtils.random(.05f, .15f), 1f, xp, yp);
                if (player < 0) getPoolMask(x, y, player);
                return;
            } else {// убийство палкой )))
                ejectionBlood(MathUtils.random(7, 14), x, y, angel); // направление

                getCorpse(x, y, textureRegions.get(8), MathUtils.random(0, 350), MathUtils.random(0, 350), textureRegions.get(38), player); // telo
                //желет

                int xp = 15;
                int yp = 15;
                if (MathUtils.randomBoolean()) xp *= -1;
                if (MathUtils.randomBoolean()) yp *= -1;
                getBoload(x, y, textureRegions.get(10), MathUtils.random(.05f, .15f), 1f, xp, yp);
                if (player < 0) getPoolMask(x, y, player);
                getPoolBlood(x, y, textureRegions.get(61), 1, 1); // luga
                return;
            }

        }
    }

    private void deathAnimation_3() {

    }

    private void flipTextReg(TextureRegion in, boolean logik) {
        if (logik == in.isFlipY()) return;
        if (logik != in.isFlipY()) {
            in.flip(false, true);
        }
    }

    private void ejectionBlood(int quantity, int x, int y) { /// добавить капля кровь
        for (int i = 0; i < quantity; i++) {
            getBoload(x, y, textureRegions.get(61), MathUtils.random(.09f, .3f), MathUtils.random(.05f, .01f), MathUtils.random(-10, 10) * 2, MathUtils.random(-10, 10) * 2, true);
        }
    }

    private void ejectionHead(int x, int y) { /// оторванная голова
        int xp = 15;
        int yp = 15;
        if (MathUtils.randomBoolean()) xp *= -1;
        if (MathUtils.randomBoolean()) yp *= -1;
        getBoload(x, y, textureRegions.get(10), MathUtils.random(.05f, .15f), 1f, xp, yp);
    }

    private void ejectionArm(int x, int y) { /// оторванная рука
        getBoload(x, y, textureRegions.get(1), 1f, 1f, 4, 4);
    }


    public void ejectionBlood(int quantity, int x, int y, int angle) { /// добавить капля кровь - с направлением
        OperationVector.setTemp_vector(1, 1);
        OperationVector.get_Setter_Temp_vector().setAngle(angle);
        for (int i = 0; i < quantity; i++) {
            Blood a = getBoload(x, y, textureRegions.get(61), MathUtils.random(.09f, .3f), MathUtils.random(.1f, .11f), MathUtils.random(-10, 10) * 2, MathUtils.random(-10, 10) * 2, true);
            a.getAngle().setAngle(OperationVector.get_Setter_Temp_vector().angle() + MathUtils.random(-24, +24));
        }
    }

    private void getPoolMask(int x, int y, int player) { /// Maska крови
        int nomMask = mainGaming.getHero().getOtherPlayers().getMaskToID(player) + 101;
        getBoload(x + MathUtils.random(50, 100), y + MathUtils.random(50, 100), textureRegions.get(nomMask), MathUtils.random(.09f, .2f), 1, MathUtils.random(-10, 10) * 2, MathUtils.random(-10, 10) * 2, false);
    }

    private void getCorpse(int x, int y, TextureRegion textureRegion, int xr, int yr, TextureRegion jellyTexture, int nom_player) { /// добавить труп

        Vector2 directionBody = new Vector2(MathUtils.random(-1, 1), MathUtils.random(-1, 1));
        float actiontimer = MathUtils.random(.1f, .25f);


        getBoload(x, y, textureRegion, directionBody, actiontimer, 1); // тело
        if (nom_player != mainGaming.getMainClient().getMyIdConnect())
            getBoload(x, y, jellyTexture, directionBody, actiontimer, 1, mainGaming.getHero().getOtherPlayers().getColorPfromId(nom_player));     /// Желет цветой


////////////////////////////////////////////////////////////////////////////////////////////////////////

        // for (int i = 0; i < 20; i++) {
        //     getPoolVest(x, y, textureRegion, 3, 3, new Color(1, 1, 1, 1));
        //      System.out.println(textureRegion);

        // }
    }

    private void takeColoredVest(int x, int y, TextureRegion textureRegion, float v, float angle, float p4, Color color) {/// Желет цветой - добавление
        //System.out.println("takeColoredVest");
        //   getBoload(x, y, textureRegion, 0.2f, MathUtils.random(.2f, 1.7f), xr, yr, true);
        // getBoload(x, y, textureRegion, 90, actiontimer, 1); // тело

    }

    private void getPoolBlood(int x, int y, TextureRegion textureRegion, int xr, int yr) { /// Лужа крови
        getBoload(x, y, textureRegion, 0.2f, MathUtils.random(.2f, 1.7f), xr, yr, true);
    }

    private void getPoolVest(int x, int y, TextureRegion textureRegion, int xr, int yr, Color color) { /// Желет цветой
        getBoload(x, y + 60, textureRegion, 0, MathUtils.random(.2f, .7f), xr, yr, true);
    }

    public void upDate(float deltaTime) {
        this.upDateBillet(deltaTime);
        this.reboundTimer += deltaTime;

        for (Blood blood : myPriorityQueue) {
            if (blood.score < 1) blood.score += 1.1f * deltaTime; // размер крови

            if (blood.timer < blood.actiontimer) {
                blood.timer += deltaTime / 2;

                OperationVector.setTemp_vector(blood.x, blood.y);
                OperationVector.getTemp_vector().add(blood.angle.cpy().scl(.75f));

                blood.x = (int) OperationVector.get_Setter_Temp_vector().x;
                blood.y = (int) OperationVector.get_Setter_Temp_vector().y;
            } else continue;

        }

        updaeteAd(deltaTime); // это упдате Ад

    }

    public void renders() {
        renderBlood(mainGaming.getBatch());
        renderBulet(mainGaming.getBatch());
        //renderAd(mainGaming.getBatch(), mainGaming);
    }

    public void renderBlood(SpriteBatch spriteBatch) {
        this.upDate(Gdx.graphics.getDeltaTime());

        int i = 0;
        float a = 0;
        for (Blood b : myPriorityQueue) {
            i++;
            if (!b.isLive()) continue;
            if (b.transparent) mainGaming.getBatch().setColor(1, 1, 1, .68f);


            // System.out.println(i + "   " + myPriorityQueue.size() +"  " +  ( (i - myPriorityQueue.size()) * -1   ));
            //  a = M_Util.map(i,myPriorityQueue.size()*.8f,0,1,0);
//                spriteBatch.setColor(
//                        spriteBatch.getColor().r,
//                        spriteBatch.getColor().g,
//                        spriteBatch.getColor().b,
//                       a
//                );

            if (b.color != null) {
                spriteBatch.setColor(b.color);
            }

            spriteBatch.draw(b.texture, b.getX() - 125, b.getY() - 125, 125, 125, 250, 250, b.score, b.score, b.angle.angle() + b.flip);
            spriteBatch.setColor(1, 1, 1, 1);
        }
        //System.out.println();
    }

    /////////////////////////////////////////////////////////////////
    public Bullet getBullet(Vector2 direction, int startX, int startY) {
        Bullet b = Bullet.startBulletFly(this);
        b.zeroBullet();
        b.setLive(true);
        b.setStepX(direction.cpy().nor().x);
        b.setStepY(direction.cpy().nor().y);
        b.setPoition(startX, startY);
        return b;
    }

    public void upDateBillet(float dt) {
        for (com.mygdx.game.BloodBullet.Bullet b : myBulletQueue) {
            if (!b.isLive()) continue;
            if (!b.upDatePosition(this, dt)) {
                // myBulletQueue.add(new BulletHit(new Vector2(1,1),true,45));
                continue;
            }
        }
        //if (StaticService.selectWithProbability(1)) startNewAd();
        //System.out.println(slidingAdDeque.size());
    }

    public void renderBulet(SpriteBatch spriteBatch) {

//        if(StaticService.selectWithProbability(1))startingAdPlus();
//        if(StaticService.selectWithProbability(1))startingAdLose();
//        if(StaticService.selectWithProbability(1))startingAdWiner();
        if (StaticService.selectWithProbability(75))
            //this.getBullet(new Vector2(MathUtils.random(-250, 390), MathUtils.random(-250, 390)), MathUtils.random(250, 390), MathUtils.random(250, 390));
            for (com.mygdx.game.BloodBullet.Bullet b : myBulletQueue) {
                if (!b.isLive()) {
//                    spriteBatch.setColor(0,0,0,.6f);
//                    spriteBatch.draw(textureRegions.get(5), b.getPoition().x, b.getPoition().y, 40, 40);
//                    spriteBatch.setColor(1,1,1,1);
                    continue;
                }
                if (b.getNumberSteps() < 5) continue;
                if (b.getNumberSteps() > 20) b.setLive(false);

                spriteBatch.setColor(1, 1, 1, .8f);
                spriteBatch.draw(textureRegions.get(62), b.getPoition().x, b.getPoition().y, 24, 24);
                for (int i = 0; i < b.getNumberSteps(); i++) {
                    if (StaticService.selectWithProbability(50)) continue;
                    spriteBatch.draw(textureRegions.get(62), b.getPoition().x - b.getStepX() * i * 15 + MathUtils.random(-5, 5), b.getPoition().y - b.getStepY() * i * 15 + MathUtils.random(-5, 5), 10, 10);
                }
                spriteBatch.setColor(1, 1, 1, 1);
            }
    }

    public void addBulletOtherPlayerPistol(int id) {

        Vector2 p = new Vector2(mainGaming.getHero().getOtherPlayers().getXplayToId(id), mainGaming.getHero().getOtherPlayers().getYplayToId(id));
        Vector2 cook = new Vector2(10, 10);
        try {
            mainGaming.getHero().getLith().startBulletFlash(p.x, p.y); ///вспышка
        } catch (Exception e) {

        }
        cook.setAngle(mainGaming.getHero().getOtherPlayers().getRotationToId(id));
        Vector2 delta = new Vector2(cook);
        delta.rotate(20).scl(70);
        mainGaming.getHero().getPoolBlood().getBullet(cook, (int) (p.x - delta.x), (int) (p.y - delta.y));

    }

    public void addBulletOtherPlayerShootGun(int id) {
        Vector2 p = new Vector2(mainGaming.getHero().getOtherPlayers().getXplayToId(id), mainGaming.getHero().getOtherPlayers().getYplayToId(id));
        Vector2 cook = new Vector2(10, 10);
        try {
            mainGaming.getHero().getLith().startBulletFlash(p.x, p.y); ///вспышка
        } catch (Exception e) {

        }

        cook.setAngle(mainGaming.getHero().getOtherPlayers().getRotationToId(id));
        Vector2 delta = new Vector2(cook);
        for (int i = -10; i < 10; i += 4) {
            mainGaming.getHero().getPoolBlood().getBullet(cook.cpy().rotate(i), (int) (p.x - delta.x), (int) (p.y - delta.y));
        }

    }
    /////////////////////

    private void updaeteAd(float dt) {
        for (SlidingAd slidingAd : slidingAdDeque) {
            if (!slidingAd.isLive()) continue;
            slidingAd.upDateAd(dt);
        }
    }

    public void renderAd(SpriteBatch spriteBatch, MainGaming mainGaming) {
        for (SlidingAd slidingAd : slidingAdDeque) {
            if (!slidingAd.isLive()) continue;
            slidingAd.renderAd(spriteBatch, mainGaming);
        }
    }

    ///////
    public void startingAdLose() {
        SlidingAd slidingAd = this.slidingAdDeque.pollFirst();
        slidingAd.starterNewAd(100, 100, 3f, textureRegions.get(21), 1.4f);
        slidingAdDeque.addLast(slidingAd);
    }

    public void startingAdLostLead() {
        SlidingAd slidingAd = this.slidingAdDeque.pollFirst();
        slidingAd.starterNewAd(100, 100, 3f, textureRegions.get(25), 1.4f);
        slidingAdDeque.addLast(slidingAd);
    }

    public void startingAdWiner() {
        SlidingAd slidingAd = this.slidingAdDeque.pollFirst();
        slidingAd.starterNewAd(100, 100, 2f, textureRegions.get(22), 1.4f);
        slidingAdDeque.addLast(slidingAd);
    }

    public void startingAdPlus() {
        SlidingAd slidingAd = this.slidingAdDeque.pollFirst();
        slidingAd.starterNewAd(100, 100, .9f, textureRegions.get(20), 4f);
        slidingAdDeque.addLast(slidingAd);
    }

    public void startingAdStart() {
        SlidingAd slidingAd = this.slidingAdDeque.pollFirst();
        slidingAd.starterNewAd(100, 100, .9f, textureRegions.get(23), 3f);
        slidingAdDeque.addLast(slidingAd);
    }

    public void startingAdFirst() {
        SlidingAd slidingAd = this.slidingAdDeque.pollFirst();
        slidingAd.starterNewAd(100, 100, 2f, textureRegions.get(24), 1.4f);
        slidingAdDeque.addLast(slidingAd);
    }


    private void startNewAd() {
        SlidingAd slidingAd = this.slidingAdDeque.pollLast();
        slidingAd.starterNewAd(100, 100, .5f, textureRegions.get(6), 4);
        slidingAdDeque.addFirst(slidingAd);
    }

}
