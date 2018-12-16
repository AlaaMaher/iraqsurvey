package com.example.asamir.iraqproject.OfflineWork.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "userProjects")
public class UserProjectsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String project_id;
    private String project_name;
    private String userId;

    public UserProjectsEntity(String project_id, String project_name, String userId) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserProjectsEntity{" +
                "id=" + id +
                ", project_id='" + project_id + '\'' +
                ", project_name='" + project_name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
