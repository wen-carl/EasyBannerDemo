package com.seven.easybanner.model;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;

import com.seven.easybanner.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataModel {

    public static final int Net = 0;
    public static final int Disk = 1;
    public static final int Resource = 2;

    private String description;
    private int type;
    private String url;
    private @IdRes int id;

    public DataModel() {}

    public DataModel(String description, @IdRes int id) {
        this.type = Resource;
        this.description = description;
        this.id = id;
    }

    public DataModel(String description, String url) {
        this.type = Net;
        this.description = description;
        this.url = url;
    }

    public DataModel(String description, int type, String url) {
        this.type = type;
        this.description = description;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public DataModel setDescription(String description) {
        this.description = description;

        return this;
    }

    public int getType() {
        return type;
    }

    public DataModel setType(int type) {
        this.type = type;

        return this;
    }

    public String getUrl() {
        return url;
    }

    public DataModel setUrl(String url) {
        this.url = url;

        return this;
    }

    public int getId() {
        return id;
    }

    public DataModel setId(@IdRes int id) {
        this.id = id;

        return this;
    }
}
