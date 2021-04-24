package com.yxkong.agent.utils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Author: yxkong
 * @Date: 2021/4/19 10:20 上午
 * @version: 1.0
 */
public class StringUtils {
    static Pattern pattern = Pattern.compile("[0-9]*");
    static String DOT = ".";
    /**
     * 判断字符串不为空
     * @param str
     * @return
     */
    public static Boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static Boolean isEmpty(String str){
        if(Objects.isNull(str) || "".equals(str)|| "null".equals(str)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static Boolean isNumber(String str){
        //如果是以-开头，去除
        if(str.startsWith("-")){
            str = str.replace("-","");
        }
        //判断是否有小数点
        if(str.indexOf(DOT)>0){
            //判断是否只有一个小数点
            if(str.indexOf(DOT)==str.lastIndexOf(DOT) && str.split("\\.").length==2){
                return pattern.matcher(str.replace(DOT,"")).matches();
            }
        }else {
            return pattern.matcher(str).matches();
        }
        return false;
    }
}