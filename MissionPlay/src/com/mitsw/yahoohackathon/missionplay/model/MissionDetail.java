package com.mitsw.yahoohackathon.missionplay.model;

import java.io.Serializable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class MissionDetail implements Serializable{


    private int StatusEnum;
    
    private JsonObject Data;

    public int getStatusEnum() {
        return StatusEnum;
    }

    public void setStatusEnum(int statusEnum) {
        StatusEnum = statusEnum;
    }

    public JsonObject getData() {
        return Data;
    }

    public void setData(JsonObject data) {
        Data = data;
    }




    

}
