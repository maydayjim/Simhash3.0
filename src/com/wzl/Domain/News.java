package com.wzl.Domain;


import java.util.HashMap;

/**
 * @author wzl
 * @Title: News
 * @date 2019/1/22 08:54
 * ***********************************
 * @function
 */
public class News {

    public String newsPath;
    public String content;

    public News(String newsPath, String content) {
        this.newsPath = newsPath;
        this.content = content;
    }
}
