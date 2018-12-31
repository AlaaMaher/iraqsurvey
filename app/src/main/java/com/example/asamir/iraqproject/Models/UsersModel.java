package com.example.asamir.iraqproject.Models;
//////////////////////////////////BY_Mohammed
public class UsersModel {
    public String UserName, Passowrd, UserID;

    public UsersModel() {
    }

    public UsersModel(String userName, String passowrd, String userID) {
        UserName = userName;
        Passowrd = passowrd;
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassowrd() {
        return Passowrd;
    }

    public void setPassowrd(String passowrd) {
        Passowrd = passowrd;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}

