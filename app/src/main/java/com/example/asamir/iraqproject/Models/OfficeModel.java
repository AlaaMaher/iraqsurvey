package com.example.asamir.iraqproject.Models;

public class OfficeModel {
    String id,name;
    String vis;

    public OfficeModel(String id, String name,String vis) {
        this.id = id;
        this.name = name;
        this.vis=vis;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
}

