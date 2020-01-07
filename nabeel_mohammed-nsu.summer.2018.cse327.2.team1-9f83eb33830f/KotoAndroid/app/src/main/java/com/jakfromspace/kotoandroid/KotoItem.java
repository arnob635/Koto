package com.jakfromspace.kotoandroid;

import java.util.Calendar;

/**
 * Coded by JAKfromSpace on 26-Aug-18 for Koto.
 */
public class KotoItem {
    private int imageRes;
    private String textMain;
    private float textSubAmount;
    public String dateTaken;
    private boolean isExpense;

    public KotoItem(String text1, float text2){
        //imageRes = imRes;
        textMain = text1;
        textSubAmount = text2;
        dateTaken = GetDateTaken();
    }

    public int getImageRes(){
        return imageRes;
    }
    public String getTextMain(){
        return textMain;
    }
    public float getTextSubAmount(){
        return textSubAmount;
    }
    public String getDateTaken() {
        return dateTaken;
    }


    public  void setTextMain(String text){
        textMain = text;

    }

    public void setSubAmount (float num){
        textSubAmount = num;
    }

    public void isExpense(boolean value){
        isExpense = value;
    }

    public  void setDateTaken(String dateGiven) {
        dateTaken = dateGiven;
    }

    public static String GetDateTaken(){
        return java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }


}
