package com.mygdx.game.Menu;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;

public class ShaderFilm extends ShaderProgram {
    private float timer = -0.5f;
    private float graytimer = 0;
    private float rgb;
    private boolean wantraise = false;
    private float raiseamount;
    private float grayextra;
    private static final String vert =
            "attribute vec4 a_position;" +
                    "attribute vec4 a_color;" +
                    "attribute vec2 a_texCoord0;" +
                    "uniform mat4 u_projTrans;" +
                    "varying vec4 v_color;" +
                    "varying vec2 v_texCoords;" +
                    "void main(){" +
                    "v_color=vec4(1, 1., 1., 1.);" +
                    "v_texCoords = a_texCoord0;" +
                    "gl_Position = u_projTrans * a_position;" +
                    "}";

    private static final String frag =        "#ifdef GL_ES\n" +
            "#define LOWP lowp\n" +
            "#define MED mediump\n" +
            "#define HIGH highp\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform float r0;\n" +
            "uniform float pomehi;\n" +
            "uniform float grayScale;\n" +
            "uniform float nIntensity;\n" +
            "uniform float sIntensity;\n" +
            "uniform float sCount;\n" +
            "uniform float sPosition;\n" +
            "uniform float amountRGB;\n" +
            "float random(vec2 seed,float time){\n" +
            "float x=degrees((sin(seed.x*time*82.))+cos(seed.y*time*918.4));\n" +
            "float y=degrees(cos(seed.y*time*82.))+cos(seed.x*time*984.);\n" +
            "return fract(sin(x*cos(y)));}\n" +
            "void main(void)\n" +
            "{\n" +
            "vec4 cTextureScreen = texture2D( u_texture,v_texCoords );\n" +
            "vec3 rnd=vec3(random(v_texCoords.xy,r0))*pomehi;\n" +
            "vec3 cResult = cTextureScreen.rgb + cTextureScreen.rgb * rnd;\n" +
            "vec2 sc = vec2( sin(( gl_FragCoord.y+sPosition) * sCount ), cos(( gl_FragCoord.y+sPosition) * sCount ) );\n" +
            "cResult += cTextureScreen.rgb * vec3( sc.x, sc.y, sc.x ) * sIntensity;\n" +
            "cResult += cTextureScreen.rgb + clamp( nIntensity, 0.0,1.0 ) * ( cResult - cTextureScreen.rgb );\n" +
            "cResult += vec3(cResult.r * 0.3 + cResult.g * 0.59 + cResult.b * 0.11 )*grayScale;\n" +
            "vec2 offset = amountRGB * vec2( cos(0.), sin(0.) );\n" +
            "vec4 cr = texture2D(u_texture, v_texCoords + offset);\n" +
            "vec4 cb = texture2D(u_texture, v_texCoords- offset);\n" +
            "cResult += vec3(cr.r,cResult.g, cb.b)/3.;\n" +
            "gl_FragColor = vec4( cResult*0.8, cTextureScreen.a );\n" +
            "}";

    public ShaderFilm() {
        super(vert, frag);
    }

    public float getTimer() {
        return timer;
    }

    public void setGrayScaleExtraAmount(float amount) {
        grayextra = amount;
    }

    public void start(float delta) {
        timer += delta * 10f;
        if (graytimer > 0) graytimer -= delta;

        if (MathUtils.random() > 0.99f) graytimer = MathUtils.random() * 1.5f;

        if (MathUtils.random() > 0.94f) {
            if (rgb <= 0) {
                wantraise = true;
                raiseamount = MathUtils.random() * 0.5f + 0.5f;
            }
        }
        if (wantraise) {
            rgb += delta;
        }
        if (rgb >= raiseamount) {
            rgb = raiseamount;
            wantraise = false;
        }
        if (!wantraise) rgb -= delta * 2f;
        if (rgb < 0) {
            rgb = 0;
        }
        begin();

        setUniformf("r0", MathUtils.random() * 0.5f + 0.5f);

        setUniformf("pomehi", MathUtils.random(50));

        setUniformf("sPosition", timer);

        setUniformf("grayScale", +rgb * 0.4f + grayextra);

        setUniformf("amountRGB", rgb * 0.012f);

        setUniformf("nIntensity", 0.9f);

        setUniformf("sIntensity", 0.8f);

        setUniformf("sCount", 1.2f);

        end();
    }

    //@Override
    public ShaderProgram getShader() {
        return this;
    }

}