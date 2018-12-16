package com.example.asamir.iraqproject.Models;

public class JobsModel
{
    public String name;
    public String num;
    public String note;



    public JobsModel(String name, String num,String note)
    {
        this.name = name;
        this.num = num;
        this.note=note;


    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public String getNote() {
        return note;
    }
}
