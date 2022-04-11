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
    final  private  static  String sp =
            "#ifdef GL_ES   \n" +
                    "    #define LOWP lowp\n" +
                    "    precision mediump float;\n" +
                    "#else\n" +
                    "    #define LOWP\n" +
                    "#endif\n" +
                    "varying LOWP vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "// sampler2D это специальный формат данных в  glsl для доступа к текстуре\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main(){\n" +
                    "    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);// итоговый цвет пикселя\n" +
                    "}";
    final  private  static String vp =
            "attribute vec4 a_position; //позиция вершины\n" +
                    "attribute vec4 a_color; //цвет вершины\n" +
                    "attribute vec2 a_texCoord0; //координаты текстуры\n" +
                    "uniform mat4 u_projTrans;  //матрица, которая содержим данные для преобразования проекции и вида\n" +
                    "varying vec4 v_color;  //цвет который будет передан в фрагментный шейдер\n" +
                    "varying vec2 v_texCoords;  //координаты текстуры\n" +
                    "void main(){\n" +
                    "    v_color=a_color;\n" +
                    "    // При передаче цвет из SpriteBatch в шейдер, происходит преобразование из ABGR int цвета в float. \n" +
                    "    // что-бы избежать NAN  при преобразование, доступен не весь диапазон для альфы, а только значения от (0-254)\n" +
                    "    //чтобы полностью передать непрозрачность цвета, когда альфа во float равна 1, то всю альфу приходится умножать.\n" +
                    "    //это специфика libgdx и о ней надо помнить при переопределение  вершинного шейдера.\n" +
                    "    v_color.a = v_color.a * (255.0/254.0);\n" +
                    "    v_texCoords = a_texCoord0;\n" +
                    "    //применяем преобразование вида и проекции, можно не забивать себе этим голову\n" +
                    "    // тут происходят математические преобразование что-бы правильно учесть параметры камеры\n" +
                    "    // gl_Position это окончательная позиция вершины \n" +
                    "    gl_Position =  u_projTrans * a_position; \n" +
                    "}";

    public ShaderFilm() {
        super(vp, sp);
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