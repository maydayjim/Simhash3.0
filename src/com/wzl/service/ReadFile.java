package com.wzl.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author wzl
 * @Title: ReadFile
 * @date 2019/1/22 10:26
 * ***********************************
 * @function
 */
public class ReadFile {
    public static String loadFile(String filePath){
        /**
         *  public String readDataFile(String filePath) {
         *         File file = new File(filePath);
         *         StringBuilder strBuilder = null;
         *
         *         try {
         *             BufferedReader in = new BufferedReader(new FileReader(file));
         *             String str;
         *             strBuilder = new StringBuilder();
         *             while ((str = in.readLine()) != null) {
         *                 strBuilder.append(str);
         *             }
         *             in.close();
         *         } catch (IOException e) {
         *             e.getStackTrace();
         *         }
         *
         *         return strBuilder.toString();
         *     }
         */
        File file = new File(filePath);
        if(!file.isFile())
        {
            System.out.println("The input "+filePath+" is not a file or the file doesn't exist");
            System.exit(0);
        }

        StringBuilder strBuilder = new StringBuilder();
        try{
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str ;
            while ((str = in.readLine()) != null) {
                //System.out.println(str);
                strBuilder.append(str);
            }
            //System.out.println(str);
            in.close();

        }catch (IOException e){
            e.getStackTrace();
        }
        if (strBuilder == null) strBuilder.append("wzl");
        return strBuilder.toString();
    }
}
