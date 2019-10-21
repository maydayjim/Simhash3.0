package com.wzl.test;


import com.wzl.Domain.SimHashValue;
import com.wzl.jdbc.BuildFile;
import com.wzl.jdbc.jdbcUtils;
import com.wzl.service.RepDetectService;
import com.wzl.service.SimHashService;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * @author wzl
 * @Title: mainTest
 * @date 2019/2/27 13:19
 * ***********************************
 * @function
 */
public class mainTest {
    @Test
    public void testConnection(){
        try{
            Connection con = jdbcUtils.getConnection();
            //System.out.println(con);

            //Statement statement = con.createStatement();

            con.close();
        }catch (Exception e){
            e.printStackTrace();;
        }

    }

    @Test
    public void testBuildFile() throws Exception{
        BuildFile.createFile(1,1000);
    }

    @Test
    public  void testSimHaahService() throws Exception{
        ArrayList<String> simList = new ArrayList<>();
        ArrayList<String> notSimList = new ArrayList<>();

        ArrayList<SimHashValue> allSimHash = new ArrayList<>();

        String dir ="src/com/wzl/text/newsOfBC";
        Boolean isnotSim = true;

        int acount = 0;

        for(int i = 1;i<=1000;i++){
            String file = "src/com/wzl/text/newsOfBC/news_"+i;
            isnotSim = RepDetectService.simCheck(file,dir,allSimHash);
            if(isnotSim == false)acount++;
        }

        System.out.println("");
        System.out.println("****************************");
        System.out.println("");

        for (int i = 1; i <= 100 ; i++) {
            int j = 1000+i;
            String file = "src/com/wzl/text/newsOfBC/news_"+j;
            isnotSim = RepDetectService.simCheck(file,dir,allSimHash);
            if(isnotSim == true){
                notSimList.add(file);
            }else{
                simList.add(file);
            }

        }


        System.out.println("");
        System.out.println("****************************");
        System.out.println("");

        System.out.println(acount+"篇原始文档已经重复了!");

        System.out.println("");
        System.out.println("****************************");
        System.out.println("");

        System.out.println("检测出相似文档数："+simList.size()+".它们是：");
        for (String a: simList ) {
            System.out.println(a);
        }

        System.out.println("");
        System.out.println("****************************");
        System.out.println("");

        System.out.println("检测出不相似文档数"+notSimList.size()+".它们是：");
        for (String a: notSimList ) {
            System.out.println(a);
        }

    }
}
