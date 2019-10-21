package com.wzl.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Properties;

/**
 * @author wzl
 * @Title: jdbcUtils
 * @date 2019/1/30 12:45
 * ***********************************
 * @function
 */
public class jdbcUtils {

    private static Properties pro = new Properties();

    static{

        try{
            InputStream in = jdbcUtils.class.getClassLoader().getResourceAsStream("dbconfig.properties");
            pro.load(in);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            Class.forName(pro.getProperty("driverClassName"));
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws IOException, Exception{
        Connection con = DriverManager.getConnection
                (pro.getProperty("url"), pro.getProperty("use"), pro.getProperty("password"));

        return con;
    }

    public static String getCotent() throws Exception{
        Connection con = getConnection();
        Statement statement = con.createStatement();
        String str = "SELECT content FROM grapnews;";
        ResultSet rs = statement.executeQuery(str);

        rs.next();
        String content = rs.getString("content");

        rs.close();
        statement.close();
        con.close();

        return content;
    }


}
