package com.github.brandonstack.ireader.Entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 22693 on 2017/10/6.
 */

public class Book extends DataSupport {
    private int id;
    private String name;
    private String type;
    private String path;
    //    字体
    private String charset;

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

    public void setCharset(String charset) {
        this.charset = charset;
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

    public String getCharset() {
        return charset;
    }
}
