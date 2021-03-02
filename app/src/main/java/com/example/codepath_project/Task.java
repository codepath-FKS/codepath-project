package com.example.codepath_project;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Task")
public class Task extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DUEDATE = "dueDate";

    public Task(){
        // empty constructor needed for parseobj
    }

    public String getDescription() { return getString(KEY_DESCRIPTION); }

    public void setDescription(String description) { put(KEY_DESCRIPTION, description); }

    public ParseFile getPhoto() { return getParseFile(KEY_PHOTO); }

    public void setPhoto(ParseFile parseFile){ put(KEY_PHOTO, parseFile); }

    public ParseFile getAuthor() { return getParseFile(KEY_AUTHOR); }

    public void setAuthor(ParseUser user){ put(KEY_AUTHOR, user); }

    public Date getDueDate() { return getDate(KEY_DUEDATE); }

    public void setDueDate(Date date){ put(KEY_DUEDATE, date); }

}
