package com.wzl.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wzl
 * @Title: RegexUtils
 * @date 2019/1/27 12:47
 * ***********************************
 * @function
 */
public class RegexUtils {
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

    /**
     *
     * @param htmlStr
     * @return 去除标签化之后的新闻
     */
    public static String delHtmlTag(String htmlStr){
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_html2 = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_html2= p_html2.matcher(htmlStr);
        htmlStr = m_html2.replaceAll(""); // 过滤html标签


        return htmlStr;
    }
}

