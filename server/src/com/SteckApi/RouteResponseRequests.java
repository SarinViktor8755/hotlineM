package com.SteckApi;

import com.GameServer;
import com.Network;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Service.Key_cod;
import com.mygdx.game.Service.TimeService;

/**
 * Created by 1 on 05.01.2020.
 */

public class RouteResponseRequests {  // роутр запросов
    private GameServer gameServer;
    private StockBase stockBase;

    public RouteResponseRequests(GameServer gameServer) {
        this.gameServer = gameServer;
        this.stockBase = this.gameServer.getSnapShots().getStockBase();
    }

    public StockBase getStockBase() {
        return stockBase;
    }

    public boolean handleRequest(Network.StockMess in, int nomerP) { // первый большой роутер получения запроса (запрос с ответом)
        in.nomer_pley = nomerP;
       // System.out.println(in + "       :::::::");
        //System.out.println("out <<  " + stockBase.getOutMess().getSize());
        //System.out.println("in  >>  " + stockBase.getInMess().getSize());
        // System.out.println("Входящее сообщение -->> " + in + " Номер плейра :: " + nomerP +" tip "+ in.tip);
        if (!gameServer.indexMatch.isMatchRun()) { // если в матче пауза отсылать этот пакет в ответ
//            try {
//            Network.StockMess stockMess = new Network.StockMess();
//            stockMess.tip = -9;
//            stockMess.nomer_pley = nomerP;
//            stockMess.time_even = TimeService.getTimeGame() * (-1);
//            stockMess.p1 = (int) gameServer.indexMatch.getCurrentTimerMatch();// ВРЕМЯ PAUSE осталось
//            stockMess.p2 = -99;
//            stockMess.p3 = 0;// МЕСТО в РЕЙТЕНГЕ
//            stockMess.p4 = gameServer.indexMatch.fineFirstLiderFragsPlayer(); // раги лучшего игрока
//            gameServer.server.sendToAllUDP(stockMess);
//            }catch (NullPointerException e){
//                e.printStackTrace();
//                return false;
//            }
//            return true;
        }

        if (in.tip == Key_cod.STICK_ATTACK) { // ударил соперника = далеет расчет демеджа
            new Thread(new Runnable() {
                public void run() {
                    try {
                        if (stockBase.addInSteckIn(in, nomerP)) {
                            System.out.println(in.textM + ">>>   NikName");
                            stockBase.addOutSteckOut(in, nomerP); // записывает в стек ИН - если такое сообщение уже было - то просто хранться в ИН если нет, наоборот записывает
                            gameServer.calculationСontact.getHit(in.p1, in.p2, nomerP, 0, 110);//подсчет попадания удара

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR tip 1");
                    }
                }
            }).start();
            return true;
        }

        if (in.tip == Key_cod.GUN_SHOT) { // стрельба из пистолета;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Vector2 direction = new Vector2(in.p1, in.p2);
                        Vector2 temp_vector = new Vector2(1, 1).nor();
                        temp_vector.setAngle(in.p3);
                        int x, y;
                        if (stockBase.addInSteckIn(in, nomerP)) {
                            stockBase.addOutSteckOut(in, nomerP);
                            for (int step = 1; step <= 1200; step += 50) {
                                x = (int) (direction.x + temp_vector.x * step);
                                y = (int) (direction.y + temp_vector.y * step);
                                if (!gameServer.indexBot.getIndexMap().canMove(x, y)) return;
                                if (gameServer.calculationСontact.getHit(x, y, nomerP, 0, 50,in.p3,2) != Integer.MIN_VALUE) return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR tip 2");
                    }
                }
            }).start();
            return true;
        }

        if (in.tip == Key_cod.SHOTGUN_SHOT) { // стрельба из ружья
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Vector2 direction = new Vector2(in.p1, in.p2);
                        Vector2 temp_vector = new Vector2(1, 1).nor();
                        temp_vector.setAngle(in.p3);

                        int x, y;
                        if (stockBase.addInSteckIn(in, nomerP)) {
                            stockBase.addOutSteckOut(in, nomerP);
                            int dispersion_bullet = 20; // разлет пули
                            //System.out.println("--------------------------");
                            for (int step = 1; step <= 900; step += 55) {
                                x = (int) (direction.x + temp_vector.x * step);
                                y = (int) (direction.y + temp_vector.y * step);
                                if (!gameServer.indexBot.getIndexMap().canMove(x, y)) return;
                                //System.out.println(60 + step/5);
                                gameServer.calculationСontact.getHit(x, y, nomerP, 0, 60 + step / 5,in.p3,3);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("ERROR tip 3");
                    }
                }
            }).start();
            return true;
        }
////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////
//        ////////////////////////////////////////////////////////////
//
//        if (in.tip == 4) { // стрельба из чего то еще
//            if (stockBase.addInSteckIn(in, nomerP)) {
//                stockBase.addOutSteckOut(in, nomerP);
//                gameServer.calculationСontact.getHitStick(in.p1, in.p2, nomerP, 0, 20);//подсчет попадания удара
//            }
//        }



        if (in.tip == Key_cod.REQUEST_FOR_PARAMETERS) { // ЗАПРОС ПАРАМЕТРОВ
            Network.StockMess stockMess = new Network.StockMess();
            stockMess.tip = -9;
            stockMess.nomer_pley = nomerP;
            stockMess.time_even = TimeService.getTimeGame() * (-1);
            stockMess.p1 = (int) gameServer.indexMatch.getCurrentTimerMatch();// ВРЕМЯ МАТЧАЯ все ОК или время паузы
            stockMess.p3 = gameServer.indexMatch.getRaitongFromPlayer(nomerP);// МЕСТО в РЕЙТЕНГЕ
            stockMess.p4 = gameServer.indexMatch.fineFirstLiderFragsPlayer(); // раги лучшего игрока
            stockMess.p2 = gameServer.indexMatch.getFragFromPlayer(nomerP); // ФРАГИ
            gameServer.getSnapShots().getStockBase().getOutMess().addStockOutQuery(new RequestStockServer(stockMess, nomerP));
        }

        if (in.tip == Key_cod.RESPOWN_PLAYER) { // возраждение
            { // ударил соперника = далее расчет демеджа
                if (stockBase.addInSteckIn(in, nomerP)) {
                    stockBase.addOutSteckOut(in, nomerP); // записывает в стек ИН - если такое сообщение уже было - то просто хранться в ИН если нет, наоборот записывает
                    gameServer.snapShots.setLive(nomerP, true); // мертвый -> живой
                    gameServer.getSnapShots().clenSnapShotsToPleyer(nomerP);
                }


                return true;
            }
        }

        return false;
    }




}
