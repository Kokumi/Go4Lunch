package com.debruyckere.florian.go4lunch.Model;

import android.graphics.Bitmap;
import android.net.Uri;

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
    private Bitmap image;
    private int distance;
    private String phoneNumber;
    private Uri webUri;

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

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public Integer getRate() {
        return rate;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getWebUri() {
        return webUri;
    }

    public void setWebUri(Uri webUrl) {
        this.webUri = webUrl;
    }
}
