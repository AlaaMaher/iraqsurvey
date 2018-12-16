package com.example.asamir.iraqproject.OfflineWork.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "districTable")
public class DistricEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public   int id ;
    private String cityId;
    private String DistricName;
    private String DistricId;

    public DistricEntity() {
    }

    public DistricEntity(String cityId, String districName, String districId) {
        this.cityId = cityId;
        this.DistricName = districName;
        this.DistricId = districId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getDistricName() {
        return DistricName;
    }

    public String getDistricId() {
        return DistricId;
    }


    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setDistricName(String districName) {
        DistricName = districName;
    }

    public void setDistricId(String districId) {
        DistricId = districId;
    }

    @Override
    public String toString() {
        return "DistricEntity{" +
                "id=" + id +
                ", cityId='" + cityId + '\'' +
                ", DistricName='" + DistricName + '\'' +
                ", DistricId='" + DistricId + '\'' +
                '}';
    }
}
