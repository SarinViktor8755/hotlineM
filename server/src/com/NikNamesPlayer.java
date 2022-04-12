package com;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class NikNamesPlayer {
    public Map<Integer, String> hashmap = new HashMap<Integer, String>();
    ArrayList<String> lResult;

    public NikNamesPlayer() {
        this.hashmap = new HashMap<Integer, String>();
        lResult = new ArrayList<>();
    }

    public String[] genrateArrayFromPlayer() {
        lResult.clear();
        for (Integer key : hashmap.keySet()) {
          //  System.out.println("ID = " + key + ", День недели = " +  hashmap.get(key));
            lResult.add(hashmap.get(key)+"||__||"+key);
        }
      //  System.out.println(Arrays.toString(lResult.toArray(new String[lResult.size()])));
        return lResult.toArray(new String[lResult.size()]);
    }


}
