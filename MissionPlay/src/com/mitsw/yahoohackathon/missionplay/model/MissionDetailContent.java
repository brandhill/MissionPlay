package com.mitsw.yahoohackathon.missionplay.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class MissionDetailContent implements Serializable{



    private JsonArray Joiners;

    private String OwnerUserId;

    private String Descript;
    private String Name;
    private String PhotoUrl;
    private String Radious;
    private String Deadline;
    private String IsCancel;
    private String Good;
    private String Bad;
    private String Lat;
    private String Lng; 
    private String Id;
    


    public String getOwnerUserId() {
        return OwnerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        OwnerUserId = ownerUserId;
    }

    public String getDescript() {
        return Descript;
    }

    public void setDescript(String descript) {
        Descript = descript;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public String getRadious() {
        return Radious;
    }

    public void setRadious(String radious) {
        Radious = radious;
    }

    public String getGood() {
        return Good;
    }

    public void setGood(String good) {
        Good = good;
    }

    public String getIsCancel() {
        return IsCancel;
    }

    public void setIsCancel(String isCancel) {
        IsCancel = isCancel;
    }

    public String getBad() {
        return Bad;
    }

    public void setBad(String bad) {
        Bad = bad;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public JsonArray getJoiners() {
        return Joiners;
    }

    public void setJoiners(JsonArray joiners) {
        Joiners = joiners;
    }

    @Override
    public String toString() {        
        Gson gson = new Gson();
        return gson.toJson(this);        
    }

}
