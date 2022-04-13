package com.mygdx.game.SpriteStackPack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


import java.util.ArrayList;

public class SpriteStack implements SpriteStackInterface{
    SpriteBatch sb;
    ArrayList<Texture> sprite;

    float x, y; // позиция
    float dx, dy; // смещение слоев
    float w, h; // размеры
   // float viewingAngle; // угол обзора

    static Vector2 temp  = new Vector2();


    public SpriteStack( float x, float y,SpriteBatch sb,float viewingAngle) {
        this.sb = sb;
        this.sprite = new ArrayList<Texture>();
        this.x = x;
        this.y = y;
        //this.viewingAngle = viewingAngle;
    }

    public SpriteStack(float x, float y,SpriteBatch sb) {
        this.sb = sb;
        this.sprite = new ArrayList<Texture>();
        this.x = x;
        this.y = y;
        this.w = 50;
        this.h = 50;
       // this.viewingAngle = 45; // 0 вид сверху
    }

    public SpriteStack(float x, float y,float with,float hide,SpriteBatch sb) {
        this.sb = sb;
        this.sprite = new ArrayList<Texture>();
        this.x = x;
        this.y = y;
        this.w = with;
        this.h = hide;
        // this.viewingAngle = 45; // 0 вид сверху
    }

    public void randerSpriteStack(float camX, float camY, Vector3 angelCamera,float viewingAngle) {
       // System.out.println(angelCamera);
        dx = ((camX - x) / -300) + (angelCamera.x * viewingAngle/10);
        if(Math.abs(camX- x) > 600) return;
        dy = ((camY - y) / -300) + (angelCamera.y * viewingAngle/10);
        if(Math.abs(camY- y) > 600) return;

        for (int i = 0; i < sprite.size(); i++) {
            sb.draw(sprite.get(i), x + i * dx, y + i * dy, w, h);
        }
    }

    public void addTexture(Texture texture) {
        this.sprite.add(texture);
    }

    public void addTexture(Texture texture, int repeat) {
        for (int i = 0; i < repeat; i++) {
            this.sprite.add(texture);
        }
    }

    @Override
    public float getDistance(float camX, float camY) {
        return temp.set(camX,camY).dst2(x,y);
    }
}
