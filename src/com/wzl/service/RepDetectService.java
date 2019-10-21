package com.wzl.service;

import com.wzl.Domain.News;
import com.wzl.Domain.SimHashValue;

import java.util.ArrayList;

/**
 * @author wzl
 * @Title: RepDetectService
 * @date 2019/1/28 10:13
 * ***********************************
 * @function
 */
public class RepDetectService {


    /**
     * @param filePath
     * @param dirPath
     * @param AllSimHashValue
     * @return
     */
    public static boolean simCheck(String filePath, String dirPath, ArrayList<SimHashValue> AllSimHashValue) {
        int[] Haming = {4, 5, 6};
        boolean isnotSim = true;//true为不重复

        News news = new News(filePath, ReadFile.loadFile(filePath));
        SimHashValue waitSimHashValue = SimHashService.calSimHashValue(news, dirPath);
        waitSimHashValue.newsPath = filePath;
        char[] wait_x = waitSimHashValue.fullHash_x.toCharArray();
        char[] wait_y = waitSimHashValue.fullHash_y.toCharArray();
        char[] wait_z = waitSimHashValue.fullHash_z.toCharArray();


        for (SimHashValue s : AllSimHashValue) {
            char[] s_x = new char[64];
            char[] x_tem = s.fullHash_x.toCharArray();
            for (int i = 0; i < x_tem.length; i++) {
                s_x[i] = x_tem[i];
            }
            char[] s_y = new char[80];
            char[] y_tem = s.fullHash_y.toCharArray();
            for (int i = 0; i < y_tem.length; i++) {
                s_y[i] = y_tem[i];
            }
            char[] s_z = new char[96];
            char[] z_tem = s.fullHash_z.toCharArray();
            for (int i = 0; i < z_tem.length; i++) {
                s_z[i] = z_tem[i];
            }

            int count_x = 0;
            int count_y = 0;
            int count_z = 0;

            for (int i = 0; i < 64; i++) {
                if (s_x[i] != wait_x[i]) count_x++;
            }
            if (count_x <= Haming[0]) {
                isnotSim = false;
                System.out.println(waitSimHashValue.newsPath + " 与文档 " + s.newsPath + " 的simHash_X海明距离为 " + count_x);
                break;
            }

            for (int i = 0; i < 80; i++) {
                if (s_y[i] != wait_y[i]) count_y++;
            }
            if (count_y <= Haming[1]) {
                isnotSim = false;
                System.out.println(waitSimHashValue.newsPath + " 与文档 " + s.newsPath + " 的simHash_Y海明距离为 " + count_y);
                break;
            }

            for (int i = 0; i < 96; i++) {
                if (s_z[i] != wait_z[i]) count_z++;
            }
            if (count_z <= Haming[2]) {
                isnotSim = false;
                System.out.println(waitSimHashValue.newsPath + " 与文档 " + s.newsPath + " 的simHash_Z海明距离为 " + count_z);
                break;
            }
        }
        if (isnotSim == true) AllSimHashValue.add(waitSimHashValue);

        return isnotSim;
    }
}
