package com.example.asamir.iraqproject.OfflineLogin;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;
    @NonNull
    @ColumnInfo(name = "password")
    private String password;
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public User(@NonNull String email , @NonNull String password , @NonNull String id) {
        this.email = email;
        this.password = password;
        this.id = id;
    }

    @NonNull
    public String getEmail() {
        return this.email;
    }

    @NonNull
    public String getPassword() {
        return this.password;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}