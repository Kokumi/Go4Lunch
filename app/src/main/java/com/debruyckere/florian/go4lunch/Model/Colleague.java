package com.debruyckere.florian.go4lunch.Model;

import android.media.Image;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class Colleague {

    private String id;
    private String mName;
    private String mSurname;
    private Image mPicture;

    public Colleague(String id, String name, String surname, Image picture) {
        this.id = id;
        this.mName = name;
        this.mSurname = surname;
        this.mPicture = picture;
    }
    public Colleague(String id, String name, String surname){
        this.id = id;
        this.mName = name;
        this.mSurname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(String surname) {
        this.mSurname = surname;
    }

    public Image getPicture() {
        return mPicture;
    }

    public void setPicture(Image picture) {
        this.mPicture = picture;
    }
}
