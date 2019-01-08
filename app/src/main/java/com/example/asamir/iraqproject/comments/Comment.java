package com.example.asamir.iraqproject.comments;

public class Comment {

    String officeId;
    String projectName ;
    String comment ;

    public Comment(String officeId, String projectName, String comment) {
        this.officeId = officeId;
        this.projectName = projectName;
        this.comment = comment;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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
