package com.mitsw.yahoohackathon.missionplay.model;

import java.io.Serializable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class MissionDetailList implements Serializable{


    private int StatusEnum;
    
    private JsonArray Data;

    public int getStatusEnum() {
        return StatusEnum;
    }

    public void setStatusEnum(int statusEnum) {
        StatusEnum = statusEnum;
    }

    public JsonArray getData() {
        return Data;
    }

    public void setData(JsonArray data) {
        Data = data;
    }



    

}
