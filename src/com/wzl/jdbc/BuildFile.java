package com.wzl.jdbc;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author wzl
 * @Title: BulidFile
 * @date 2019/1/29 12:44
 * ***********************************
 * @function
 */
public class BuildFile {
    /**
     * 这个方法用来循环创建文件，编号从n到m，每个文件存放一个数据库的content
     */
    public static void createFile(int n,int m) throws Exception{

        Connection con = jdbcUtils.getConnection();
        Statement statement = con.createStatement();
        String str = "SELECT * FROM chinanews;";
        ResultSet rs = statement.executeQuery(str);

        File f  = new File("src/com/wzl/text/newsOfBC/news_" +n);
        FileOutputStream osf = new FileOutputStream(f);


        while( n<=m ){
            rs.next();
            String s = rs.getString("content");
            String s2 = RegexUtils.delHtmlTag(s);


            f  = new File("src/com/wzl/text/newsOfBC/news_" +n);
            //File file = File.createTempFile("TrainText"+n, ".Text", dir);
            osf = new FileOutputStream(f);

            osf.write(s2.getBytes());

            n++;
        }

        osf.close();

        rs.close();
        statement.close();
        con.close();


    }
}