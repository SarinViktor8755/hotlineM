package com.mygdx.game.HUDAudio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MainGaming;

import java.util.ArrayDeque;


/**
 * Created by 1 on 07.10.2019.
 */

public class Hud implements Disposable {

    private Viewport viewport;
    private MainGaming mainGaming;
    private Stage stageHUD;
    private boolean connect;
    private EndingMathHUD endingMathHUD;
    private int nPlayer, myPosition, timeM, timeS, myFrags, timer, liderMath;
    private TextureRegion textureAim;
    public boolean first;

    private BitmapFont font;

    Label coinCountLabel;
    Label raitingTextLabel; // 1/3
    Label fragsTextLabel;
    Label timerTextLabel;
    Label liderMathLabel;
    Label notConnectLabel;


    public int getTimer() {
        return timer;
    }

    public int getMyFrags() {
        return myFrags;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public Hud(MainGaming mainGaming) {

        font = mainGaming.getAssetsManagerGame().get("fonts/font.fnt", BitmapFont.class);

        endingMathHUD = new EndingMathHUD(mainGaming);
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stageHUD = new Stage(viewport, mainGaming.getBatch());
        first = false;



        font.getData().setScale(.8f);
        font.getColor().set(.5f, .5f, .5f, 1);
        nPlayer = 0;
        myPosition = 0;
        timeM = 0;
        timeS = 0;
        myFrags = -10;
        liderMath = 0;
        this.timer = 180000;
        connect = true;

        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        raitingTextLabel = new Label("0/0", style);
        fragsTextLabel = new Label("frag : 0", style);
        timerTextLabel = new Label("0:0", style);
        liderMathLabel = new Label("asd", style);
        notConnectLabel = new Label("", style);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        table.add(raitingTextLabel).expandX().padTop(12.0f);
        table.add();
        table.add(timerTextLabel).expandX().padTop(12.0f);
        table.add();
        table.add(fragsTextLabel).expandX().padTop(12.0f);

        table.row();
        table.add();
        table.add(notConnectLabel);
        table.add();
        table.add();
        table.add(liderMathLabel).expandX().padTop(12.0f);

        coinCountLabel = new Label("", style);

        stageHUD.addActor(table);

    }

    public Stage getStageHUD() {
        return stageHUD;
    }

    public boolean isActual() {
        if (this.myFrags < 0) {
            return false;
        }
        return true;
    }

    public void update(int myPosition, int sizePlayer, int myFrags, int timer, int max_fargs) {
        this.nPlayer = sizePlayer;
        this.myPosition = myPosition;
        this.myFrags = myFrags;
        this.timer = timer;
        this.liderMathLabel.setText("1st: " + max_fargs);
        this.liderMath = max_fargs;
        this.endingMathHUD.setUpdateToServer(true);
    }

    public void update(float delta) {

        //////////////////////////
        this.timer = (int) (this.timer - (delta * 1000));
        raitingTextLabel.setText(myPosition + "/" + nPlayer);
        if (timeS > 9) timerTextLabel.setText(timeM + ":" + timeS);
        else timerTextLabel.setText(timeM + ":0" + timeS);
        fragsTextLabel.setText("Frag: " + myFrags);
        if (myFrags < 0) fragsTextLabel.setText("Frag: 0");
        transformationTime(this.timer);
        if (myPosition > nPlayer) raitingTextLabel.setText(myPosition + "/" + myPosition);
        if (timer < 0) timerTextLabel.setText("0:00");

        if (!isConnect()) {
            timerTextLabel.setText("No connection..!!!");
            raitingTextLabel.setText("N/C");
            raitingTextLabel.setText("N/C");
            liderMathLabel.setText("N/C");
            raitingTextLabel.setText("N/C");
            fragsTextLabel.setText("N/C");
            this.endingMathHUD.setUpdateToServer(false);
            this.endingMathHUD.setWeCanFinish(false);

        }

        this.endingMathHUD.update(delta);
    }

    public Label getTimerTextLabel() {
        return timerTextLabel;
    }

    public void setTimerTextLabel(Label timerTextLabel) {
        this.timerTextLabel = timerTextLabel;
    }

    private void transformationTime(int timer) {
        timeM = (timer / 1000) / 60;
        timeS = (timer / 1000) % 60;
    }

    public void render(float dt) {
        stageHUD.draw();

    }


    @Override
    public void dispose() {
        stageHUD.dispose();
    }

    public int getMyPosition() {
        return myPosition;
    }
}
