package com.example.asamir.iraqproject.comments;

public class Comment {

    String projectId ;
    String projectName ;
    String comment ;

    public Comment(String projectId, String projectName, String comment) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.comment = comment;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
