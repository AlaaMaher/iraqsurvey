package com.example.asamir.iraqproject.OfflineWork.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cityTable")
public class CityEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public   int id ;

    private String cityId;
    private String cityName;
    private String govId;

    public CityEntity(String cityId, String cityName, String govId) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.govId = govId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getGovId() {
        return govId;
    }

    @Override
    public String toString() {
        return "CityEntity{" +
                "id=" + id +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", govId='" + govId + '\'' +
                '}';
    }
}
