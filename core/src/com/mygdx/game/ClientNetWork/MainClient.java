package com.mygdx.game.ClientNetWork;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.ClientNetWork.Network.PleyerPosition;
import com.mygdx.game.ClientNetWork.Network.PleyerPositionNom;
import com.mygdx.game.ClientNetWork.Network.rc;
import com.mygdx.game.ClientNetWork.SteckApi.*;
import com.mygdx.game.MainGaming;

import com.mygdx.game.Service.Key_cod;
import com.mygdx.game.Service.NikName;
import com.mygdx.game.Service.TimeService;
import com.mygdx.game.VoiceChat.VoiceChatClient;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class MainClient {

    private StockRequestService inStock;
    private StockRequestService outStock;
    //private MessageQueue messageQueue;

    private float pingError; //коэфицент ошибки
    public Client client;   //клиент
    private MainGaming mg;
    private int timerReConnect;

    public int myIdConnect; //Мой ИД

    private VoiceChatClient voiceChatClient;


    public MainClient(MainGaming mg) {
        int bufferSize = 22050; // Recommened value.
        pingError = 0;
        this.mg = mg;

        client = new Client(bufferSize,bufferSize);
        client.start();

        // FrameworkMessage.Ping ping = new FrameworkMessage.Ping();
        Network.register(client);

        /////////////////VC
        voiceChatClient = new VoiceChatClient(client.getKryo());
        voiceChatClient.addReceiver(client);
//////////////////

        client.addListener(new Listener() {

            public void connected(Connection connection) {
                setMyIdConnect(connection.getID());
                sendMyNik();
            }

            public void received(Connection connection, Object object) {
                router(object);

            }

            public void disconnected(Connection connection) {
            }
        });

        inStock = new com.mygdx.game.ClientNetWork.SteckApi.StockRequestService(mg);
        outStock = new com.mygdx.game.ClientNetWork.SteckApi.StockRequestService(mg);
        // messageQueue = new MessageQueue();
    }

    public VoiceChatClient getVoiceChatClient() {
        return voiceChatClient;
    }

    public boolean reconnect() {

        // System.out.println("--------------  " + timerReConnect);
        mg.getHud().update(0, 0, 0, 0, 0);
        try {
            // System.out.println(timerReConnect);
            timerReConnect -= 1;
            if (timerReConnect > 0) return false;
            timerReConnect = 5;
            mg.getHero().clearOtherPlayer();
            try {
                //client.stop();
                //client.start();
                client.reconnect();
            } catch (SocketTimeoutException e) {
                System.out.println("SocketTimeoutException");
            }

            mg.getHero().getOtherPlayers().getPlayersList().clear();
            System.out.println("reconnect");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            //reconnect();
        }
        return true;

    }


    public int getAndUpdateRealTime() {
        return TimeService.getTimeGame();
    }

    public void actionMainClient() {                // выполнить все действия МайнКлиента
        mg.getHud().setConnect(client.isConnected());
        sendToServerMyCooedinat();
        outStock.sendMyMessToServer();              // отправить все сообщения на сервер с возвратом
        inStock.processingOfIncomingMessages();     // обработать все сообщения с сервера
        if (!mg.getHud().isActual()) requestCurrentParameters();
    }

    public StockRequestService getInStock() {
        return inStock;
    }

    public void setInStock(StockRequestService inStock) {
        this.inStock = inStock;
    }

    public StockRequestService getOutStock() {
        return outStock;
    }

    public void setOutStock(StockRequestService outStock) {
        this.outStock = outStock;
    }

    public boolean coonectToServer() {

        try {
            client.start();
            client.connect(5000, Network.ip, Network.tcpPort, Network.udpPort);
            //client.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Gdx.app.error("MyTag", "!!!!!!ConcurrentModificationException");
            return false;
        }
    }


    public void coonectToServerNewThred() {
        System.out.println("coonectToServerNewThred");
        Thread t = new Thread(new Runnable() {
            public void run() {
                coonectToServer();
            }

        });
        t.start();


    }


    public int getMyIdConnect() {
        return myIdConnect;
    }

    public MainClient setMyIdConnect(int myIdConnect) {
        this.myIdConnect = myIdConnect;
        pingError = 0;
        return this;
    }

    public boolean isConnectToServer() {
        return client.isConnected();
    }


    public void requestCurrentParameters() {  // запрос актуальных параметров
        if (MathUtils.random(15) != 1) return;
        try {
            getOutStock().addStockInQuery(new RequestStock(// отправить на сервер - возраждение
                    getAndUpdateRealTime(), 502,
                    null, null,
                    null, null, null, null, null, null
            ));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void sendToServerMyCooedinat() { // сообщить серверу мои координаты
        if (!client.isConnected()) {
            reconnect();
            return;
        }
        PleyerPosition ps = new PleyerPosition();
        updatePingError();

        ps.xp = Integer.valueOf((int) (mg.getHero().getPosition().x));
        ps.yp = Integer.valueOf((int) (mg.getHero().getPosition().y));
        ps.rot = Integer.valueOf((int) mg.getHero().getCookAngle().angle());

        //System.out.println("-------------------------");
//        System.out.println(ps.xp);
//        System.out.println(ps.yp);
//        System.out.println(ps.rot);

        client.sendUDP(ps);

        // Gdx.app.error("MyTag", String.valueOf(pingError));
    }

    public void updatePingError() {
        if (mg.getHero().getOtherPlayers().getPlayersList().size() < 1) return;
        float res = (mg.getHero().getPosition().x - mg.getHero().getOtherPlayers().getXplayToId(myIdConnect)) * mg.getHero().getVelocity().x;
        if (res > 3) pingError += .001f;
        else {
            if (res < -3) pingError += -.001f;
        }
        if (pingError > .1f) pingError = .1f;
    }

    public float getPing() {
        FrameworkMessage.Ping ping = new FrameworkMessage.Ping();
        int lastPingID = 0;
        float lastPingSendTime = 0;
        ping.id = lastPingID++;
        lastPingSendTime = System.currentTimeMillis();
        client.sendTCP(ping);
        return lastPingID;
    }

    public void router(Object object) {
/////////////////////////////////////////////////////////////////////Запрос с ответом
        if (object instanceof PleyerPositionNom) {  //получает данные от сервера = если обьект в колекции есть - то обнавляет если нет создает нового
            int nom = ((PleyerPositionNom) object).nom;
            int xp = ((PleyerPositionNom) object).xp;
            int yp = ((PleyerPositionNom) object).yp;
            int rp = ((PleyerPositionNom) object).rot;

            try {
                if (nom == mg.getMainClient().myIdConnect) return;
                mg.getHero().getOtherPlayers().getPlayerToID(nom).updateCoordinatPleyer(xp, yp, rp);
            } catch (NullPointerException e) {
                Gdx.app.error("MyTag", "юзер плейра в коллекции");
            }

        }


        if (object instanceof Network.Answer) { //ответ сервера - ответ на ответ - от сервера
            int nom = ((Network.Answer) object).nomber;
            if (!getOutStock().isCompleted(nom)) {
                getOutStock().markCompleted(nom);
            }
        }

        if (object instanceof Network.StockMess) {  // получение стокового сообщения - и отпрвка ответа
            int eventTime = ((Network.StockMess) object).time_even;

            //-----------------------------------
            Network.Answer answer = new Network.Answer();
            answer.nomber = eventTime;
            client.sendUDP(answer); // ответ
            // передаем входящие сообщение в стек обработки


            if (mg.getMainClient().getInStock().isExists(((Network.StockMess) object).time_even))
                return;
            inStock.addStockInQuery(
                    new com.mygdx.game.ClientNetWork.SteckApi.RequestStock(
                            ((Network.StockMess) object).time_even,
                            ((Network.StockMess) object).tip,
                            ((Network.StockMess) object).p1,
                            ((Network.StockMess) object).p2,
                            ((Network.StockMess) object).p3,
                            ((Network.StockMess) object).p4,
                            ((Network.StockMess) object).p5,
                            ((Network.StockMess) object).p6,
                            ((Network.StockMess) object).nomer_pley,
                            ((Network.StockMess) object).textM
                    ));
        }
//////////////////////////////////////////////////////////////////
        if (object instanceof PleyerPosition) {
            int x = ((PleyerPosition) object).xp;
            int y = ((PleyerPosition) object).yp;
            int r = ((PleyerPosition) object).rot;
        }

        if (object instanceof Network.UpdateNames) {
            Network.UpdateNames ub = (Network.UpdateNames) object;
            //System.out.println("!!!   " + Arrays.toString(ub.names));
            for (int i = 0; i < ub.names.length; i++) {
//                System.out.println();
//                System.out.println(ub.names[i]);
//                System.out.println(ub.names[i].indexOf("||__||"));
                String s=ub.names[i];
                String name = s.substring(0,s.indexOf("||__||"));
                int id = Integer.valueOf(s.substring(name.length()+6,ub.names[i].length()));
                mg.getHero().getOtherPlayers().setNikName(id,name);
               //  System.out.println("name  " + name + "  " + id);

            }


        }

        if (object instanceof rc) { // дисконект пользователя
            try {
                int id = ((rc) object).id;
                mg.getHero().getOtherPlayers().getPlayersList().remove(id);
            } catch (NullPointerException e) {
                System.out.println("NullPointerException ROUTER");
            }
        }


    }


    private void sendMyNik() {
        mg.getMainClient().getOutStock().addStockInQuery(new RequestStock(// отправить на сервер
                mg.getMainClient().getAndUpdateRealTime(), Key_cod.MY_NIK_TO_SERVER,
                null, null,
                null, null, null, null, null, NikName.getNikName()
        ));
    }

}






