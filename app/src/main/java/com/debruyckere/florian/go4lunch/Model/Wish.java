package com.debruyckere.florian.go4lunch.Model;

import java.util.Date;

/**
 * Created by Debruyckère Florian on 02/01/2019.
 */
public class Wish {

    private Date mDate;
    private Colleague mColleague;
    private Restaurant mRestaurant;

    public Wish(Date date, Colleague colleague, Restaurant restaurant) {
        mDate = date;
        mColleague = colleague;
        mRestaurant = restaurant;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Colleague getColleague() {
        return mColleague;
    }

    public void setColleague(Colleague colleague) {
        mColleague = colleague;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        mRestaurant = restaurant;
    }
}
