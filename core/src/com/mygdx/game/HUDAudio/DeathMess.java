package com.mygdx.game.HUDAudio;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Characters.MainCharacter;
import com.mygdx.game.Service.NikName;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;


public class DeathMess {
    ArrayDeque<Mess> mess;
    Label ld1;
    Label ld2;
    Label ld3;
    ArrayList<Mess> a;


    public DeathMess(Label l1, Label l2, Label l3) {
        this.mess = new ArrayDeque<Mess>();
        a = new ArrayList<Mess>();
        ld1 = l1;
        ld2 = l2;
        ld3 = l3;
        for (int i = 0; i < 3; i++) {
            mess.addFirst(new Mess());
        }
    }

    public void updateMess(float delta, Label lhud1, Label lhud2, Label lhud3) {
        a.clear();
      //  System.out.println("updateMess");
        for (Mess p : mess) {
            if (p.getTimeLife() < 0) continue;
            p.setTimeLife(p.getTimeLife() - delta);
        }
        a.addAll(mess);
        lhud1.setColor(1, 1, 1, a.get(0).getTimeLife());
        lhud1.setText(a.get(0).getMess());
        lhud2.setColor(1, 1, 1, a.get(1).getTimeLife());
        lhud2.setText(a.get(1).getMess());
        lhud3.setColor(1, 1, 1, a.get(2).getTimeLife());
        lhud3.setText(a.get(2).getMess());
    }

    public void addMessDead(String p1, String p2) {
        Mess a = mess.pollLast();
        mess.addFirst(a);
        a.setTimeLife(1.5f);
        a.setMess(p1 +" > " + p2);

    }


}
