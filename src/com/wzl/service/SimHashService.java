package com.wzl.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.wzl.Domain.News;
import com.wzl.Domain.SimHashValue;
import com.wzl.Domain.idfPoll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author wzl
 * @Title: SimHashService
 * @date 2019/1/23 10:08
 * ***********************************
 * @function
 */
public class SimHashService {


    /**
     * @param news
     * @param dirPath
     * @return the SimHash
     */
//    public static SimHashValue generateSimHashValue(News news,String dirPath){
//        String newsPath = news.newsPath;
//        SimHashValue simHashValue = new SimHashValue();
//        String fullHash_x = ;
//        /**
//         * 这里要补全，想一想用什么形式存储哈希，三段怎么赋值
//         */
//
//        return simHashValue;
//    }
    public static SimHashValue calSimHashValue(News news, String dirPath) {

        SimHashValue simHashValue = new SimHashValue();

        simHashValue.newsPath = news.newsPath;
        String content = news.content;
        HashMap<String, Float> tfOfNews = TFIDFService.getTF(content);
        //HashMap<String, Float> idfOfDir = TFIDFService.idfForDir(dirPath);
        HashMap<String, Float> idfOfDir = idfPoll.getIdfOfDir_1();

        int[] longNum = {64, 80, 96};
        String[] fullHash = {"", "", "",};

        for (int num = 0; num < 3; num++) {

            float weight;
            long hashValue;
            int NUM = longNum[num];

            int[] binaryArray = new int[NUM];
            double[] hashArray = new double[NUM];
            int[] resultValue = new int[NUM];


            List<Term> terms = new ArrayList<Term>();
            ArrayList<String> words = new ArrayList<String>();

            terms = HanLP.segment(content);
            for (Term t : terms) {
                if (TFIDFService.shouldInclude(t)) words.add(t.word);
            }

            for (String str : words) {
                //if(!tfOfNews.containsKey(str)) continue;
                float tfValue = tfOfNews.get(str);
                float idfValue;
                if (idfOfDir.containsKey(str)) idfValue = idfOfDir.get(str);
                else continue;

                /**
                 * 切换两个算法的开关
                 */
                //weight = tfValue;
                weight = tfValue * idfValue;

                //System.out.println(str+":"+weight+",   tf:"+tfValue+",   idf:"+idfValue);//此语句负责各个值得输出，用来调试和排雷
                //计算出一个特定位数的hash
                hashValue = BKDRHash(str);
                hashValue %= Math.pow(2, NUM);

                numToBinaryArray(binaryArray, (int) hashValue);

                for (int i = 0; i < binaryArray.length; i++) {
                    if (binaryArray[i] == 1) hashArray[i] += weight;
                    else hashArray[i] -= weight;
                }
            }

            for (int j = 0; j < hashArray.length; j++) {
                if (hashArray[j] > 0) resultValue[j] = 1;
                else resultValue[j] = 0;
            }

            for (int a = 0; a < NUM; a++) {
                fullHash[num] = fullHash[num] + resultValue[a];
            }

        }


        simHashValue.fullHash_x = fullHash[0];
        simHashValue.fullHash_y = fullHash[1];
        simHashValue.fullHash_z = fullHash[2];

        return simHashValue;

    }


    private static void numToBinaryArray(int[] binaryArray, int num) {
        int index = 0;
        int temp = 0;
        while (num != 0) {
            binaryArray[index] = num % 2;
            index++;
            num /= 2;
        }

        // 进行数组前和尾部的调换
        for (int i = 0; i < binaryArray.length / 2; i++) {
            temp = binaryArray[i];
            binaryArray[i] = binaryArray[binaryArray.length - 1 - i];
            binaryArray[binaryArray.length - 1 - i] = temp;
        }
    }


    private static long BKDRHash(String str) {
        int seed = 31; /* 31 131 1313 13131 131313 etc.. */
        long hash = 0;
        int i;

        for (i = 0; i < str.length(); i++) {
            hash = (hash * seed) + (str.charAt(i));
        }

        hash = Math.abs(hash);
        return hash;
    }


}
