package com.example.asamir.iraqproject.OfflineWork.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "govTable")
public class GovEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public   int id ;
    private String govId;
    private String govName;

    public GovEntity(String govId, String govName) {
        this.govId = govId;
        this.govName = govName;
    }

    public String getGovId() {
        return govId;
    }

    public String getGovName() {
        return govName;
    }

    @Override
    public String toString() {
        return "GovEntity{" +
                "id=" + id +
                ", govId='" + govId + '\'' +
                ", govName='" + govName + '\'' +
                '}';
    }
}
