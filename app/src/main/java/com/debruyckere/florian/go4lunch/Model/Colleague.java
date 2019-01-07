package com.debruyckere.florian.go4lunch.Model;

import android.media.Image;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class Colleague {

    private int id;
    private String mName;
    private String mSurname;
    private Image mPicture;

    public Colleague(int id, String name, String surname, Image picture) {
        this.id = id;
        this.mName = name;
        this.mSurname = surname;
        this.mPicture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
