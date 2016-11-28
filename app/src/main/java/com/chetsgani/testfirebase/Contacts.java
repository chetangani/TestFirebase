package com.chetsgani.testfirebase;

/**
 * Created by Chetan Gani on 11/28/2016.
 */

public class Contacts {
    public String Username;
    public String ConfirmedUser;
    public boolean isSelected;

    public Contacts() {
    }

    public Contacts(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getConfirmedUser() {
        return ConfirmedUser;
    }

    public void setConfirmedUser(String confirmedUser) {
        ConfirmedUser = confirmedUser;
    }
}
