package com.example.asamir.iraqproject.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.asamir.iraqproject.LoginActivity;
import com.example.asamir.iraqproject.Models.UsersModel;

//////////////////////////////////BY_Mohammed
public class Sesstion {
    private static final String SHARED_PREF_NAME = "userSession1258";
    private static final String TAYAR_ID = "user_id1258";
    private static final String TAYAR_EMAIL = "user_email1258";
    private static final String TAYAR_PASS = "user_pass1258";
    private static Sesstion mInstance;
    private static Context mCtx;
    private Sesstion(Context context) {
        mCtx = context;
    }
    public static synchronized Sesstion getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Sesstion(context);
        }
        return mInstance;
    }
    public void userLogin(UsersModel usersModel) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAYAR_ID, usersModel.getUserID());
        editor.putString(TAYAR_EMAIL, usersModel.getUserName());
        editor.putString(TAYAR_PASS, usersModel.getPassowrd());
        editor.apply();
    }
//    public boolean isLoggedIn() {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getString(TAYAR_ID, null) != null;
//    }
    public UsersModel getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UsersModel( sharedPreferences.getString(TAYAR_ID, null) ,
                sharedPreferences.getString(TAYAR_EMAIL, null),
                sharedPreferences.getString(TAYAR_PASS, null));
    }
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

//    public void editProfileUser(String profileImage) {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(TAYAR_IMAGE, profileImage);
//        editor.apply();
//    }

//    public void editPhoneUser(String userPhone) {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(TAYAR_PHONE2, userPhone);
//        editor.apply();
//    }
}
