package com.appex.edhelp.Models;

import io.realm.RealmObject;

/**
 * Created by anuraag on 4/24/16.
 */
public class Favourites extends RealmObject {

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
