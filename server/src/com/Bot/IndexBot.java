package com.Bot;

import com.Bot.Map.IndexMap;
import com.GameServer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by qwerty on 18.02.2020.
 */

public class IndexBot extends Thread {
    private GameServer gameServer;
    private HashMap<Integer, SimulationUnit> units;
    private IndexMap indexMap;
    private final int MIN_PLEYSES;
    private boolean interrupted; // если ТРУЕ - выключает поток

    public IndexBot(GameServer gameServer, int mixPlayer) {
        indexMap = new IndexMap();
        this.gameServer = gameServer;
        units = new HashMap<>();
        MIN_PLEYSES = mixPlayer;
        interrupted = false;
        ////////////////////////////////////////////
    }

    public void run() {
        final long tackt = 24;
        while (true) {
            try {
                Thread.sleep(tackt);
                //System.out.println("bots 1" + this.units);
//                System.out.println(gameServer.getSnapShots().getPlayrsLisn().size());
//                //System.out.println(gameServer.getSnapShots().numberLivePlayers());
                if(!gameServer.getSnapShots().numberLivePlayers()) Thread.sleep(500);
                if(gameServer.getSnapShots().getPlayrsLisn().size() < MIN_PLEYSES ) {
                    int id = MathUtils.random(-1000, 1);
                    units.put(id, new SimulationUnit(this, id, tackt));
                }
                try {
                    if(!getGameServer().getIndexMatch().isMatchRun()) continue;
                for (Map.Entry<Integer, SimulationUnit> entry : units.entrySet()) {
                        entry.getValue().getLogicBot().actionUnits();
                    if (gameServer.getSnapShots().getPlayrsLisn().size() > MIN_PLEYSES) {
                        entry.getValue().disconect(entry.getKey());
                    }
                }

                }catch (NullPointerException e){
                    if(!gameServer.getIndexMatch().isMatchRun()) {
                        Thread.sleep(2000);
                    }continue;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            } catch (ConcurrentModificationException e) {
                //e.printStackTrace();
                continue;
            }
        }
    }

    private int getSizePlayers() {
        return gameServer.getListP(0).size();
    }

    public HashMap<Integer, SimulationUnit> getUnits() {
        return units;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public IndexMap getIndexMap() {
        return indexMap;
    }


    static String getNikNameGen() {
        ArrayList<String> names = new ArrayList<>();
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
        return names.get(MathUtils.random(names.size() - 1)) + "@Bot";
    }



}
