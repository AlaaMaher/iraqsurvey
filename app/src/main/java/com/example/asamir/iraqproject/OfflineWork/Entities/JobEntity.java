package com.example.asamir.iraqproject.OfflineWork.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "jobTable")
public class JobEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public   int id ;

    private String jobId;
    private String jobName;
    String projectId;

    public JobEntity( String jobId, String jobName, String projectId) {
    //public JobEntity( String jobId, String jobName) {

        this.jobId = jobId;
        this.jobName = jobName;
        this.projectId=projectId;
    }


    @NonNull
    public int getId() {
        return id;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "JobEntity{" +
                "id=" + id +
                ", jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +

                '}';
    }
}
