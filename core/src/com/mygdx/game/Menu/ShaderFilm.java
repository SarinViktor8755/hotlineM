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
    final private static String VERT =
            "attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
                    "attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +

                    "uniform mat4 u_projTrans;\n" +
                    " \n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +

                    "void main() {\n" +
                    "	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
                    "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "}";

    //no changes except for LOWP for color values
    //we would store this in a file for increased readability
    final private static String FRAG =
            //GL ES specific stuff
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "uniform vec2 u_resolution;\n" +
                    "uniform vec2 u_mouse;\n" +
                    "uniform float u_time;\n" +
                    "\n" +
                    "// 2D Random\n" +
                    "float random (in vec2 st) {\n" +
                    "    return fract(sin(dot(st.xy,\n" +
                    "                         vec2(12.9898,78.233)))\n" +
                    "                 * 43758.5453123);\n" +
                    "}\n" +
                    "\n" +
                    "// 2D Noise based on Morgan McGuire @morgan3d\n" +
                    "// https://www.shadertoy.com/view/4dS3Wd\n" +
                    "float noise (in vec2 st) {\n" +
                    "    vec2 i = floor(st);\n" +
                    "    vec2 f = fract(st);\n" +
                    "\n" +
                    "    // Four corners in 2D of a tile\n" +
                    "    float a = random(i);\n" +
                    "    float b = random(i + vec2(1.0, 0.0));\n" +
                    "    float c = random(i + vec2(0.0, 1.0));\n" +
                    "    float d = random(i + vec2(1.0, 1.0));\n" +
                    "\n" +
                    "    // Smooth Interpolation\n" +
                    "\n" +
                    "    // Cubic Hermine Curve.  Same as SmoothStep()\n" +
                    "    vec2 u = f*f*(3.0-2.0*f);\n" +
                    "    // u = smoothstep(0.,1.,f);\n" +
                    "\n" +
                    "    // Mix 4 coorners percentages\n" +
                    "    return mix(a, b, u.x) +\n" +
                    "            (c - a)* u.y * (1.0 - u.x) +\n" +
                    "            (d - b) * u.x * u.y;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 st = gl_FragCoord.xy/u_resolution.xy;\n" +
                    "\n" +
                    "    // Scale the coordinate system to see\n" +
                    "    // some noise in action\n" +
                    "    vec2 pos = vec2(st*5.0);\n" +
                    "\n" +
                    "    // Use the noise function\n" +
                    "    float n = noise(pos);\n" +
                    "\n" +
                    "    gl_FragColor = vec4(vec3(n), 1.0);\n" +
                    "}\n";

    public ShaderFilm() {
        super(VERT, FRAG);
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