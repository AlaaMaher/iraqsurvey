package com.example.asamir.iraqproject.Models;

public class ProjectsModel {
    String id,name;

    public ProjectsModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
