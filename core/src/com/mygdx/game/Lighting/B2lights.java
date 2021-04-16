package com.mygdx.game.Lighting;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MainGaming;

import java.util.ArrayList;

import box2dLight.ConeLight;

import box2dLight.RayHandler;


public class B2lights {
//    private World world;

    private box2dLight.PointLight pointLightHero;
    private ObFromLight object;
    private RayHandler rayHandlerHero;

    private ArrayList<box2dLight.PointLight> pointLightsList = new ArrayList<box2dLight.PointLight>();

    private ConeLight coneLightHero;

    private BuletFlash buletFlash;

    public B2lights(MainGaming mg) {
        //Gdx.app.log("Gdx version", com.badlogic.gdx.Version.VERSION);
//        this.world = mg.getWorld();
        pointLightsList = new ArrayList<box2dLight.PointLight>();
        RayHandler.useDiffuseLight(true);
        this.rayHandlerHero = new RayHandler(new World(new Vector2(0,0),true));
//        object = new ObFromLight(this.world); // припятсвия
//        object.crearBodys(mg.getIndexMap().getTopQualityMap_Box());
        box2dLight.PointLight pl;

        for (int i = 0; i < 5000; i += 1000) {
            for (int j = 0; j < 5000; j += 1000) {
                pl = new box2dLight.PointLight(rayHandlerHero, 8, getColorFromPoint(), 2300, j, i);
                 pl.setIgnoreAttachedBody(false);
       //
                pointLightsList.add(pl);
            }
        }

        pointLightHero = new box2dLight.PointLight(rayHandlerHero, 4, Color.WHITE, 700, 0, 0); /// свитильник героя
        coneLightHero = new ConeLight(rayHandlerHero, 65, Color.WHITE, 800, 0, 0, 90, 60);
//
        buletFlash = new BuletFlash(rayHandlerHero);


//        for (Body cars : object.getBodyList()) {
//            pl = new box2dLight.PointLight(rayHandlerHero, 8, getColorFromPoint(), 1500, cars.getPosition().x, cars.getPosition().y);
//            pl.attachToBody(cars);
//        }

        rayHandlerHero.setAmbientLight(.7f);
       // rayHandlerHero.setShadows();
    }


    Color getColorFromPoint() {
        Color colorPoint;
        if (MathUtils.randomBoolean(.1f)) colorPoint = Color.CHARTREUSE;
        else if ((MathUtils.randomBoolean(.1f))) colorPoint = Color.RED;
        else if ((MathUtils.randomBoolean(.1f))) colorPoint = Color.NAVY;
        else if ((MathUtils.randomBoolean(.1f))) colorPoint = Color.BLUE;
        else if ((MathUtils.randomBoolean(.2f))) colorPoint = Color.OLIVE;
        else if ((MathUtils.randomBoolean(.3f))) colorPoint = Color.YELLOW;
        else colorPoint = Color.BROWN;
        return colorPoint;
    }


    public void upDateLights(float xHero, float yHero, float align) {
//        world.step(1 / 60f, 1, 1);
        coneLightHero.setPosition(xHero, yHero);
        pointLightHero.setPosition(xHero, yHero);
        coneLightHero.setDirection(align);
        buletFlash.upDate();
        if(MathUtils.randomBoolean(.02f))
        coneLightHero.setDistance(MathUtils.random(500,1000));
    }

    public void renderLights(Camera camera) {
        rayHandlerHero.setCombinedMatrix((OrthographicCamera) camera);
        rayHandlerHero.updateAndRender();
//        float a = MathUtils.random(1f);
//        System.out.println(a);
//        rayHandlerHero.setAmbientLight(a);
    }


    public boolean isAtShadow(float x, float y) {
        return rayHandlerHero.pointAtShadow(x, y);
    }


    public void startBulletFlash(float x, float y) {
        buletFlash.newFlesh(x, y);
    }

}
