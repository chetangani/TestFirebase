package com.chetsgani.testfirebase;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cgani on 27-Nov-16.
 */

@IgnoreExtraProperties
public class User {

    public String username="";
    public String address="";
    public String msg="";

    public User() {

    }

    public User(String msg) {
        this.msg = msg;
    }

    public User(String username, String address) {
        this.username = username;
        this.address = address;
    }

    public User(String username, String address, String msg) {
        this.username = username;
        this.address = address;
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getMsg() {
        return msg;
    }
}
