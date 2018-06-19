package com.lehoangduy.smartworld.Model;

/**
 * Created by Admin on 12/12/2016.
 */

public class User {
    int Id;
    public String Email;
    public String Pass;

    public User(int id, String email, String pass) {
        Id = id;
        Email = email;
        Pass = pass;
    }
}
