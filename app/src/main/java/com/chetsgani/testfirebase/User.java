package com.chetsgani.testfirebase;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cgani on 27-Nov-16.
 */

@IgnoreExtraProperties
public class User {

    public String msg="";

    public User() {

    }

    public User(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
