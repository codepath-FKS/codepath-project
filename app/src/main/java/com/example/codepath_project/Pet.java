package com.example.codepath_project;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Pet")
public class Pet extends ParseObject {
    public static final String KEY_POINTS= "points";
    public static final String KEY_HEALTH = "health";
    public static final String KEY_NAME = "name";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_PURCHASES = "purchases";
    public static final String KEY_BG = "bg";

    public Pet(){
        // empty constructor needed for parseobj
    }

    public int getPoints() { return getInt(KEY_POINTS); }

    public void setPoints(int points) { put(KEY_POINTS, points); }

    public int  getHealth() { return getInt(KEY_HEALTH); }

    public void setHealth(int health) { put(KEY_HEALTH, health); }

    public String  getName() { return getString(KEY_NAME); }

    public void setName(String name) { put(KEY_HEALTH, name); }

    public ParseUser getOwner() { return getParseUser(KEY_OWNER); }

    public void setOwner(ParseUser user){ put(KEY_OWNER, user); }




}
