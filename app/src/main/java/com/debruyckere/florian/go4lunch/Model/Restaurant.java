package com.debruyckere.florian.go4lunch.Model;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class Restaurant {

    private int id;
    private String adresse;

    public Restaurant(int id, String adresse) {
        this.id = id;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
