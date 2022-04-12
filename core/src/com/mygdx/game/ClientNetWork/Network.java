package com.mygdx.game.ClientNetWork;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;


public class Network {
    final public static int udpPort = 37960, tcpPort = 37960;
    //final public static String ip = "176.62.66.63";

    //final public static String ip = "185.231.68.81";
    //final public static String ip = "omskSarin2020.online";
    // final  public static String ip = "127.0.0.1";
   final public static String ip = "92.124.144.204";

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Integer.class);
        kryo.register(PleyerPosition.class);
        kryo.register(PleyerPositionNom.class);
        kryo.register(rc.class);
        kryo.register(StockMess.class);
        kryo.register(Answer.class);
        kryo.register(UpdateNames.class);
        kryo.register(String[].class);
    }

    /////////////////////////////////////
    public static class PleyerPosition {   //позиция
        public Integer xp;
        public Integer yp;
        public Integer rot;
    }

    public static class PleyerPositionNom {   //ответ позиция с номером
        public Integer nom;
        public Integer xp;
        public Integer yp;
        public Integer rot;
    }

    static public class rc {
        public Integer id;
    }

    public static class StockMess {   //сообщение из стока
        public Integer time_even;
        public Integer tip;
        public Integer p1;
        public Integer p2;
        public Integer p3;
        public Integer p4;
        public Integer p5;
        public Integer p6;
        public String textM;
        public Integer nomer_pley;

        @Override
        public String toString() {
            return "StockMess{" +
                    "time_even=" + time_even +
                    ", tip=" + tip +
                    ", p1=" + p1 +
                    ", p2=" + p2 +
                    ", p3=" + p3 +
                    ", p4=" + p4 +
                    ", p5=" + p5 +
                    ", p6=" + p6 +
                    ", textM='" + textM + '\'' +
                    ", nomer_pley=" + nomer_pley +
                    '}';
        }
    }


    static public class Answer { // ответ типа получил от сервера
        public Integer nomber;
    }

    static public class UpdateNames {
        public String[] names;
    }
}
