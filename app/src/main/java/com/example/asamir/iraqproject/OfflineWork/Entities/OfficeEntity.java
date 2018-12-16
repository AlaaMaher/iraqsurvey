package com.example.asamir.iraqproject.OfflineWork.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "officeTable")
public class OfficeEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public   int id ;
    private String OfficeId;
    private String officeName;
    private String districId;
    private String project_id;

    public OfficeEntity(String officeId, String officeName, String districId, String project_id) {
        this.OfficeId = officeId;
        this.officeName = officeName;
        this.districId = districId;
        this.project_id = project_id;
    }

    public OfficeEntity() {
    }

    public String getOfficeId() {
        return OfficeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getDistricId() {
        return districId;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setOfficeId(String officeId) {
        OfficeId = officeId;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setDistricId(String districId) {
        this.districId = districId;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    @Override
    public String toString() {
        return "OfficeEntity{" +
                "id=" + id +
                ", OfficeId='" + OfficeId + '\'' +
                ", officeName='" + officeName + '\'' +
                ", districId='" + districId + '\'' +
                ", project_id='" + project_id + '\'' +
                '}';
    }
}
