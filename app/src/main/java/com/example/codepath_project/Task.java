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
    public static final String KEY_PUBLIC = "public";
    public static final String KEY_COMPLETE = "complete";
    public static final String KEY_APPROVED = "approved";
    public static final String KEY_REJECTED = "rejected";

    public static final String KEY_APPROVAL = "approved";
    public static final String KEY_CREATEDATE = "createdAt";

    public Task(){
        // empty constructor needed for parseobj
    }

    public String getDescription() { return getString(KEY_DESCRIPTION); }

    public void setDescription(String description) { put(KEY_DESCRIPTION, description); }

    public ParseFile getPhoto() { return getParseFile(KEY_PHOTO); }

    public void setPhoto(ParseFile parseFile){ put(KEY_PHOTO, parseFile); }

    public ParseUser getAuthor() { return getParseUser(KEY_AUTHOR); }

    public void setAuthor(ParseUser user){ put(KEY_AUTHOR, user); }

    public Date getDueDate() { return getDate(KEY_DUEDATE); }

    public void setDueDate(Date date){ put(KEY_DUEDATE, date); }

    public boolean isPublic() { return getBoolean(KEY_PUBLIC); }

    public void setPublic(boolean publicity){ put(KEY_PUBLIC, publicity);}

    public boolean isComplete() { return getBoolean(KEY_COMPLETE); }

    public void setComplete(boolean complete){ put(KEY_COMPLETE, complete);}

    public boolean isApproved() { return getBoolean(KEY_APPROVED); }

    public void setApproved(boolean approved){ put(KEY_APPROVED, approved);}

    public boolean getApproval() { return getBoolean(KEY_APPROVAL);}

    public void setApproval(Boolean boo) {put(KEY_APPROVAL, boo);}

    public boolean getCompleted() { return getBoolean(KEY_COMPLETE);}

    public void setCompleted(Boolean boo) {put(KEY_COMPLETE, boo);}

    public boolean getRejected() { return getBoolean(KEY_REJECTED); }

    public void setRejected(Boolean boo) {put(KEY_REJECTED, boo);}

    public Date getCreateDate() { return getDate(KEY_CREATEDATE); }


    // for AdvTasksadapter.getItemId
    public long getLongId() {
        String id = getObjectId();
        long longId = 0;
        for(int i = 0; i < 10; i++)
        {
            longId *= 10;
            longId += id.charAt(i);
        }
        return longId;
    }

}
