package com.yxkong.agent.utils;

import java.util.Objects;

/**
 * @Author: yxkong
 * @Date: 2021/4/19 10:20 上午
 * @version: 1.0
 */
public class StringUtils {

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
}