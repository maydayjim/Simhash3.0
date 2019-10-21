package com.wzl.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

/**
 * @author wzl
 * @Title: TFIDFService
 * @date 2019/1/22 10:24
 * ***********************************
 * @function
 */
public class TFIDFService {
    public static HashMap<String, Float> getTF(String content){
        List<Term> terms=new ArrayList<Term>();
        ArrayList<String> words = new ArrayList<String>();

        terms=HanLP.segment(content);
        for(Term t:terms)
        {
            if(TFIDFService.shouldInclude(t))
            {
                words.add(t.word);
            }
        }

        // get TF values
        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
        HashMap<String, Float> TFValues = new HashMap<String, Float>();

        //统计词频
        for(String word : words)
        {
            if(wordCount.get(word) == null)
            {
                wordCount.put(word, 1);
            }
            else
            {
                wordCount.put(word, wordCount.get(word) + 1);
            }
        }

        int wordLen = words.size();

        //traverse the HashMap
        Iterator<Map.Entry<String, Integer>> iter = wordCount.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
            TFValues.put(entry.getKey().toString(), 100*(Float.parseFloat(entry.getValue().toString()) / wordLen));
            //System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }
        return TFValues;

    }

    /**
     * judge whether a word belongs to stop words
     * @param term(Term): word needed to be judged
     * @return(boolean):  if the word is a stop word,return false;otherwise return true
     */
    public static boolean shouldInclude(Term term)
    {
        return CoreStopWordDictionary.shouldInclude(term);
    }

    /**
     * calculate TF values for each word of each file under a directory
     * @param dirPath(String): path of the directory
     * @return(HashMap<String,HashMap<String, Float>>): path of file and its  corresponding "word-TF Value" pairs
     * @throws
     */
    public static HashMap<String,HashMap<String, Float>> tfForDir(String dirPath)
    {
        HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
        List<String> filelist = ReadDir.readDirFileNames(dirPath);

        for(String file : filelist)
        {
            HashMap<String, Float> dict = new HashMap<String, Float>();
            String content = ReadFile.loadFile(file); // remember to modify the loadFile method of class ReadFile
            dict = TFIDFService.getTF(content);
            allTF.put(file, dict);
        }
        return allTF;
    }


    /**
     * calculate IDF values for each word  under a directory
     * @param dirPath(String): path of the directory
     * @return(HashMap<String, Float>): "word:IDF Value" pairs
     */
    public static HashMap<String, Float> idfForDir(String dirPath){

        List<String> fileList = ReadDir.readDirFileNames(dirPath);
        int docNum = fileList.size();

        Map<String, Set<String>> passageWords = new HashMap<String, Set<String>>();
        // get words that are not repeated of a file
        for(String filePath:fileList)
        {
            List<Term> terms=new ArrayList<Term>();
            Set<String> words = new HashSet<String>();
            String content = ReadFile.loadFile(filePath); // remember to modify the loadFile method of class ReadFile
            terms=HanLP.segment(content);
            for(Term t:terms)
            {
                if(TFIDFService.shouldInclude(t))
                {
                    words.add(t.word);
                }
            }
            passageWords.put(filePath, words);
        }

        // get IDF values
        HashMap<String, Integer> wordPassageNum = new HashMap<String, Integer>();//每个单词在几个文档中出现过
        for(String filePath : fileList)
        {
            Set<String> wordSet = new HashSet<String>();
            wordSet = passageWords.get(filePath);
            for(String word:wordSet)
            {
                if(wordPassageNum.get(word) == null)
                    wordPassageNum.put(word,1);
                else
                    wordPassageNum.put(word, wordPassageNum.get(word) + 1);
            }
        }

        HashMap<String, Float> wordIDF = new HashMap<String, Float>();
        Iterator<Map.Entry<String, Integer>> iter_dict = wordPassageNum.entrySet().iterator();
        while(iter_dict.hasNext())
        {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter_dict.next();
            float value = (float)Math.log( (docNum+2) / (1+Float.parseFloat(entry.getValue().toString())) );
            wordIDF.put(entry.getKey().toString(), value);
            //System.out.println(entry.getKey().toString() + "=" +value);
        }
        return wordIDF;
    }

    /**
     * calculate TF-IDF value for each word of each file under a directory
     * @param dirPath(String): path of the directory
     * @return(Map<String, HashMap<String, Float>>): path of file and its corresponding "word:TF-IDF Value" pairs
     */
    public static Map<String, HashMap<String, Float>> getDirTFIDF(String dirPath)
    {
        HashMap<String, HashMap<String, Float>> dirFilesTF = new HashMap<String, HashMap<String, Float>>();
        HashMap<String, Float> dirFilesIDF = new HashMap<String, Float>();

        dirFilesTF = TFIDFService.tfForDir(dirPath);
        dirFilesIDF = TFIDFService.idfForDir(dirPath);

        Map<String, HashMap<String, Float>> dirFilesTFIDF = new HashMap<String, HashMap<String, Float>>();
        Map<String,Float> singlePassageWord= new HashMap<String,Float>();
        List<String> fileList = new ArrayList<String>();
        fileList = ReadDir.readDirFileNames(dirPath);
        for (String filePath: fileList)
        {
            HashMap<String,Float> temp= new HashMap<String,Float>();
            singlePassageWord = dirFilesTF.get(filePath);
            Iterator<Map.Entry<String, Float>> it = singlePassageWord.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<String, Float> entry = it.next();
                String word = entry.getKey();
                Float TFIDF = entry.getValue()*dirFilesIDF.get(word);
                temp.put(word, TFIDF);
            }
            dirFilesTFIDF.put(filePath, temp);
        }
        return dirFilesTFIDF;
    }

    /**
     *
     * @param word
     * @param filePath
     * @param dirPath
     * @return the tfidf value about a word at a filePath
     */
    public static float getWordTFIDF(String word,String filePath,String dirPath){
        Map<String, HashMap<String, Float>> mapTFIDF = new HashMap<String, HashMap<String, Float>>();
        mapTFIDF = TFIDFService.getDirTFIDF(dirPath);

        return mapTFIDF.get(filePath).get(word);
    }


}
