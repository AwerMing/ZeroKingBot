package com.zkb.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularMatch {
    /**
     * 获取中文
     */
    public static String getChines(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]*?\\&?[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group());
        }
        return builder.toString();
    }

    /**
     * 判断是否是武器名称
     */
    public static boolean isWeaponsName(String str) {
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5]*?\\&?[\u4e00-\u9fa5]*?[A-z]*?-?[A-z]*?$");
        Pattern compile = Pattern.compile("^[\u4e00-\u9fa5]*?\\&?[\u4e00-\u9fa5]*$");
        Matcher m = p.matcher(str.trim());
        Matcher m2 = compile.matcher(str.trim());
        return m.matches() || m2.matches();
    }

    public static String getRivenNameE(String str) {
        Pattern p = Pattern.compile("[a-zA-z]*-?$");
        Matcher m = p.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group());
        }
        return builder.toString();
    }

    /**
     * 获取紫卡名称
     */
    public static String getRivenName(String str) {
        Pattern p = Pattern.compile("[a-zA-z]*-[a-zA-z]*$");
        Matcher m = p.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group());
        }
        return builder.toString();
    }

    /**
     * 判断是否是属性词条
     */
    public static boolean isAttribute(String str) {
        Pattern p = Pattern.compile(".[+-]?\\d+(\\.\\d+)?%?.?[\u4e00-\u9fa5]*?[a-zA-Z]*?[\u4e00-\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 获取属性数值
     */
    public static Double getAttributeNum(String str) {
        String regex = "[+-]?\\d+(\\.\\d+)?%?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group());
            break;
        }
        return Double.valueOf(builder.toString().replace("%", "").trim());
    }
    /**
     * 获取属性名称
     */
    public static String getAttribetName(String str) {
        String regex = "[\u4e00-\u9fa5]*?[a-zA-Z]*?[\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group());
        }
        return builder.toString().trim();
    }

    /**
     * 检测是否是紫卡名称
     */
    public static boolean isRivenName(String str) {
        Pattern p = Pattern.compile("^[a-zA-z]*-[a-zA-z]*$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isRivenNameEx(String str){
        Pattern p = Pattern.compile("^[a-zA-z]*$");
        Matcher m = p.matcher(str);
        return isRivenName(str)||m.matches();
    }
}
