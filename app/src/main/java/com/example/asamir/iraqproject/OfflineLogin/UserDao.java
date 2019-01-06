package com.example.asamir.iraqproject.OfflineLogin;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface UserDao {

    @Query("SELECT * from user_table where email = :email1 and password = :password")
    LiveData<User> getUser(String email1 , String password);
    @Insert
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();
}
