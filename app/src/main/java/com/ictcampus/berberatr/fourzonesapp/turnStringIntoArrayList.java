package com.ictcampus.berberatr.fourzonesapp;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by berberatr on 09.06.2017.
 */

public class turnStringIntoArrayList {
    String string;
    String[] strings;
    ArrayList<String[]> arrayList = new ArrayList<>();

    public turnStringIntoArrayList(String in){
        this.string = in;
    }

    public ArrayList<String[]> getArrayList(){
        doSplitting();
        return arrayList;
    }

    private void doSplitting(){
        split();
        splitAgain();
    }


    /**
     * Split the String along the double Pipe-Lines into a String[]
     */
    private void split(){
        strings = string.split("\\|\\|");
    }

    /**
     * Split each String inside the String[] into a new String[], and insert this new String[] into an ArrayList.
     */
    private void splitAgain(){
        for (String i: strings) {
            String[] temp = i.split("\\|");
            arrayList.add(temp);
        }
    }

    /**
     * Print each String[] inside the ArrayList onto a separate Line into the Console. Space each Entry from each Array[].
     */
    public void showArrayList(){
        Iterator iterator = arrayList.iterator();
        while(iterator.hasNext()){
            String[] temp = (String[])iterator.next();
            for (String i: temp) {
                System.out.print(i+" ");
            }
            System.out.println("");
        }
    }
}
