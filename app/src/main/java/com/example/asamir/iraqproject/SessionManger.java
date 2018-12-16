package com.example.asamir.iraqproject;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManger {


    private static String MyPREFERENCES="IRAQSHARED";

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

    public static  void setIsLogin(Context context,boolean isLogin)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).edit();
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
    }

    public static boolean getIsLogin(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return prefs.getBoolean("isLogin", false);
    }

}
