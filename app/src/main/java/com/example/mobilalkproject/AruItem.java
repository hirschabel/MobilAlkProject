package com.example.mobilalkproject;

import java.util.ArrayList;

public class AruItem {
    private String name;
    private String desc;
    private String tag;
    private int imageResource;

    public AruItem() {
    }

    public AruItem(String name, String desc, String tag, int imageResource) {
        this.name = name;
        this.desc = desc;
        this.tag = tag;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public String getTag() {
        return tag;
    }

    public int getImageResource() {
        return imageResource;
    }
}
