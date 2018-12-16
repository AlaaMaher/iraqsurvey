package com.example.asamir.iraqproject;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by ASI on 4/5/2017.
 */

 public class ConstMethods {

    public static final String MyPREFERENCES = "PREF" ;

    public static final String MyPREFERENCEdadsS = "PREF" ;
    public  static  void saveBasicInformationData(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("basicinfo", value);
        editor.apply();

    }
    public  static  String getSavedBasicInformationData( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("basicinfo", "");
    }

    public  static  void SavePageOneData(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("one", value);
        editor.apply();
    }
    public  static  String getPageOneData( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("one", "");
    }
    public  static  void SavePageTwoData(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("two", value);
        editor.apply();
    }
    public  static  String getPageTwoData( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("two", "");
    }
    public  static  void SavePageThreeData(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("three", value);
        editor.apply();
    }
    public  static  String getPageThreeData( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("three", "");
    }
    public  static  void SavePageFourData(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("four", value);
        editor.apply();
    }
    public  static  String getPageFourData( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("four", "");
    }

    public static  void saveUserId(Context context,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("userId", value);
        editor.apply();
    }

    public static String getSavedUserId(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("userId", "");
    }



    public static  void saveRooms(Context context,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("room", value);
        editor.apply();
    }

    public static String getRooms(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("room", "");
    }


    public static  void saveSketch(Context context,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("sketch", value);
        editor.apply();
    }

    public static String getSketch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("sketch", "");
    }


    public static  void saveOutDoorPhotos(Context context,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("outDoor", value);
        editor.apply();
    }

    public static String getOutDoorPhotos(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("outDoor", "");
    }
    public static  void saveInDoorPhotos(Context context,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("inDoor", value);
        editor.apply();
    }

    public static String getInDoorPhotos(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("inDoor", "");
    }

    public static  void savePositions(Context context,String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("pos", value);
        editor.apply();
    }

    public static String getPositions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("pos", "");
    }

    public  static  void SaveIsBareed(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("IsBareed", value);
        editor.apply();
    }
    public  static  String getIsBareed( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("IsBareed", "");
    }

    public  static  void SaveProjectId(String value, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putString("id", value);
        editor.apply();
    }
    public  static  String getSavedprogectid( Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getString("id", "");
    }

}
