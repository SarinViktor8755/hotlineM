package com.mygdx.game.SpriteStackPack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.sprite.stacking.MyGdxGame;

public class SpriteStackPrecipitation implements SpriteStackInterface {
    SpriteBatch sb;
    Texture texture;

    float x, y; // позиция
    float dx, dy; // смещение слоев
    float hide; /// высота
    // float viewingAngle; // угол обзора
    float speed;

    public static float xw, yw;

    static Vector2 temp;


    public SpriteStackPrecipitation(float x, float y, SpriteBatch sb, float viewingAngle) {
        this.sb = sb;
        this.x = x;
        this.y = y;
        this.hide = 150;
        speed = 50;
        xw = 20;
        yw = 15;
        //this.viewingAngle = viewingAngle;
    }

    public SpriteStackPrecipitation(float x, float y, SpriteBatch sb) {
        this.sb = sb;
        this.x = x;
        this.y = y;
        speed = 50;
        xw = 20;
        yw = 15;


        // this.viewingAngle = 45; // 0 вид сверху
    }

    public void randerSpriteStack(float camX, float camY, Vector3 angelCamera, float viewingAngle, float deltaTime) {
        // System.out.println(angelCamera);
        this.hide -= speed * deltaTime;
        if (hide < 0) restart(camX, camY);
        // System.out.println(hide);

        this.x += xw * deltaTime;
        this.y += yw * deltaTime;

        dx = ((camX - x) / -300) + (angelCamera.x * viewingAngle / 10);
        if (Math.abs(camX - x) > 600) return;
        dy = ((camY - y) / -300) + (angelCamera.y * viewingAngle / 10);
        if (Math.abs(camY - y) > 600) return;
        float h = MathUtils.map(0, 50, 5, 10, hide);

        sb.setColor(1, 1, 1, 1f - h / 50f);
        sb.draw(texture, (x + this.hide * dx) - h / 2, (y + this.hide * dy), h, h);
        sb.setColor(1, 1, 1, 1);
    }

    public void randerSpriteStack(float camX, float camY, Vector3 angelCamera, float viewingAngle, float deltaTime, MyGdxGame mgg) {
        // System.out.println(angelCamera);
        this.hide -= speed * deltaTime;
        if (hide < 0) {
            restart(camX, camY);
            mgg.addSnowdrift(x, y);
        }
        // System.out.println(hide);

        this.x += xw * deltaTime;
        this.y += yw * deltaTime;

        dx = ((camX - x) / -300) + (angelCamera.x * viewingAngle / 10);
        if (Math.abs(camX - x) > 600) return;
        dy = ((camY - y) / -300) + (angelCamera.y * viewingAngle / 10);
        if (Math.abs(camY - y) > 600) return;
        float h = MathUtils.map(0, 50, 5, 10, hide);

        sb.setColor(1, 1, 1, 1f - h / 50f);
        sb.draw(texture, (x + this.hide * dx) - h / 2, (y + this.hide * dy), h, h);
        sb.setColor(1, 1, 1, 1);
    }

    public void addTexture(Texture texture) {
        this.texture = texture;
    }

    private void restart(float camX, float camY) {
        this.hide = MathUtils.random(50, 80);
        this.x = camX + MathUtils.random(-500, 500);
        this.y = camY + MathUtils.random(-500, 500);
        this.speed = MathUtils.random(30, 80);
    }

    @Override
    public float getDistance(float camX, float camY) {
        return temp.set(x, y).dst2(x, y);
    }

    public static void randodWindow() {
        SpriteStackPrecipitation.xw = MathUtils.random(-30, 30);
        SpriteStackPrecipitation.yw = MathUtils.random(-30, 30);
    }

}
