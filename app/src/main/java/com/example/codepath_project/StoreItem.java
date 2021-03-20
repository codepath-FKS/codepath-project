package com.example.codepath_project;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.load.engine.Resource;

import java.util.Arrays;
import java.util.List;

public class StoreItem implements Parcelable {
    private int res;
    private int id;
    private String name;
    private int cost;
    private int type; // type 0 is background and type 1 is foods
    private boolean purchased;

    public StoreItem(int id, int res, int cost, String name, int type){
        this.res = res;
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.type = type;
        this.purchased = false;
    }


    protected StoreItem(Parcel in) {
        res = in.readInt();
        id = in.readInt();
        name = in.readString();
        cost = in.readInt();
        type = in.readInt();
        purchased = in.readByte() != 0;
    }

    public static final Creator<StoreItem> CREATOR = new Creator<StoreItem>() {
        @Override
        public StoreItem createFromParcel(Parcel in) {
            return new StoreItem(in);
        }

        @Override
        public StoreItem[] newArray(int size) {
            return new StoreItem[size];
        }
    };

    public int getRes() {
        return res;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public static List<StoreItem> storeItemData(){
        // data to populate the RecyclerView with
        StoreItem[] sData = {
                new StoreItem(0, R.drawable.ic_dog_food,        5,  "Standard dog food", 0),
                new StoreItem(1, R.drawable.ic_dog_food_rich,   10, "Premium dog food", 0),
                new StoreItem(2, R.drawable.bg_night_1,         0,  "Forest Night", 0),
                new StoreItem(3, R.drawable.bg_outdoor_winter,  5,  "Winterland", 0),
                new StoreItem(4, R.drawable.bg_night_2,         50, "Canyon Star", 0),
                new StoreItem(5, R.drawable.bg_outdoor_abstract,40, "Abstract", 0),
        };
        return Arrays.asList(sData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(res);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(cost);
        parcel.writeInt(type);
        parcel.writeByte((byte) (purchased ? 1 : 0));
    }

    public int getId() {
        return id;
    }
}
