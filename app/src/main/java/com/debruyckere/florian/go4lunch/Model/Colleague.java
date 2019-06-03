package com.debruyckere.florian.go4lunch.Model;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class Colleague {

    private String id;
    private String mName;
    private String mSurname;
    private String mPictureUrl;

    public Colleague(String id, String name, String surname, String picture) {
        this.id = id;
        this.mName = name;
        this.mSurname = surname;
        this.mPictureUrl = picture;
    }
    public Colleague(String id, String name, String surname){
        this.id = id;
        this.mName = name;
        this.mSurname = surname;
    }

    public Colleague(String id, String name){
        this.id = id;
        this.mName = name;
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

    public String getPicture() {
        return mPictureUrl;
    }

    public void setPicture(String picture) {
        this.mPictureUrl = picture;
    }
}
