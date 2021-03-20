package com.example.codepath_project;

import com.bumptech.glide.load.engine.Resource;

public class StoreItem {
    private int res;
    private String name;
    private int cost;
    private boolean purchased;

    public StoreItem(int res, int cost, String name){
        this.res = res;
        this.cost = cost;
        this.name = name;
    }

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
}
