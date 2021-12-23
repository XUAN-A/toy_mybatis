package com.axuan.ibatis.utils;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/13 15:02
 */
public class StringUtil {

    public static int getTargetStringNum(String str, String character) {
        String temp = str.replace(character, "");
        return str.length() - temp.length();
    }
}
