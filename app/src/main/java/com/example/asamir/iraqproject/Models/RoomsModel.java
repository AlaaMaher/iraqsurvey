package com.example.asamir.iraqproject.Models;

public class RoomsModel
{
    public String name;
    public String num;
    public String roomThings;



    public RoomsModel(String name, String num, String roomThings)
    {
        this.name = name;
        this.num = num;
        this.roomThings = roomThings;


    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public String getRoomThings() {
        return roomThings;
    }

}
