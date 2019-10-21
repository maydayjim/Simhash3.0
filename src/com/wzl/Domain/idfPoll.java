package com.wzl.Domain;

import com.wzl.service.TFIDFService;

import java.util.HashMap;

/**
 * @author wzl
 * @Title: idfPoll
 * @date 2019/1/22 09:29
 * ***********************************
 * @function
 */
public class idfPoll {
    public static HashMap<String, Float> idfOfDir_1 = TFIDFService.idfForDir("src/com/wzl/text/newsOfBC");

    public static HashMap<String, Float> getIdfOfDir_1() {
        return idfOfDir_1;
    }

}
