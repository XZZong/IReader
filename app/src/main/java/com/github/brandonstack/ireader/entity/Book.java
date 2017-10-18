package com.github.brandonstack.ireader.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 22693 on 2017/10/6.
 */

public class Book extends DataSupport implements Serializable {
    private int id;
    private String name;
    private String type;
    private String path;
    private String folder;
    private int order;
    private long begin;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getFolder() {
        return folder;
    }
}
