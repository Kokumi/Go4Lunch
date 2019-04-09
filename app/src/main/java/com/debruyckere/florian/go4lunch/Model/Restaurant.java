package com.debruyckere.florian.go4lunch.Model;

import android.media.Image;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class Restaurant {

    private String id;
    private String name;



    private String address;
    private String type;
    private String open;
    private Integer rate;
    //private image image;

    public Restaurant(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public Restaurant(String id, String name, String address, String type, String open, Integer rate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.open = open;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adresse) {
        this.address = adresse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
