package com.github.brandonstack.ireader.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 22693 on 2017/10/6.
 */

public class Book extends DataSupport {
    private int id;
    private String name;
    private String type;
    private String path;
    private String folder;

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


    //    @Override
//    public boolean equals(Object obj) {
//        if (((Book) obj).getId() == getId())
//            return true;
//        return false;
//    }
}
